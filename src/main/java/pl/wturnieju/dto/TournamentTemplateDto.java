package pl.wturnieju.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Data;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentSystemType;
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

    private int expectedParticipants;

    private int minParticipants;

    private List<String> contributorsIds = new ArrayList<>();

    private List<String> staffIds = new ArrayList<>();

    private CompetitionType competitionType;

    private TournamentSystemType tournamentSystemType;

    private TournamentParticipantType tournamentParticipantType;

    @Override
    public void assignFields(T entity) {
        entity.setName(name);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setPlace(place);
        entity.setDescription(description);
        entity.setExpectedParticipants(expectedParticipants);
        entity.setMinParticipants(minParticipants);
        entity.setContributorsIds(contributorsIds);
        entity.setStaffIds(staffIds);
        entity.setSystemType(tournamentSystemType);
        entity.setTournamentParticipantType(tournamentParticipantType);
    }
}
