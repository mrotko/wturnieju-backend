package pl.wturnieju.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.gamefixture.GameFixture;

@Document("generatedGame")
@Getter
@Setter
public class GeneratedGameFixtureWrapper {

    private String id;

    private GameFixture gameFixture;
}
