package pl.wturnieju.service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public abstract class TournamentBundleUpdateContent {

    @Setter(value = AccessLevel.PROTECTED)
    private TournamentBundleUpdateContentType type;

}
