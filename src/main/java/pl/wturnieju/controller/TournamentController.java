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
import pl.wturnieju.annotation.CheckTournamentAccess;
import pl.wturnieju.annotation.TournamentAccessLevel;
import pl.wturnieju.annotation.TournamentId;
import pl.wturnieju.controller.dto.tournament.ParticipantDto;
import pl.wturnieju.controller.dto.tournament.TournamentDto;
import pl.wturnieju.controller.dto.tournament.UpdateTournamentStatusDTO;
import pl.wturnieju.controller.dto.tournament.UserTournamentsDto;
import pl.wturnieju.controller.dto.tournament.gamefixture.GameFixtureDto;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleDto;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleElementDto;
import pl.wturnieju.controller.dto.tournament.table.TournamentTableDto;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.helper.ITournamentHelper;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.User;
import pl.wturnieju.model.verification.TournamentInviteVerificationData;
import pl.wturnieju.model.verification.TournamentInviteVerificationToken;
import pl.wturnieju.service.IParticipantService;
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

    private final IParticipantService participantService;

    private final ITournamentPresentationService tournamentPresentationService;

    private final ITournamentHelper tournamentHelper;

    private final IUserService userService;

    private final DtoMappers mappers;

    @Qualifier("tournamentInviteTokenVerificationService")
    private final IVerificationService<TournamentInviteVerificationToken> tournamentInviteVerificationService;

    @GetMapping
    public UserTournamentsDto getUserTournaments(@RequestParam("userId") String userId) {
        var userTournaments = tournamentHelper.getUserTournaments(userId);
        return mappers.createUserTournamentDto(userId, userTournaments);
    }

    @PutMapping("/{tournamentId}")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.OWNER)
    public TournamentDto updateTournamentStatus(
            @TournamentId @PathVariable("tournamentId") String tournamentId,
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
        var tournament = tournamentService.getById(tournamentId);
        return mappers.createTournamentDto(tournament);
    }

    @GetMapping("/{tournamentId}")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.USER)
    public TournamentDto getTournament(@TournamentId @PathVariable("tournamentId") String tournamentId) {
        var tournament = tournamentService.getById(tournamentId);
        if (tournament == null) {
            throw new ResourceNotFoundException();
        }
        return mappers.createTournamentDto(tournament);
    }

    @DeleteMapping("/{tournamentId}")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.OWNER)
    public String deleteTournament(@TournamentId @PathVariable("tournamentId") String tournamentId) {
        tournamentService.deleteTournament(tournamentId);
        return tournamentId;
    }

    @GetMapping("/{tournamentId}/participants")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.USER)
    public List<ParticipantDto> getTournamentParticipants(@TournamentId @PathVariable("tournamentId") String tournamentId) {
        return participantService.getAllByTournamentId(tournamentId).stream()
                .map(mappers::createTournamentParticipantDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{tournamentId}/participants")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.OWNER)
    public void acceptParticipant(@TournamentId @PathVariable("tournamentId") String tournamentId,
            @RequestBody() String participantId) {
        participantService.acceptInvitation(participantId);
    }

    @DeleteMapping("/{tournamentId}/participants/{participantId}")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.OWNER)
    public void deleteParticipant(@TournamentId @PathVariable("tournamentId") String tournamentId,
            @PathVariable("participantId") String participantId) {
        participantService.deleteParticipant(participantId);
    }

    @PostMapping("/{tournamentId}/participants")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.OWNER)
    public List<String> inviteParticipants(@TournamentId @PathVariable("tournamentId") String tournamentId,
            @RequestBody List<String> userIds) {
        userIds.forEach(userId -> {
            var participant = participantService.inviteUserId(tournamentId, userId);

            var verificationData = new TournamentInviteVerificationData();

            verificationData.setTournamentId(tournamentId);
            verificationData.setParticipantId(participant.getId());
            verificationData.setEmail(getUserByIdOrThrow(userId).getUsername());
            tournamentInviteVerificationService.createVerificationToken(verificationData);
        });
        return userIds;
    }

    private User getUserByIdOrThrow(String userId) {
        return userService.findUserById(userId).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/{tournamentId}/schedule/generate/{groupId}")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.OWNER)
    public ScheduleDto generateSchedule(@TournamentId @PathVariable("tournamentId") String tournamentId,
            @PathVariable("groupId") String groupId) {
        var schedule = scheduleService.generateSchedule(tournamentId, groupId);
        var round = schedule.stream()
                .findFirst()
                .map(GameFixture::getRound).orElse(null);
        return mappers.createScheduleDto(tournamentId, round, schedule);
    }

    @PutMapping("/{tournamentId}/schedule")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.OWNER)
    public ScheduleDto saveSchedule(@TournamentId @PathVariable("tournamentId") String tournamentId,
            @RequestBody ScheduleDto scheduleDTO) {

        var gamesIds = scheduleDTO.getElements().stream()
                .map(ScheduleElementDto::getGameId)
                .collect(Collectors.toList());

        if (gamesIds.isEmpty()) {
            return scheduleDTO;
        }

        var cachedGames = scheduleService.getGeneratedSchedule(tournamentId, gamesIds);
        var elements = scheduleDTO.getElements();
        elements.forEach(element -> cachedGames.stream().filter(g -> g.getId().equals(element.getGameId())).findFirst()
                .ifPresent(cachedGame -> mappers.getScheduleElementDtoMapper().updateGameFixture(element, cachedGame)));

        scheduleService.saveSchedule(tournamentId, cachedGames);

        var tournament = tournamentService.getById(tournamentId);
        tournament.setCurrentRound(scheduleDTO.getRound());
        tournamentService.updateTournament(tournament);

        return scheduleDTO;
    }

    @GetMapping(path = "/{tournamentId}/schedule", params = {"game_status"})
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.USER)
    public List<ScheduleDto> getSchedule(
            @TournamentId @PathVariable("tournamentId") String tournamentId,
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
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.USER)
    public List<GameFixtureDto> getGameFixtures(@TournamentId @PathVariable("tournamentId") String tournamentId) {
        var games = scheduleService.getAllGameFixtures(tournamentId);
        return mappers.getGameFixtureDtoMapper().gameFixtureToGameFixtureDtos(games);
    }

    @GetMapping("/{tournamentId}/table/{groupId}")
    @Transactional(readOnly = true)
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.USER)
    public TournamentTableDto getTournamentTable(
            @TournamentId @PathVariable("tournamentId") String tournamentId,
            @PathVariable("groupId") String groupId
    ) {
        var table = tournamentPresentationService.retrieveTournamentTable(tournamentId, groupId);
        return mappers.createTournamentTableDto(table);
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

    @PostMapping(value = "/{tournamentId}/users/{userId}/join")
    @CheckTournamentAccess(accessLevel = TournamentAccessLevel.USER)
    public void userWantJoinToTournament(
            @TournamentId @PathVariable("tournamentId") String tournamentId,
            @PathVariable("userId") String userId) {
        participantService.requestParticipation(tournamentId, userId);
    }
}
