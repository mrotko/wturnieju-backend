package pl.wturnieju.model.generic;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Transient;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.DuelBuilder;
import pl.wturnieju.model.DuelBuilderFactory;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.Persistent;
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

    protected AccessOption accessOption;

    protected List<IProfile> participant;

    protected IProfile owner;

    protected LocalDateTime startDate;

    protected LocalDateTime endDate;

    protected TournamentSystemType systemType;

    @Setter(value = AccessLevel.PROTECTED)
    protected CompetitionType competitionType;

    protected TournamentParticipantType tournamentParticipantType;

    protected List<String> staffIds;

    protected List<String> contributorsIds;

    protected int minParticipants;

    protected int maxParticipants;

    @Transient
    public DuelBuilder getDuelBuilder() {
        return DuelBuilderFactory.getInstance(competitionType);
    }
}
