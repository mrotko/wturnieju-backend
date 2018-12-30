package pl.wturnieju.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.TournamentSystemType;

@Data
public class TournamentDTO {

    private String id;

    private String name;

    private String description;

    private String place;

    private String img;

    private TournamentStatus status;

    private AccessOption accessOption;

    private List<TournamentParticipantDTO> participants = new ArrayList<>();

    private IProfile owner;

    private Timestamp startDate;

    private Timestamp endDate;

    private TournamentSystemType systemType;

    private CompetitionType competitionType;

    private TournamentParticipantType participantType;

    private Integer minParticipants;

    private Integer maxParticipants;

    private Integer plannedRounds;
}
