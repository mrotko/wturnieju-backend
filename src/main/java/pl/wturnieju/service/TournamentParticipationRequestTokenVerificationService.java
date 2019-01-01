package pl.wturnieju.service;

import org.springframework.stereotype.Service;

import pl.wturnieju.model.TournamentParticipationRequestVerificationToken;
import pl.wturnieju.repository.TokenVerificationRepository;

@Service
public class TournamentParticipationRequestTokenVerificationService extends TokenVerificationService<TournamentParticipationRequestVerificationToken> {

    public TournamentParticipationRequestTokenVerificationService(TokenVerificationRepository tokenVerificationRepository) {
        super(tokenVerificationRepository, null);
    }

    @Override
    public TournamentParticipationRequestVerificationToken createVerificationToken(VerificationData verificationData) {
        var data = (TournamentParticipationRequestVerificationData) verificationData;

        var token = new TournamentParticipationRequestVerificationToken();
        token.setTournamentId(data.getTournamentId());
        setExpiryDateAndGenerateToken(token);

        return tokenVerificationRepository.save(token);
    }
}
