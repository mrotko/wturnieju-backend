package pl.wturnieju.dto;

import java.time.LocalDateTime;

import lombok.Data;
import pl.wturnieju.model.DuelResult;
import pl.wturnieju.model.IProfile;

@Data
public class FixtureDTO {

    private LocalDateTime timestamp;

    private IProfile homePlayer;

    private IProfile awayPlayer;

    private double homePlayerPoints;

    private double awayPlayerPoints;

    private DuelResult duelResult;


}
