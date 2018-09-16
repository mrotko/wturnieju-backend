package pl.wturnieju.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.TournamentCreatorService;

@RestController
@RequestMapping("/create")
@RequiredArgsConstructor
public class TournamentCreatorController {

    private final TournamentCreatorService tournamentCreatorService;



    //    @PostMapping
    //    public
    //
}
