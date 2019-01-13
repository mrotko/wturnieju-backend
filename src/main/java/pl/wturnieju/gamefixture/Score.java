package pl.wturnieju.gamefixture;

import java.util.Map;

import lombok.Data;

@Data
public class Score {

    private Double current;

    private Map<Long, Double> periods;
}
