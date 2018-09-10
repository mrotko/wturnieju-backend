package pl.wturnieju.model.generic;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import lombok.Data;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.DuelBuilder;
import pl.wturnieju.model.DuelBuilderFactory;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.PlayerProfile;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.TournamentSystemType;

@Data
public abstract class Tournament extends Persistent {

    protected String name;

    protected String description;

    protected String place;

    protected String img;

    protected TournamentStatus status;

    protected List<? extends GenericProfile> participant;

    protected PlayerProfile owner;

    protected Date startDate;

    protected Date endDate;

    protected TournamentSystemType systemType;

    protected CompetitionType competitionType;

    protected TournamentParticipantType tournamentParticipantType;

    protected List<String> staffIds;

    protected List<String> contributorsIds;

    protected int minParticipants;

    protected int expectedParticipants;

    @Transient
    public DuelBuilder getDuelBuilder() {
        return DuelBuilderFactory.getInstance(competitionType);
    }
}
