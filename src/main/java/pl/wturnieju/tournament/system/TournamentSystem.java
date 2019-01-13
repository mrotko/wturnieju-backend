package pl.wturnieju.tournament.system;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.Team;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;
import pl.wturnieju.tournament.system.table.SwissTournamentTableRow;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableRow;

public abstract class TournamentSystem<T extends SystemState> {

    protected final ISystemStateService<T> stateService;

    private final Tournament tournament;

    public TournamentSystem(ISystemStateService<T> stateService, Tournament tournament) {
        this.stateService = stateService;
        this.tournament = tournament;
    }

    protected void prepareParticipantsBeforeStart() {
        var participants = tournament.getParticipants();
        participants.removeIf(p -> p.getInvitationStatus() != InvitationStatus.ACCEPTED);
        participants.forEach(p -> p.setParticipantStatus(ParticipantStatus.ACTIVE));
    }

    public void startTournament() {
        prepareParticipantsBeforeStart();
        createSystemState();
    }


    protected abstract void createSystemState();

    public abstract void finishTournament();

    public abstract TournamentTable<TournamentTableRow> buildTournamentTable();

    protected void createRowsIfNotExists(Map<String, SwissTournamentTableRow> teamIdToRow, GameFixture game) {
        if (!teamIdToRow.containsKey(game.getHomeTeam().getId())) {
            teamIdToRow.put(game.getHomeTeam().getId(),
                    new SwissTournamentTableRow(game.getHomeTeam().getId(), game.getHomeTeam().getName()));
        }

        Optional.ofNullable(game.getAwayTeam()).map(Team::getId).ifPresent(awayId -> {
            if (!teamIdToRow.containsKey(awayId)) {
                teamIdToRow.put(awayId, new SwissTournamentTableRow(awayId, game.getAwayTeam().getName()));
            }
        });
    }

    protected TournamentTable<TournamentTableRow> createTournamentTable(Collection<? extends TournamentTableRow> rows) {
        var table = new TournamentTable<>();
        table.getRows().addAll(rows.stream().sorted().collect(Collectors.toList()));
        return table;
    }

    public ISystemStateService<T> getStateService() {
        return stateService;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public T getSystemState() {
        return stateService.getSystemStateByTournamentId(tournament.getId());
    }
}
