package pl.wturnieju.cli.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class TournamentInfoResponse extends CliResponse {

    private String tournamentId;

    @JsonInclude(Include.NON_NULL)
    private String status;

    @JsonInclude(Include.NON_NULL)
    private Timestamp startDate;

    @JsonInclude(Include.NON_NULL)
    private Timestamp endDate;

    @JsonInclude(Include.NON_NULL)
    private String tournamentName;

    @JsonInclude(Include.NON_NULL)
    private String systemName;

    @JsonInclude(Include.NON_NULL)
    private String competitionName;
}
