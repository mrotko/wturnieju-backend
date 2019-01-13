package pl.wturnieju.gamefixture;

import java.util.List;

import lombok.Data;

@Data
public class Team {

    private String id;

    private String name;

    private String shortName;

    private List<String> membersIds;
}
