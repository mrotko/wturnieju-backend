package pl.wturnieju.controller;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentDTO;
import pl.wturnieju.dto.TournamentParticipantDTO;
import pl.wturnieju.dto.TournamentTableDTO;
import pl.wturnieju.dto.mapping.TournamentMapping;
import pl.wturnieju.dto.mapping.TournamentParticipantMapping;
import pl.wturnieju.dto.mapping.TournamentTableMapping;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.generic.ChessBundleResult;
import pl.wturnieju.model.generic.ResultBundleUpdateContent;
import pl.wturnieju.model.generic.SwissFixtureUpdateBundle;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.service.GenericTournamentUpdateBundle;
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
        var dto = new UserTournamentsDTO();
        dto.setTournaments(new HashMap<>());

        tournamentService.getAllUserTournamentsGroupedByStatus(userId).forEach((status, tournaments) ->
                dto.getTournaments().put(status, tournaments.stream()
                        .map(tournament -> TournamentMapping.map(userService, tournament))
                        .collect(Collectors.toList())));
        return dto;
    }

    @GetMapping("/{tournamentId}/table")
    public TournamentTableDTO getTournamentTable(@PathVariable("tournamentId") String tournamentId) {
        return TournamentTableMapping.map(tournamentService.getTournamentTable(tournamentId));
    }

    @PutMapping("/{tournamentId}")
    public TournamentDTO updateTournament(@PathVariable("tournamentId") String tournamentId,
            @RequestBody GenericTournamentUpdateBundle bundle) {
        tournamentService.updateTournament(bundle);
        return TournamentMapping.map(userService, tournamentService.getById(tournamentId).orElse(null));
    }

    //    @PutMapping("/{tournamentId}/fixtures/{fixtureId}")
    //    public Fixture updateFixtureResult(@PathVariable("tournamentId") String tournamentId,
    //            @PathVariable("fixtureId") String fixtureId,
    //            @RequestBody GenericFixtureUpdateBundle bundle) {
    //        tournamentService.updateFixtureResult(bundle);
    //        return tournamentService.getFixtureById(tournamentId, fixtureId);
    //    }

    @PutMapping("/{tournamentId}/fixture/{fixtureId}/result")
    public Fixture updateFixtureResult(@PathVariable("tournamentId") String tournamentId,
            @PathVariable("fixtureId") String fixtureId,
            @RequestBody MutablePair<Double, Double> result) {
        // TODO(mr): 01.12.2018 impl factory

        var bundle = new SwissFixtureUpdateBundle();
        bundle.setChangedById(userService.getCurrentUser().getId());
        bundle.setFixtureId(fixtureId);
        bundle.setTimestamp(Timestamp.now());
        bundle.setTournamentId(tournamentId);
        bundle.setType(tournamentService.getById(tournamentId).map(Tournament::getSystemType).orElse(null));

        {
            var content = new ResultBundleUpdateContent<ChessBundleResult>();

            {
                var newResult = new ChessBundleResult();
                newResult.setResult(result);

                var oldResult = new ChessBundleResult();
                oldResult.setResult(tournamentService.getFixtureById(tournamentId, fixtureId).getResult());

                content.setNewResult(newResult);
                content.setOldResult(oldResult);
            }

            bundle.setContent(content);
        }

        tournamentService.updateFixture(bundle);
        return tournamentService.getFixtureById(tournamentId, fixtureId);
    }

    @GetMapping("/{tournamentId}/fixtures")
    public List<Fixture> getFixtures(@PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getFixtures(tournamentId);
    }

    @GetMapping("/{tournamentId}/roundToFixtures")
    public Map<Integer, List<Fixture>> getRoundToFixtures(@PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getFixtures(tournamentId).stream()
                .collect(Collectors.groupingBy(Fixture::getRound, Collectors.toList()));
    }


    @GetMapping("/{tournamentId}/fixtures/current")
    public List<Fixture> getCurrentFixtures(@PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getCurrentFixtures(tournamentId);
    }

    @GetMapping("/{tournamentId}")
    public TournamentDTO getTournament(@PathVariable("tournamentId") String tournamentId) {
        return TournamentMapping.map(userService, tournamentService.getById(tournamentId).orElse(null));
    }

    @GetMapping("/{tournamentId}/participants")
    public List<TournamentParticipantDTO> getTournamentParticipants(@PathVariable("tournamentId") String tournamentId) {
        return tournamentService.getById(tournamentId).map(tournament -> tournament.getParticipants().stream()
                .map(participant ->
                        TournamentParticipantMapping.map(userService.getById(participant.getId()).orElse(null),
                                participant)
                ).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @PostMapping("/{tournamentId}/invite/{participantId}")
    public void inviteParticipant(@PathVariable("tournamentId") @NonNull String tournamentId,
            @PathVariable("participantId") @NonNull String participantId) {
        participantService.invite(tournamentId, participantId);
    }

    @GetMapping("/{tournamentId}/prepareNextRound")
    public List<Fixture> prepareNextRound(@PathVariable("tournamentId") @NonNull String tournamentId) {
        return tournamentService.prepareNextRound(tournamentId);
    }

    @PostMapping("/{tournamentId}/prepareNextRound")
    public void addNextRound(@PathVariable("tournamentId") String tournamentId,
            @NonNull @RequestBody List<Fixture> fixtures) {
        tournamentService.addNextRoundFixtures(tournamentId, fixtures);
    }
}
