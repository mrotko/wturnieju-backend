package pl.wturnieju.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.controller.dto.tournament.TournamentDto;
import pl.wturnieju.controller.dto.tournament.TournamentParticipantDto;
import pl.wturnieju.controller.dto.tournament.UpdateTournamentStatusDTO;
import pl.wturnieju.controller.dto.tournament.UserTournamentsDto;
import pl.wturnieju.controller.dto.tournament.gamefixture.GameFixtureDto;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleDto;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleElementDto;
import pl.wturnieju.controller.dto.tournament.table.TournamentTableDto;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.User;
import pl.wturnieju.model.verification.TournamentInviteVerificationData;
import pl.wturnieju.model.verification.TournamentInviteVerificationToken;
import pl.wturnieju.service.ITournamentParticipantService;
import pl.wturnieju.service.ITournamentPresentationService;
import pl.wturnieju.service.ITournamentScheduleService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.service.IVerificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournaments")
public class TournamentController {

    private final ITournamentService tournamentService;

    private final ITournamentScheduleService scheduleService;

    private final ITournamentParticipantService participantService;

    private final ITournamentPresentationService tournamentPresentationService;

    private final IUserService userService;

    private final DtoMappers mappers;

    @Qualifier("tournamentInviteTokenVerificationService")
    private final IVerificationService<TournamentInviteVerificationToken> tournamentInviteVerificationService;

    @GetMapping
    public UserTournamentsDto getUserTournaments(@RequestParam("userId") String userId) {
        var userTournaments = tournamentService.getUserTournaments(userId);
        return mappers.createUserTournamentDto(userId, userTournaments);
    }

    @PutMapping("/{tournamentId}")
    public TournamentDto updateTournamentStatus(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody UpdateTournamentStatusDTO dto) {
        switch (dto.getStatus()) {
        case "START":
            tournamentService.startTournament(tournamentId);
            break;
        case "FINISH":
            tournamentService.finishTournament(tournamentId);
            break;
        default:
            throw new IllegalArgumentException("Unknown property - " + dto.getStatus());
        }
        var tournament = tournamentService.getTournament(tournamentId);
        return mappers.createTournamentDto(tournament);
    }

    @GetMapping("/{tournamentId}")
    public TournamentDto getTournament(@PathVariable("tournamentId") String tournamentId) {
        var tournament = tournamentService.getTournament(tournamentId);
        if (tournament == null) {
            throw new ResourceNotFoundException();
        }
        return mappers.createTournamentDto(tournament);
    }

    @DeleteMapping("/{tournamentId}")
    public String deleteTournament(@PathVariable("tournamentId") String tournamentId) {
        tournamentService.deleteTournament(tournamentId);
        return tournamentId;
    }

    @GetMapping("/{tournamentId}/participants")
    public List<TournamentParticipantDto> getTournamentParticipants(@PathVariable("tournamentId") String tournamentId) {
        return participantService.getAll(tournamentId).stream()
                .map(mappers::createTournamentParticipantDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{tournamentId}/participants")
    public void acceptParticipant(@PathVariable("tournamentId") String tournamentId,
            @RequestBody() String participantId) {
        participantService.acceptInvitation(tournamentId, participantId);
    }

    @DeleteMapping("/{tournamentId}/participants/{participantId}")
    public void deleteParticipant(@PathVariable("tournamentId") String tournamentId,
            @PathVariable("participantId") String participantId) {
        participantService.deleteParticipant(tournamentId, participantId);
    }

    @PostMapping("/{tournamentId}/participants")
    public List<String> inviteParticipants(@PathVariable("tournamentId") String tournamentId,
            @RequestBody List<String> participantsIds) {
        participantsIds.forEach(id -> {
            var verificationData = new TournamentInviteVerificationData();

            verificationData.setTournamentId(tournamentId);
            verificationData.setUserId(id);
            verificationData.setEmail(getUserByIdOrThrow(id).getUsername());
            tournamentInviteVerificationService.createVerificationToken(verificationData);

            participantService.invite(tournamentId, id);
        });
        return participantsIds;
    }

    private User getUserByIdOrThrow(String userId) {
        return userService.findUserById(userId).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/{tournamentId}/schedule/generate")
    public ScheduleDto generateSchedule(@PathVariable("tournamentId") String tournamentId) {
        var schedule = scheduleService.generateSchedule(tournamentId);
        var round = schedule.stream()
                .findFirst()
                .map(GameFixture::getRound).orElse(null);
        return mappers.createScheduleDto(tournamentId, round, schedule);
    }

    @PutMapping("/{tournamentId}/schedule")
    public ScheduleDto saveSchedule(@PathVariable("tournamentId") String tournamentId,
            @RequestBody ScheduleDto scheduleDTO) {

        var gamesIds = scheduleDTO.getElements().stream()
                .map(ScheduleElementDto::getGameId)
                .collect(Collectors.toList());

        var cachedGames = scheduleService.getGeneratedSchedule(tournamentId, gamesIds);
        var elements = scheduleDTO.getElements();
        elements.forEach(element -> cachedGames.stream().filter(g -> g.getId().equals(element.getGameId())).findFirst()
                .ifPresent(cachedGame -> mappers.getScheduleElementDtoMapper().updateGameFixture(element, cachedGame)));

        scheduleService.saveSchedule(tournamentId, cachedGames);

        var tournament = tournamentService.getTournament(tournamentId);
        tournament.setCurrentRound(scheduleDTO.getRound());
        tournamentService.updateTournament(tournament);

        return scheduleDTO;
    }

    @GetMapping(path = "/{tournamentId}/schedule", params = {"game_status"})
    public List<ScheduleDto> getSchedule(@PathVariable("tournamentId") String tournamentId,
            @RequestParam("game_status") String gameStatus) {

        List<GameFixture> games = new ArrayList<>();

        if (gameStatus.equals("BEFORE_START")) {
            games.addAll(scheduleService.getGameFixturesBeforeStart(tournamentId));
        } else if (gameStatus.equals("ENDED")) {
            games.addAll(scheduleService.getEndedGameFixtures(tournamentId));
        }

        var fixturesGroupedByRound = games.stream()
                .collect(Collectors.groupingBy(GameFixture::getRound, Collectors.toList()));
        return fixturesGroupedByRound.entrySet().stream()
                .map(entry -> mappers.createScheduleDto(tournamentId, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{tournamentId}/game-fixtures")
    public List<GameFixtureDto> getGameFixtures(@PathVariable("tournamentId") String tournamentId) {
        var games = scheduleService.getGameFixtures(tournamentId);
        var tournament = tournamentService.getTournament(tournamentId);
        return mappers.getGameFixtureDtoMapper().gameFixtureToGameFixtureDtos(games, tournament);
    }

    @GetMapping("/{tournamentId}/table")
    @Transactional(readOnly = true)
    public TournamentTableDto getTournamentTable(@PathVariable("tournamentId") String tournamentId) {
        var table = tournamentPresentationService.getTournamentTable(tournamentId);
        return mappers.createTournamentTableDto(tournamentId, table);
    }

    @GetMapping(value = "/", params = {"access"})
    public List<String> getTournamentsIdsByAccess(@RequestParam("access") String access) {
        if (access.equals("PUBLIC")) {
            return tournamentService.getTournamentsByAccess(AccessOption.PUBLIC).stream()
                    .map(Persistent::getId)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
