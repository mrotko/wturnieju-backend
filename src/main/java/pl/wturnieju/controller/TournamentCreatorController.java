package pl.wturnieju.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentConfigDTO;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.service.ITournamentCreatorService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tournamentCreator")

public class TournamentCreatorController {

    private final ITournamentCreatorService tournamentCreatorService;

    @PostMapping("/create")
    public void createTournament(@RequestBody TournamentTemplateDto dto) {
        tournamentCreatorService.create(dto);
    }

    @GetMapping("/config")
    public TournamentConfigDTO getTournamentConfig() {
        System.out.println(new TournamentConfigDTO());
        return new TournamentConfigDTO();
    }
}
