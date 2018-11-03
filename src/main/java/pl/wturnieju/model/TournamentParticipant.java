package pl.wturnieju.model;

import lombok.Data;

@Data
public class TournamentParticipant implements IProfile {

    private String id;

    private ParticipantStatus participantStatus;
}
