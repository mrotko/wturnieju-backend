package pl.wturnieju.controller.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {

    private String id;

    private String name;

    private List<String> membersIds;
}
