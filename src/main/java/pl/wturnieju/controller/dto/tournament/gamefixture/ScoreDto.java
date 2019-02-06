package pl.wturnieju.controller.dto.tournament.gamefixture;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.converter.JsonListToDoubleMapDeserializer;

@Getter
@Setter
public class ScoreDto {

    private Double current;

    @JsonDeserialize(using = JsonListToDoubleMapDeserializer.class)
    private Map<Integer, Double> periods;
}
