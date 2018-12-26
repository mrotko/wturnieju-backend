package pl.wturnieju.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.wturnieju.model.VerificationToken;

@Repository
public interface TokenVerificationRepository extends MongoRepository<VerificationToken, String> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByToken(String token);
}
