package pl.wturnieju.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.model.verification.VerificationToken;

@Repository
public interface TokenVerificationRepository<T extends VerificationToken> extends MongoRepository<T, String> {

    Optional<T> findByToken(String token);

    void deleteByToken(String token);
}
