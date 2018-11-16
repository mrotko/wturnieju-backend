package pl.wturnieju.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.ITournamentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournaments")
public class TournamentController {

    private final ITournamentService tournamentService;

    @GetMapping()
    public UserTournamentsDTO getAllUserTournaments(@RequestParam("userId") String userId) {
        UserTournamentsDTO dto = new UserTournamentsDTO();

        dto.setTournaments(tournamentService.getAllUserTournamentsGroupedByStatus(userId));

        return dto;
    }
}
