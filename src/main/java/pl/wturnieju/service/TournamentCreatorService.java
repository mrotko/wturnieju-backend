package pl.wturnieju.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.model.TournamentFactory;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.TournamentRepository;

@Service
@RequiredArgsConstructor
public class TournamentCreatorService {

    private final TournamentRepository tournamentRepository;

    private Map<String, TournamentTemplateDto> userIdToTournamentTemplateDtoMap = new HashMap<>();

    public TournamentTemplateDto getUserTemplate() {
        return getTemplateFromCache().orElse(new TournamentTemplateDto());
    }

    private Optional<TournamentTemplateDto> getTemplateFromCache() {
        return Optional.ofNullable(userIdToTournamentTemplateDtoMap.getOrDefault(getCurrentUserId(), null));
    }

    public void update(TournamentTemplateDto tournamentTemplateDto) {
        userIdToTournamentTemplateDtoMap.put(getCurrentUserId(), tournamentTemplateDto);
    }

    private String getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        return user.getId();
    }

    public void create(TournamentTemplateDto tournamentTemplateDto) {
        removeFromCache();
        var tournament = TournamentFactory.getTournament(tournamentTemplateDto.getCompetitionType());
        tournamentTemplateDto.assignFields(tournament);
        tournamentRepository.save(tournament);
    }

    public void removeFromCache() {
        userIdToTournamentTemplateDtoMap.remove(getCurrentUserId());
    }
}
