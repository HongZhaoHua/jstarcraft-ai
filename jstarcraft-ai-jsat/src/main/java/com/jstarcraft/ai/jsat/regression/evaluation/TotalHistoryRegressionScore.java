package com.jstarcraft.ai.jsat.regression.evaluation;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

/**
 * This abstract class provides the work for maintaining the history of
 * predictions and their true values.
 * 
 * @author Edward Raff
 */
public abstract class TotalHistoryRegressionScore implements RegressionScore {

    private static final long serialVersionUID = -5262934560490160236L;
    /**
     * List of the true target values
     */
    protected DoubleArrayList truths;
    /**
     * List of the predict values for each target
     */
    protected DoubleArrayList predictions;
    /**
     * The weight of importance for each point
     */
    protected DoubleArrayList weights;

    public TotalHistoryRegressionScore() {
    }

    /**
     * Copy constructor
     * 
     * @param toCopy the object to copy
     */
    public TotalHistoryRegressionScore(TotalHistoryRegressionScore toCopy) {
        if (toCopy.truths != null) {
            this.truths = new DoubleArrayList(toCopy.truths);
            this.predictions = new DoubleArrayList(toCopy.predictions);
            this.weights = new DoubleArrayList(toCopy.weights);
        }
    }

    @Override
    public void prepare() {
        truths = new DoubleArrayList();
        predictions = new DoubleArrayList();
        weights = new DoubleArrayList();
    }

    @Override
    public void addResult(double prediction, double trueValue, double weight) {
        truths.add(trueValue);
        predictions.add(prediction);
        weights.add(weight);
    }

    @Override
    public void addResults(RegressionScore other) {
        TotalHistoryRegressionScore otherObj = (TotalHistoryRegressionScore) other;
        this.truths.addAll(otherObj.truths);
        this.predictions.addAll(otherObj.predictions);
        this.weights.addAll(otherObj.weights);
    }

    @Override
    public abstract TotalHistoryRegressionScore clone();

}
