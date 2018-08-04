package app.helloworld;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloWorldRepository extends MongoRepository<HelloWorldMessage, String> {

}
