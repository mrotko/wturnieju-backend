package pl.wturnieju.service;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EndTournamentBundleUpdateContent extends TournamentBundleUpdateContent {

    private LocalDateTime date;

    public EndTournamentBundleUpdateContent() {
        setType(TournamentBundleUpdateContentType.END);
    }

}
