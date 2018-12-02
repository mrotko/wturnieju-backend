package pl.wturnieju.model.chess;

import org.apache.commons.lang3.tuple.MutablePair;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import pl.wturnieju.JsonPairDeserializer;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Fixture;

@Data
public class ChessFixture extends Fixture {

    //    private List<ChessMove> moves = new ArrayList<>();

    //    private String currentPlayerIdMove;

    //    private Map<String, Integer> playerIdToTimeLeftMap = new HashMap<>();

    //    private int timeBonus;


    @JsonCreator
    public ChessFixture(
            @JsonProperty("playersIds")
            @JsonDeserialize(using = JsonPairDeserializer.class)
                    MutablePair<String, String> playersIds) {
        super(playersIds);
        competitionType = CompetitionType.CHESS;
    }
}
