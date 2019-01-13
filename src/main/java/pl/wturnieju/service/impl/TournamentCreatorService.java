package pl.wturnieju.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.model.TournamentFactory;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.service.ICurrentUserProvider;
import pl.wturnieju.service.ITournamentCreatorService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.TournamentStatus;

@RequiredArgsConstructor
@Service
public class TournamentCreatorService implements ITournamentCreatorService {

    private final TournamentRepository tournamentRepository;

    private final ICurrentUserProvider currentUser;

    private Map<String, TournamentTemplateDto> userIdToTournamentTemplateDtoMap = new HashMap<>();

    @Override
    public TournamentTemplateDto getUserTemplate() {
        return getTemplateFromCache().orElse(new TournamentTemplateDto());
    }

    private Optional<TournamentTemplateDto> getTemplateFromCache() {
        return Optional.ofNullable(userIdToTournamentTemplateDtoMap.getOrDefault(getCurrentUserId(), null));
    }

    @Override
    public void update(TournamentTemplateDto tournamentTemplateDto) {
        userIdToTournamentTemplateDtoMap.put(getCurrentUserId(), tournamentTemplateDto);
    }

    private String getCurrentUserId() {
        return currentUser.getCurrentUser().getId();
    }

    @Override
    public Tournament create(TournamentTemplateDto tournamentTemplateDto) {
        removeFromCache();
        var tournament = TournamentFactory.getTournament(tournamentTemplateDto.getCompetition());
        tournamentTemplateDto.assignFields(tournament);
        tournament.setStatus(TournamentStatus.BEFORE_START);
        tournament.setOwner(currentUser.getCurrentUser());
        //         TODO(mr): 01.12.2018 fix
        tournament.setPlannedRounds(tournament.getMaxParticipants() - 1);
        tournament.setCurrentRound(0);

        return tournamentRepository.save(tournament);
    }

    public void removeFromCache() {
        userIdToTournamentTemplateDtoMap.remove(getCurrentUserId());
    }
}
