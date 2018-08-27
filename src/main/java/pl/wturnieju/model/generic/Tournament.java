package pl.wturnieju.model.generic;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.wturnieju.TournamentStatus;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.PlayerProfile;
import pl.wturnieju.model.TournamentSystemType;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
public abstract class Tournament extends Persistent {

    protected String name;

    protected String description;

    protected String img;

    protected CompetitionType competitionType;

    protected TournamentStatus status;

    protected List<? extends GenericProfile> participant;

    protected PlayerProfile owner;

    protected LocalDateTime startDate;

    protected LocalDateTime endDate;

    protected GenericStats stats;

    protected TournamentSystemType systemType;
}
