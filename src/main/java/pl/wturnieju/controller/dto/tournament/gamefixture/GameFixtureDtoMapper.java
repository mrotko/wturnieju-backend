package pl.wturnieju.controller.dto.tournament.gamefixture;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.controller.dto.tournament.ParticipantDto;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.tournament.Tournament;

@Mapper(componentModel = "spring", uses = {ScoreDtoMapper.class, ParticipantDto.class})
public interface GameFixtureDtoMapper {

    @Mapping(source = "gameFixture.id", target = "gameId")
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "gameFixture.startDate", target = "startDate")
    @Mapping(source = "gameFixture.endDate", target = "endDate")
    @Mapping(source = "tournament.competitionType", target = "competitionType")
    GameFixtureDto gameFixtureToGameFixtureDto(GameFixture gameFixture, Tournament tournament);

    default List<GameFixtureDto> gameFixtureToGameFixtureDtos(List<GameFixture> gameFixtures, Tournament tournament) {
        return gameFixtures.stream()
                .map(gameFixture -> gameFixtureToGameFixtureDto(gameFixture, tournament))
                .collect(Collectors.toList());
    }
}
