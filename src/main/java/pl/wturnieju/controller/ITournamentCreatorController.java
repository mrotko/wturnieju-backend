package pl.wturnieju.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.wturnieju.dto.TournamentTemplateDto;

@RequestMapping("/tournamentCreator")
public interface ITournamentCreatorController {

    @PostMapping("/create")
    void createTournament(TournamentTemplateDto dto);
}
