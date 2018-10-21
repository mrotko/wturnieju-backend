package pl.wturnieju.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Data;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.generic.Tournament;


@Data
@JsonTypeInfo(use = Id.NAME, property = "competitionType")
@JsonSubTypes({
        @Type(value = ChessTournamentTemplateDto.class, name = "CHESS")})
public class TournamentTemplateDto<T extends Tournament> implements EntityMapping<T> {

    private String name;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String place;

    private String description;

    private int maxParticipants;

    private int minParticipants;

    private CompetitionType competitionType;

    private AccessOption accessOption;

    @Override
    public void assignFields(T entity) {
        entity.setName(name);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setPlace(place);
        entity.setDescription(description);
        entity.setMaxParticipants(maxParticipants);
        entity.setMinParticipants(minParticipants);
        entity.setAccessOption(accessOption);
    }
}
