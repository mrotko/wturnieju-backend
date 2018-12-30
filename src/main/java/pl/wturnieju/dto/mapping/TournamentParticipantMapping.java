package pl.wturnieju.dto.mapping;

import pl.wturnieju.dto.TournamentParticipantDTO;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.User;

public class TournamentParticipantMapping {
    public static TournamentParticipantDTO map(User user, TournamentParticipant participant) {
        if (user == null || participant == null) {
            return null;
        }

        var dto = new TournamentParticipantDTO();

        dto.setId(participant.getId());
        dto.setStatus(participant.getParticipantStatus());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getUsername());
        dto.setInvitationStatus(participant.getInvitationStatus());

        return dto;
    }
}
