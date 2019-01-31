package pl.wturnieju.tournament.system.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.tournament.Participant;

public class TournamentTableGenerator {

    private Map<GameResultType, Double> resultToPointsMapping = new HashMap<>();

    private List<GameFixture> games = new ArrayList<>();

    private List<Participant> participants = new ArrayList<>();

    private Comparator<TournamentTableRow> rowComparator;

    private Map<String, TournamentTableRow> teamIdToRowMapping = new HashMap<>();

    public TournamentTable generateTable() {
        generateRows();
        calculateRowsValues();

        return createTable();
    }

    private TournamentTable createTable() {
        var table = new TournamentTable();

        var rows = getSortedRows();
        table.setRows(rows);

        return table;
    }

    private List<TournamentTableRow> getSortedRows() {
        return teamIdToRowMapping.values().stream()
                .sorted(rowComparator)
                .collect(Collectors.toList());
    }

    private void calculateRowsValues() {
        games.forEach(game -> {
            if (game.getBye()) {
                addByeGame(game);
            } else if (game.getWinner() == 0) {
                addDrawGame(game);
            } else {
                addWinGame(game);
            }
        });
    }

    private void addDrawGame(GameFixture game) {
        var homeTeamRow = teamIdToRowMapping.get(game.getHomeTeam().getId());
        var awayTeamRow = teamIdToRowMapping.get(game.getAwayTeam().getId());

        addToRowDraw(homeTeamRow);
        addToRowDraw(awayTeamRow);
    }

    private void addByeGame(GameFixture game) {
        var row = teamIdToRowMapping.get(game.getHomeTeam().getId());
        addToRowWin(row);
    }

    private void addWinGame(GameFixture game) {
        var homeTeamRow = teamIdToRowMapping.get(game.getHomeTeam().getId());
        var awayTeamRow = teamIdToRowMapping.get(game.getAwayTeam().getId());

        if (game.getWinner() == 1) {
            addToRowWin(homeTeamRow);
            addToRowLose(awayTeamRow);
        } else {
            addToRowWin(awayTeamRow);
            addToRowLose(homeTeamRow);
        }
    }

    private void addToRowWin(TournamentTableRow row) {
        row.addPoints(getPoints(GameResultType.WIN));
        row.incWins();
        row.incTotalGames();
    }

    private void addToRowDraw(TournamentTableRow row) {
        row.addPoints(getPoints(GameResultType.DRAW));
        row.incDraws();
        row.incTotalGames();
    }

    private void addToRowLose(TournamentTableRow row) {
        row.incLoses();
        row.incTotalGames();
    }

    private Double getPoints(GameResultType resultType) {
        return resultToPointsMapping.get(resultType);
    }

    private void generateRows() {
        participants.forEach(team -> {
            var row = new TournamentTableRow(team.getId(), team.getName());
            teamIdToRowMapping.put(team.getId(), row);
        });
    }

    public Map<GameResultType, Double> getResultToPointsMapping() {
        return resultToPointsMapping;
    }

    public void setResultToPointsMapping(Map<GameResultType, Double> resultToPointsMapping) {
        this.resultToPointsMapping = resultToPointsMapping;
    }

    public List<GameFixture> getGames() {
        return games;
    }

    public void setGames(List<GameFixture> games) {
        this.games = games;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public Comparator<TournamentTableRow> getRowComparator() {
        return rowComparator;
    }

    public void setRowComparator(Comparator<TournamentTableRow> rowComparator) {
        this.rowComparator = rowComparator;
    }
}
