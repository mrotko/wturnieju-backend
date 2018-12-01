package pl.wturnieju.dto;

import java.time.LocalDateTime;

import org.apache.commons.lang3.tuple.Pair;

import lombok.Data;
import pl.wturnieju.model.FixtureStatus;
import pl.wturnieju.model.IProfile;

@Data
public class FixtureDTO {

    private LocalDateTime timestamp;

    private Pair<UserDTO, IProfile> players;

    private Pair<Double, Double> points;

    private FixtureStatus status;
}
