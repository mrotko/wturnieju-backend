package pl.wturnieju.controller;


import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.service.ITournamentCreatorService;

@RequiredArgsConstructor
@RestController
public class TournamentCreatorController implements ITournamentCreatorController {

    private final ITournamentCreatorService tournamentCreatorService;

    @Override
    public void createTournament(TournamentTemplateDto dto) {
        tournamentCreatorService.create(dto);
    }
}
