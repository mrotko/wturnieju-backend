package pl.wturnieju.tournament.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ComparisonChain;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gameeditor.GameEditorFactory;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.GameResultType;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.StageType;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.Group;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableGeneratorBuilder;

@RequiredArgsConstructor
public abstract class TournamentSystem {

    protected final IGameFixtureService gameFixtureService;

    protected final IGroupService groupService;

    protected final IParticipantService participantsService;

    protected final Tournament tournament;

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

    public void startTournament() {
        prepareParticipantsBeforeStart();
    }

    public abstract void finishTournament();

    public abstract void startNextTournamentStage();

    public TournamentTable buildTournamentTable() {
        var tableGenerator = TournamentTableGeneratorBuilder.builder()
                .withGames(getLeagueStageEndedGames())
                .withParticipants(getParticipants(tournament.getParticipantIds()))
                .withPointsForWin(getPoints(GameResultType.WIN))
                .withPointsForDraw(getPoints(GameResultType.DRAW))
                .withPointsForLose(getPoints(GameResultType.LOSE))
                .withRowComparator(((o1, o2) -> ComparisonChain.start()
                        .compare(o2.getPoints(), o1.getPoints())
                        .compare(o2.getSmallPoints(), o2.getSmallPoints())
                        .result()))
                .build();

        return tableGenerator.generateTable();
    }

    public List<TournamentTable> buildGroupsTables() {
        List<TournamentTable> groupTables = new ArrayList<>();

        getGroupStageEndedGames().forEach((key, value) -> {
            var tableGenerator = TournamentTableGeneratorBuilder.builder()
                    .withGames(value)
                    .withParticipants(getParticipants(key))
                    .withPointsForWin(getPoints(GameResultType.WIN))
                    .withPointsForDraw(getPoints(GameResultType.DRAW))
                    .withPointsForLose(getPoints(GameResultType.LOSE))
                    .withRowComparator(((o1, o2) -> ComparisonChain.start()
                            .compare(o2.getPoints(), o1.getPoints())
                            .compare(o2.getSmallPoints(), o2.getSmallPoints())
                            .result()))
                    .build();

            groupTables.add(tableGenerator.generateTable());
        });

        return groupTables;
    }

    private List<Participant> getParticipants(Group group) {
        return participantsService.getAllByGroupId(group.getId());
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

    protected Group createLeagueGroup() {
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

    private Group getGroupById(String groupId) {
        return groupService.getById(groupId);
    }

    public Map<Group, List<GameFixture>> getGroupStageEndedGames() {
        return gameFixtureService.getAllByTournamentId(tournament.getId()).stream()
                .filter(game -> game.getStageType() == StageType.GROUP)
                .filter(game -> game.getGameStatus() == GameStatus.ENDED)
                .collect(Collectors.groupingBy(game -> getGroupById(game.getGroupId()), Collectors.toList()));
    }

    public List<GameFixture> getLeagueStageEndedGames() {
        return gameFixtureService.getAllByTournamentId(tournament.getId()).stream()
                .filter(game -> game.getStageType() == StageType.LEAGUE)
                .filter(game -> game.getGameStatus() == GameStatus.ENDED)
                .collect(Collectors.toList());
    }

    protected List<Participant> getParticipants(List<String> participantIds) {
        return participantsService.getAllById(participantIds);
    }
}
