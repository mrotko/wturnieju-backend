package pl.wturnieju.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Data;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.generic.Tournament;


@Data
@JsonTypeInfo(use = Id.NAME, property = "competition")
@JsonSubTypes({
        @Type(value = ChessTournamentTemplateDto.class, name = "COMPETITION_TYPE.CHESS")})
public class TournamentTemplateDto<T extends Tournament> implements EntityMapping<T> {

    private String name;

    private Timestamp fromDate;

    private Timestamp toDate;

    private String place;

    private String description;

    private int maxParticipants;

    private int minParticipants;

    private CompetitionType competition;

    private AccessOption accessOption;

    private Boolean invitationLink;

    @Override
    public void assignFields(T entity) {
        entity.setName(name);
        entity.setStartDate(fromDate);
        entity.setEndDate(toDate);
        entity.setPlace(place);
        entity.setDescription(description);
        entity.setMaxParticipants(maxParticipants);
        entity.setMinParticipants(minParticipants);
        entity.setAccessOption(accessOption);
    }
}
