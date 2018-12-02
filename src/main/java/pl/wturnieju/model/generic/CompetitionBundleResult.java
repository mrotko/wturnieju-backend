package pl.wturnieju.model.generic;

import org.apache.commons.lang3.tuple.Pair;

import lombok.Data;

@Data
public abstract class CompetitionBundleResult {

    protected Pair<Double, Double> result;
}


