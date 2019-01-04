package pl.wturnieju.model.generic;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.TournamentSystemType;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class Tournament extends Persistent {

    protected String name;

    protected String description;

    protected String place;

    protected String img;

    protected TournamentStatus status;

    protected AccessOption accessOption;

    protected String invitationToken;

    protected List<TournamentParticipant> participants = new ArrayList<>();

    protected IProfile owner;

    protected Timestamp startDate;

    protected Timestamp endDate;

    protected TournamentSystemType systemType;

    @Setter(value = AccessLevel.PROTECTED)
    protected CompetitionType competitionType;

    protected TournamentParticipantType tournamentParticipantType;

    protected List<String> staffIds = new ArrayList<>();

    protected List<String> contributorsIds = new ArrayList<>();

    @JsonIgnore
    protected TournamentSystemState tournamentSystemState;

    protected int minParticipants;

    protected int maxParticipants;

    protected int plannedRounds;
}
