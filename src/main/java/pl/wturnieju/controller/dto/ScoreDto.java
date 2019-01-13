package pl.wturnieju.controller.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreDto {

    private Double current;

    private Map<Integer, Double> periods;
}
