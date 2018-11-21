package pl.wturnieju.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TournamentTableDTO {

    private List<TournamentTableRowDTO> rows = new ArrayList<>();
}
