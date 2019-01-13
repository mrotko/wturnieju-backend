package pl.wturnieju.controller;


import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentConfigDTO;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.model.verification.TournamentParticipationRequestVerificationData;
import pl.wturnieju.model.verification.TournamentParticipationRequestVerificationToken;
import pl.wturnieju.service.ITournamentCreatorService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IVerificationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tournamentCreator")
public class TournamentCreatorController {

    @Qualifier("tournamentParticipationRequestVerificationService")
    private final IVerificationService<TournamentParticipationRequestVerificationToken> tournamentParticipationRequestVerificationService;

    private final ITournamentCreatorService tournamentCreatorService;

    private final ITournamentService tournamentService;

    @PostMapping(value = "/create")
    public Map<String, String> createTournament(@RequestBody TournamentTemplateDto dto) {
        var tournament = tournamentCreatorService.create(dto);
        if (Optional.ofNullable(dto.getInvitationLink()).orElse(false)) {
            var verificationData = new TournamentParticipationRequestVerificationData();
            verificationData.setTournamentId(tournament.getId());
            var token = tournamentParticipationRequestVerificationService.createVerificationToken(verificationData);
            tournament.setInvitationToken(token.getToken());
            tournamentService.updateTournament(tournament);
        }

        return Collections.singletonMap("tournamentId", tournament.getId());
    }

    @GetMapping("/config")
    public TournamentConfigDTO getTournamentConfig() {
        return new TournamentConfigDTO();
    }
}
