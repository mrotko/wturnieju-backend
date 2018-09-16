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
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.repository.TournamentRepository;

@RequiredArgsConstructor
@Service
public class TournamentCreatorService implements ITournamentCreatorService {

    private final TournamentRepository tournamentRepository;

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
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();
        return user.getId();
    }

    @Override
    public Tournament create(TournamentTemplateDto tournamentTemplateDto) {
        removeFromCache();
        var tournament = TournamentFactory.getTournament(tournamentTemplateDto.getCompetitionType());
        tournamentTemplateDto.assignFields(tournament);
        if (tournamentTemplateDto.getStep() == 1) {
            throw new IllegalArgumentException("Cannot create tournament without required fields");
        }
        return tournamentRepository.save(tournament);
    }

    public void removeFromCache() {
        userIdToTournamentTemplateDtoMap.remove(getCurrentUserId());
    }
}
