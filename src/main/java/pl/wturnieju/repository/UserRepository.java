package pl.wturnieju.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import pl.wturnieju.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{username: {$regex: ?0, $options: 'i'}}")
    Optional<User> findByUsername(String username);

    List<User> findAllByUsername(String username);
}
