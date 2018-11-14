package pl.wturnieju.model.generic;

import lombok.Data;

@Data
public abstract class ResultBundleUpdateContent<T extends CompetitionBundleResult> extends BundleUpdateContent {

    private T oldResult;

    private T newResult;

    public ResultBundleUpdateContent() {
        type = FixtureBundleUpdateContentType.RESULT;
    }
}