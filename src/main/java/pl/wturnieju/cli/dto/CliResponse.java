package pl.wturnieju.cli.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class CliResponse {
    @JsonInclude(Include.NON_NULL)
    private List<String> errorMessages;
}
