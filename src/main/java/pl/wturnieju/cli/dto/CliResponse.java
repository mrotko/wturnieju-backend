package pl.wturnieju.cli.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CliResponse {
    @JsonInclude(Include.NON_NULL)
    private List<String> errorMessages;
}
