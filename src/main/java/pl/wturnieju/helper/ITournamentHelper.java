package pl.wturnieju.helper;

import java.util.List;

import pl.wturnieju.tournament.Tournament;

public interface ITournamentHelper {

    List<Tournament> getUserTournaments(String userId);
}
