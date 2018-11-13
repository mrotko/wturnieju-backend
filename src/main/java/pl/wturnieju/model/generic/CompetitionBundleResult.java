package pl.wturnieju.model.generic;

import org.apache.commons.lang3.tuple.MutablePair;

import lombok.Data;

@Data
public abstract class CompetitionBundleResult {

    protected MutablePair<Double, Double> points;

}


