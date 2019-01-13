package pl.wturnieju.controller.dto.schedule;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import pl.wturnieju.gamefixture.GameFixture;

@Mapper(componentModel = "spring",
        uses = {ScheduleElementDtoMapper.class}
)
public interface ScheduleDtoMapper {

    @Mappings({
            @Mapping(source = "tournamentId", target = "tournamentId"),
            @Mapping(source = "round", target = "round"),
            @Mapping(source = "gameFixtures", target = "elements")
    })
    ScheduleDto toScheduleDto(
            String tournamentId,
            Integer round,
            List<GameFixture> gameFixtures
    );
}
