package pl.wturnieju.service;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StartTournamentBundleUpdateContent extends TournamentBundleUpdateContent {

    private LocalDateTime date;

    public StartTournamentBundleUpdateContent() {
        setType(TournamentBundleUpdateContentType.START);
    }
}
