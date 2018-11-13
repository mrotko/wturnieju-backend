package pl.wturnieju.model.generic;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public abstract class BundleUpdateContent {

    @Setter(AccessLevel.PROTECTED)
    protected FixtureBundleUpdateContentType type;

}
