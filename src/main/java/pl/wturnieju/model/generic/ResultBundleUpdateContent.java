package pl.wturnieju.model.generic;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResultBundleUpdateContent<T extends CompetitionBundleResult> extends BundleUpdateContent {

    private T oldResult;

    private T newResult;

    public ResultBundleUpdateContent() {
        type = FixtureBundleUpdateContentType.RESULT;
    }
}
