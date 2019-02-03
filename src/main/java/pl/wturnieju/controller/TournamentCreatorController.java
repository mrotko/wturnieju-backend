package pl.wturnieju.controller;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.config.tournament.creator.TournamentCreatorConfiguration;
import pl.wturnieju.config.tournament.scoring.ScoringConfiguration;
import pl.wturnieju.controller.dto.tournament.creator.TournamentTemplateDto;
import pl.wturnieju.controller.dto.tournament.creator.TournamentTemplateMapper;
import pl.wturnieju.model.verification.TournamentParticipationRequestVerificationData;
import pl.wturnieju.model.verification.TournamentParticipationRequestVerificationToken;
import pl.wturnieju.service.ITournamentCreatorService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IVerificationService;
import pl.wturnieju.tournament.Tournament;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tournamentCreator")
public class TournamentCreatorController {

    @Qualifier("tournamentParticipationRequestVerificationService")
    private final IVerificationService<TournamentParticipationRequestVerificationToken> tournamentParticipationRequestVerificationService;

    private final ITournamentCreatorService tournamentCreatorService;

    private final ITournamentService tournamentService;

    private final ScoringConfiguration scoringConfiguration;

    private final TournamentCreatorConfiguration tournamentCreatorConfiguration;

    private final TournamentTemplateMapper tournamentTemplateMapper;

    @PostMapping(value = "/create")
    public Map<String, String> createTournament(@RequestBody TournamentTemplateDto dto) {
        var tournament = tournamentTemplateMapper.mapToTournament(dto);
        tournamentCreatorService.create(tournament);
        if (Optional.ofNullable(dto.getInvitationLink()).orElse(false)) {
            createInvitationToken(tournament);
        }

        return Collections.singletonMap("tournamentId", tournament.getId());
    }

    private void createInvitationToken(Tournament tournament) {
        var verificationData = new TournamentParticipationRequestVerificationData();
        verificationData.setTournamentId(tournament.getId());
        var token = tournamentParticipationRequestVerificationService.createVerificationToken(verificationData);
        tournament.setInvitationToken(token.getToken());
        tournamentService.updateTournament(tournament);
    }

    @GetMapping("/config")
    public Map<String, Object> getTournamentConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put("creator", tournamentCreatorConfiguration.getTournamentCreatorConfigurationData());
        config.put("scoring", scoringConfiguration.getCompetitions());

        return config;

    }
}
