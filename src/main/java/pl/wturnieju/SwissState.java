package pl.wturnieju;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import pl.wturnieju.model.generic.Tournament;

@Getter
public class SwissState extends TournamentState {

    private List<SwissSystemParticipant> participants = new ArrayList<>();

    private List<Duel> duels = new ArrayList<>();

    public SwissState(Tournament tournament) {
        super(tournament);
    }
}