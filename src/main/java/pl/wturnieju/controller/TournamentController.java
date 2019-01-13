package pl.wturnieju.controller;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
import pl.wturnieju.controller.dto.tournament.UpdateTournamentStatusDTO;
import pl.wturnieju.controller.dto.tournament.UserTournamentsDTO;
import pl.wturnieju.controller.dto.tournament.UserTournamentsDTOBuilder;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleDto;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleDtoMapper;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleElementDto;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleElementDtoMapper;
import pl.wturnieju.controller.dto.tournament.table.TournamentTableDto;
import pl.wturnieju.controller.dto.tournament.table.TournamentTableDtoMapper;
import pl.wturnieju.dto.TournamentDTO;
import pl.wturnieju.dto.TournamentParticipantDTO;
import pl.wturnieju.dto.mapping.TournamentDTOMapping;
import pl.wturnieju.dto.mapping.TournamentParticipantMapping;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.gamefixture.GameFixture;
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

    private final ScheduleDtoMapper scheduleDtoMapper;

    private final ScheduleElementDtoMapper scheduleElementDtoMapper;

    private final TournamentTableDtoMapper tournamentTableDtoMapper;

    @Qualifier("tournamentInviteTokenVerificationService")
    private final IVerificationService<TournamentInviteVerificationToken> tournamentInviteVerificationService;

    @GetMapping
    public UserTournamentsDTO getUserTournaments(@RequestParam("userId") String userId) {
        var userTournaments = tournamentService.getUserTournaments(userId);
        return new UserTournamentsDTOBuilder()
                .userId(userId)
                .tournaments(userService, userTournaments)
                .build();
    }

    @PostMapping("/{tournamentId}")
    public UpdateTournamentStatusDTO updateTournamentStatus(
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
        return dto;
    }

    @GetMapping("/{tournamentId}")
    public TournamentDTO getTournament(@PathVariable("tournamentId") String tournamentId) {
        var tournament = tournamentService.getTournament(tournamentId);
        if (tournament == null) {
            throw new ResourceNotFoundException();
        }
        return TournamentDTOMapping.map(userService, tournament);
    }

    @DeleteMapping("/{tournamentId}")
    public String deleteTournament(@PathVariable("tournamentId") String tournamentId) {
        tournamentService.deleteTournament(tournamentId);
        return tournamentId;
    }

    @GetMapping("/{tournamentId}/participants")
    public List<TournamentParticipantDTO> getTournamentParticipants(@PathVariable("tournamentId") String tournamentId) {
        return tournamentService.findTournament(tournamentId).map(tournament -> tournament.getParticipants().stream()
                .map(participant ->
                        TournamentParticipantMapping.map(userService.getById(participant.getId()).orElse(null),
                                participant)
                ).collect(Collectors.toList())).orElse(Collections.emptyList());
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
            verificationData.setEmail(getUserById(id).getUsername());
            tournamentInviteVerificationService.createVerificationToken(verificationData);

            participantService.invite(tournamentId, id);
        });
        return participantsIds;
    }

    private User getUserById(String userId) {
        return userService.getById(userId).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/{tournamentId}/schedule")
    public ScheduleDto generateSchedule(@PathVariable("tournamentId") String tournamentId) {
        var schedule = scheduleService.generateSchedule(tournamentId);
        var round = schedule.stream()
                .findFirst()
                .map(GameFixture::getRound).orElse(null);
        return scheduleDtoMapper.toScheduleDto(
                tournamentId,
                round,
                schedule);
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
                .ifPresent(cachedGame -> scheduleElementDtoMapper.updateGameFixture(element, cachedGame)));

        scheduleService.saveSchedule(tournamentId, cachedGames);

        var tournament = tournamentService.getTournament(tournamentId);
        tournament.setCurrentRound(scheduleDTO.getRound());
        tournamentService.updateTournament(tournament);

        return scheduleDTO;
    }

    @GetMapping("/{tournamentId}/table")
    public TournamentTableDto getTournamentTable(@PathVariable("tournamentId") String tournamentId) {
        var table = tournamentPresentationService.getTournamentTable(tournamentId);
        return tournamentTableDtoMapper.tournamentTableToTournamentTableDto(tournamentId, table);
    }
}
