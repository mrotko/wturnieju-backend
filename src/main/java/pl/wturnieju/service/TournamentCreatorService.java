package pl.wturnieju.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.model.TournamentFactory;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.repository.TournamentRepository;

@RequiredArgsConstructor
@Service
public class TournamentCreatorService implements ITournamentCreatorService {

    private final TournamentRepository tournamentRepository;

    private final ICurrentUser currentUser;

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
        var tournament = TournamentFactory.getTournament(tournamentTemplateDto.getCompetitionType());
        tournamentTemplateDto.assignFields(tournament);
        return tournamentRepository.save(tournament);
    }

    public void removeFromCache() {
        userIdToTournamentTemplateDtoMap.remove(getCurrentUserId());
    }
}
