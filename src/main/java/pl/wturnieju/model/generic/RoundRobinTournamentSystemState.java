package pl.wturnieju.model.generic;

public class RoundRobinTournamentSystemState extends TournamentSystemState {

    @Override
    public RoundRobinTournamentTable getTournamentTable() {
        return (RoundRobinTournamentTable) super.getTournamentTable();
    }
}
