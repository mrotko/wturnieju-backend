package pl.wturnieju.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.service.ICurrentUserProvider;
import pl.wturnieju.service.ITournamentCreatorService;
import pl.wturnieju.tournament.Tournament;

@RequiredArgsConstructor
@Service
public class TournamentCreatorService implements ITournamentCreatorService {

    private final TournamentRepository tournamentRepository;

    private final ICurrentUserProvider currentUser;

    @Override
    public Tournament create(Tournament tournament) {
        tournament.setOwner(currentUser.getCurrentUser());
        tournament.setCurrentRound(0);
        tournament.setCurrentStageType(tournament.getStageTypes().get(0));
        return tournamentRepository.save(tournament);
    }
}
