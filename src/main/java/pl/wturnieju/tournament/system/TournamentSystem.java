package pl.wturnieju.tournament.system;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ComparisonChain;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gameeditor.GameEditorFactory;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.GameResultType;
import pl.wturnieju.tournament.LegType;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.StageType;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.TournamentStatus;
import pl.wturnieju.tournament.system.state.Group;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableGeneratorBuilder;

@RequiredArgsConstructor
public abstract class TournamentSystem {


    protected final IGameFixtureService gameFixtureService;

    protected final IGroupService groupService;

    protected final IParticipantService participantsService;

    protected final Tournament tournament;

    public void startTournament() {
        prepareParticipantsBeforeStart();
        tournament.getRequirements().setPlannedRounds(calculatePlannedRounds());
        tournament.setRoundToLegMapping(createRoundToLegMapping());
    }

    protected void prepareParticipantsBeforeStart() {
        var participants = participantsService.getAllByTournamentId(tournament.getId());

        var toRemove = participants.stream()
                .filter(p -> p.getInvitationStatus() != InvitationStatus.ACCEPTED)
                .map(Participant::getId)
                .collect(Collectors.toList());

        var accepted = participants.stream()
                .filter(p -> p.getInvitationStatus() == InvitationStatus.ACCEPTED)
                .collect(Collectors.toList());

        accepted.forEach(p -> p.setParticipantStatus(ParticipantStatus.ACTIVE));

        tournament.setParticipantIds(accepted.stream().map(Participant::getId).collect(Collectors.toList()));
        participantsService.deleteAllById(toRemove);
        participantsService.updateAll(accepted);
    }

    protected abstract Map<Integer, LegType> createRoundToLegMapping();


    protected void prepareSingleGroupTournament() {
        var group = createLeagueStageGroup();
        groupService.insert(group);
        tournament.setGroupIds(Collections.singletonList(group.getId()));
    }

    protected int makeParticipantsNumberEven(int participantsNumber) {
        if (participantsNumber % 2 == 1) {
            return participantsNumber + 1;
        }
        return participantsNumber;
    }

    protected abstract int calculatePlannedRounds();

    public void finishTournament() {
        tournament.setStatus(TournamentStatus.ENDED);
    }

    public abstract void startNextTournamentStage();

    public TournamentTable buildTournamentTable(String groupId) {
        var tableGenerator = TournamentTableGeneratorBuilder.builder()
                .withGames(getAllEndedGamesByGroupId(groupId))
                .withParticipants(getAllParticipantsByGroupId(groupId))
                .withPointsForWin(getPoints(GameResultType.WIN))
                .withPointsForDraw(getPoints(GameResultType.DRAW))
                .withPointsForLose(getPoints(GameResultType.LOSE))
                .withRowComparator(((o1, o2) -> ComparisonChain.start()
                        .compare(o2.getPoints(), o1.getPoints())
                        .compare(o2.getSmallPoints(), o2.getSmallPoints())
                        .result()))
                .build();
        var table = tableGenerator.generateTable();
        table.setGroupId(groupId);
        return table;
    }

    private Double getPoints(GameResultType gameResultType) {
        return tournament.getScoring().getOrDefault(gameResultType, 0.);
    }

    public GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent) {
        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(getGameById(startGameUpdateEvent.getGameId()));
        var game = editor.startGame(startGameUpdateEvent);

        return gameFixtureService.update(game);
    }

    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(getGameById(finishGameUpdateEvent.getGameId()));
        var game = editor.finishGame(finishGameUpdateEvent);

        return gameFixtureService.update(game);
    }

    private GameFixture getGameById(String gameId) {
        return gameFixtureService.getById(gameId);
    }

    protected Group createLeagueStageGroup() {
        var group = new Group();

        group.setName(getTournament().getName());
        group.setParticipantIds(getTournament().getParticipantIds());
        group.setStageType(StageType.LEAGUE);
        group.setTournamentId(getTournament().getId());
        group.setRequiredAllGamesEnded(isRequiredAllGamesEnded(StageType.LEAGUE));

        return group;
    }

    private boolean isRequiredAllGamesEnded(StageType stageType) {
        return getTournament().getRequirements().getRequiredAllGamesEndedStageTypes().contains(stageType);
    }

    public Tournament getTournament() {
        return tournament;
    }

    public List<GameFixture> getAllEndedGamesByGroupId(String groupId) {
        return gameFixtureService.getAllEndedByGroupId(groupId);
    }

    public List<Participant> getAllParticipantsByGroupId(String groupId) {
        return participantsService.getAllByGroupId(groupId);
    }
}
