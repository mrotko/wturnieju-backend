package pl.wturnieju.controller;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentParticipantDTO;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.service.ITournamentParticipantService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournaments")
public class TournamentController {

    private final ITournamentService tournamentService;

    private final ITournamentParticipantService participantService;

    private final IUserService userService;

    @GetMapping()
    public UserTournamentsDTO getAllUserTournaments(@RequestParam("userId") String userId) {
        UserTournamentsDTO dto = new UserTournamentsDTO();

        dto.setTournaments(tournamentService.getAllUserTournamentsGroupedByStatus(userId));

        return dto;
    }

    @GetMapping("/{tournamentId}")
    public Tournament getTournament(@PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getById(tournamentId).orElse(null);
    }

    @GetMapping("/{tournamentId}/participants")
    public List<TournamentParticipantDTO> getTournamentParticipants(@PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getById(tournamentId).map(tournament -> tournament.getParticipants().stream()
                .map(participant -> {
                    TournamentParticipantDTO dto = new TournamentParticipantDTO();

                    dto.setId(participant.getId());
                    dto.setStatus(participant.getParticipantStatus());

                    userService.getById(participant.getId()).ifPresent(user -> {
                        dto.setName(user.getName());
                        dto.setEmail(user.getUsername());
                        dto.setFullName(user.getFullName());
                    });
                    return dto;
                }).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @PostMapping("/{tournamentId}/invite/{participantId}")
    public void inviteParticipant(@PathVariable("tournamentId") @NonNull String tournamentId,
            @PathVariable("participantId") @NonNull String participantId) {
        participantService.invite(tournamentId, participantId);
    }
}
