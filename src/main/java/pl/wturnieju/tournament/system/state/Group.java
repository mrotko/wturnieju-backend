package pl.wturnieju.tournament.system.state;

import java.util.List;

import lombok.Data;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Timestamp;

@Data
public class Group {

    private String id;

    private Timestamp createdAt = Timestamp.now();

    private String name;

    private List<String> participants;

    private List<GameFixture> games;

    private SystemGroupType systemGroupType;
}
