package pl.wturnieju.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentParticipantDTO;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.User;
import pl.wturnieju.service.GenericTournamentUpdateBundle;
import pl.wturnieju.service.ITournamentParticipantService;
import pl.wturnieju.service.ITournamentService;
import pl.wturnieju.service.IUserService;


// TODO(mr): 15.11.2018 co powinno działać na dashboard
/*
 * rozpoznawanie uprawnien użytkowników, kto może modyfikować, a kto tylko czytać
 * wprowadzanie wyników
 * pokazanie terminarza
 * pokazywanie tabeli
 * start/koniec turnieju
 *
 * dzięki temu działać będzie podstawowa funkcja - prowadzenie turnieju
 * w zależności od dostępnego czasu dodawane będą kolejne funkcje jak
 * dodawanie użytkowników do pomocy przy turnieju
 * itd. reszta w evernote
 *
 * */

@RequiredArgsConstructor
@Controller
@RequestMapping("/dashboard/{tournamentId}")
public class TournamentDashboardController {

    private final ITournamentService tournamentService;

    private final ITournamentParticipantService participantService;

    private final IUserService userService;

    @GetMapping("/participants")
    public List<TournamentParticipantDTO> getAllParticipants(@PathVariable String tournamentId) {
        return participantService.getAll(tournamentId).stream()
                .map(this::mapParticipantToDto)
                .collect(Collectors.toList());

    }

    private TournamentParticipantDTO mapParticipantToDto(TournamentParticipant participant) {
        Optional<User> user = userService.getById(participant.getId());

        TournamentParticipantDTO dto = new TournamentParticipantDTO();

        dto.setId(participant.getId());
        dto.setStatus(participant.getParticipantStatus());

        user.ifPresent(u -> {
            dto.setName(u.getName());
            dto.setSurname(u.getSurname());
            dto.setEmail(u.getUsername());
        });

        return dto;
    }

    @GetMapping("/participants/{userId}")
    public TournamentParticipantDTO getParticipant(@PathVariable String tournamentId, @PathVariable String userId) {
        return participantService.getById(tournamentId, userId)
                .map(this::mapParticipantToDto)
                .orElse(null);
    }

    @PostMapping("/invite/{userId}")
    public void invite(@PathVariable String tournamentId, @PathVariable String userId) {
        participantService.invite(tournamentId, userId);
    }

    @PostMapping("/disqualify/{userId}")
    public void disqualify(@PathVariable String tournamentId, @PathVariable String userId) {
        participantService.doDisqualify(tournamentId, userId, null);
    }

    @PostMapping("/resign")
    public void resign(@PathVariable String tournamentId) {
        participantService.doResign(tournamentId, userService.getCurrentUser().getId());
    }

    @PostMapping("/update")
    public void updateTournament(@RequestBody GenericTournamentUpdateBundle bundle) {
        
    }

    //    void updateTournament(Tournament tournament);
    //
    //    void updateTournament(GenericTournamentUpdateBundle bundle);
    //
    //    void updateFixture(GenericFixtureUpdateBundle bundle);
    //
    //    Optional<Tournament> getById(String tournamentId);

    // TODO(mr): 15.11.2018 void confirm(String tournamentId, String participantId);
}
