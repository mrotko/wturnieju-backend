package pl.wturnieju.model.generic;

import lombok.Data;
import pl.wturnieju.model.FixtureStatus;

@Data
public class StatusBundleUpdateContent extends BundleUpdateContent {

    private FixtureStatus oldStatus;

    private FixtureStatus newStatus;

    public StatusBundleUpdateContent() {
        type = FixtureBundleUpdateContentType.STATUS;
    }
}
