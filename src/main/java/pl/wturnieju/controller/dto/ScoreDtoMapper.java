package pl.wturnieju.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import pl.wturnieju.gamefixture.Score;

@Mapper(componentModel = "spring")
public interface ScoreDtoMapper {

    ScoreDto scoreToScoreDto(Score source);

    Score scoreDtoToScore(ScoreDto source);

    Score updateScore(ScoreDto dto, @MappingTarget Score score);
}
