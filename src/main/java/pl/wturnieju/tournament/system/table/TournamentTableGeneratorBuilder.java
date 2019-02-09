package pl.wturnieju.tournament.system.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ComparisonChain;

import pl.wturnieju.PositionOrderElementType;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.tournament.GameResultType;
import pl.wturnieju.tournament.Participant;

public class TournamentTableGeneratorBuilder {

    private Double pointsForWin = 3.;

    private Double pointsForDraw = 1.;

    private Double pointsForLose = 0.;

    private List<Participant> participants = new ArrayList<>();

    private List<PositionOrderElementType> positionOrder = new ArrayList<>();

    private List<? extends GameFixture> games = new ArrayList<>();

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

    public TournamentTableGeneratorBuilder withPositionOrder(List<PositionOrderElementType> positionOrder) {
        this.positionOrder = positionOrder;
        return this;
    }

    public TournamentTableGenerator build() {
        var tableGenerator = new TournamentTableGenerator();

        tableGenerator.setGames(games);
        tableGenerator.setParticipants(participants);
        tableGenerator.setRowComparator(createRowComparator());
        tableGenerator.setResultToPointsMapping(Map.of(
                GameResultType.WIN, pointsForWin,
                GameResultType.DRAW, pointsForDraw,
                GameResultType.LOSE, pointsForLose
        ));

        return tableGenerator;
    }

    private Comparator<TournamentTableRow> createRowComparator() {
        return (o1, o2) -> buildComparisonChain(o1, o2).result();
    }

    private ComparisonChain buildComparisonChain(
            TournamentTableRow o1,
            TournamentTableRow o2) {
        var comparisonChain = ComparisonChain.start();
        for (PositionOrderElementType positionOrderElementType : positionOrder) {
            switch (positionOrderElementType) {
            case POINTS:
                comparisonChain = comparisonChain.compare(o2.getPoints(), o1.getPoints());
                break;
            case SMALL_POINTS:
                comparisonChain = comparisonChain.compare(o2.getSmallPoints(), o1.getSmallPoints());
                break;
            case WINS:
                comparisonChain = comparisonChain.compare(o2.getWins(), o1.getWins());
                break;
            case DRAWS:
                comparisonChain = comparisonChain.compare(o2.getDraws(), o1.getDraws());
                break;
            default:
                comparisonChain = comparisonChain.compare(o1.getName(), o2.getName());
            }
        }
        return comparisonChain;
    }
}
