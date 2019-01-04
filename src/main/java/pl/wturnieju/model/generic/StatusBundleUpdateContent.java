package pl.wturnieju.model.generic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wturnieju.model.FixtureStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatusBundleUpdateContent extends BundleUpdateContent {

    private FixtureStatus oldStatus;

    private FixtureStatus newStatus;

    public StatusBundleUpdateContent() {
        type = FixtureBundleUpdateContentType.STATUS;
    }
}
