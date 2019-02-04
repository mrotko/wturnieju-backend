package pl.wturnieju.controller.dto.tournament.gamefixture;

import java.util.List;

import org.mapstruct.Mapper;

import pl.wturnieju.controller.dto.tournament.ParticipantDtoMapper;
import pl.wturnieju.gamefixture.GameFixture;

@Mapper(componentModel = "spring", uses = {ScoreDtoMapper.class, ParticipantDtoMapper.class})
public interface GameFixtureDtoMapper {

    GameFixtureDto gameFixtureToGameFixtureDto(GameFixture gameFixture);

    List<GameFixtureDto> gameFixtureToGameFixtureDtos(List<GameFixture> gameFixtures);
}
