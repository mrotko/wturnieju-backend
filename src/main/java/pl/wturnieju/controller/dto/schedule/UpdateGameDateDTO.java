package pl.wturnieju.controller.dto.schedule;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class UpdateGameDateDTO {

    private String gameId;

    private Timestamp startDate;

    private Timestamp endDate;

    private Boolean shortDate;
}
