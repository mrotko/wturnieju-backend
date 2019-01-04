package pl.wturnieju.cli.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SettingsInfoResponse extends CliResponse {

    @JsonInclude(Include.NON_NULL)
    private String id;

    @JsonInclude(Include.NON_NULL)
    private String email;

    @JsonInclude(Include.NON_NULL)
    private String name;

    @JsonInclude(Include.NON_NULL)
    private String surname;

    @JsonInclude(Include.NON_NULL)
    private String fullName;

    @JsonInclude(Include.NON_NULL)
    private Boolean passwordChanged;
}
