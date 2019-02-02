package pl.wturnieju.tournament.system.table;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.tournament.Participant;

public class TournamentTableGeneratorBuilder {

    private Double pointsForWin = 3.;

    private Double pointsForDraw = 1.;

    private Double pointsForLose = 0.;

    private List<Participant> participants = new ArrayList<>();

    private List<? extends GameFixture> games = new ArrayList<>();

    private Comparator<TournamentTableRow> rowComparator = comparing(TournamentTableRow::getPoints).reversed();

    public static TournamentTableGeneratorBuilder builder() {
        return new TournamentTableGeneratorBuilder();
    }

    public TournamentTableGeneratorBuilder withPointsForWin(Double points) {
        pointsForWin = points;
        return this;
    }

    public TournamentTableGeneratorBuilder withPointsForDraw(Double points) {
        pointsForDraw = points;
        return this;
    }

    public TournamentTableGeneratorBuilder withPointsForLose(Double points) {
        pointsForLose = points;
        return this;
    }

    public TournamentTableGeneratorBuilder withParticipants(List<Participant> participants) {
        this.participants = participants;
        return this;
    }

    public TournamentTableGeneratorBuilder withGames(List<? extends GameFixture> games) {
        this.games = games;
        return this;
    }

    public TournamentTableGeneratorBuilder withRowComparator(Comparator<TournamentTableRow> rowComparator) {
        this.rowComparator = rowComparator;
        return this;
    }

    public TournamentTableGenerator build() {
        var tableGenerator = new TournamentTableGenerator();

        tableGenerator.setGames(games);
        tableGenerator.setParticipants(participants);
        tableGenerator.setRowComparator(rowComparator);
        tableGenerator.setResultToPointsMapping(Map.of(
                GameResultType.WIN, pointsForWin,
                GameResultType.DRAW, pointsForDraw,
                GameResultType.LOSE, pointsForLose
        ));

        return tableGenerator;
    }
}
