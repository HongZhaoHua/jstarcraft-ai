package com.jstarcraft.ai.jsat.regression.evaluation;

import com.jstarcraft.ai.jsat.linear.DenseVector;

/**
 * Uses the Sum of Squared Errors divided by the sum of the squared true values
 * subtracted from their mean. This produces an error metric that has no units.
 * 
 * @author Edward Raff
 */
public class RelativeSquaredError extends TotalHistoryRegressionScore {

    private static final long serialVersionUID = 8377798320269626429L;

    /**
     * Creates a new Relative Squared Error object
     */
    public RelativeSquaredError() {
        super();
    }

    /**
     * Copy constructor
     * 
     * @param toCopy the object to copy
     */
    public RelativeSquaredError(RelativeSquaredError toCopy) {
        super(toCopy);
    }

    @Override
    public double getScore() {
        double trueMean = new DenseVector(truths.elements(), 0, truths.size()).mean();
        double numer = 0, denom = 0;
        for (int i = 0; i < truths.size(); i++) {
            numer += Math.pow(predictions.getDouble(i) - truths.getDouble(i), 2);
            denom += Math.pow(trueMean - truths.getDouble(i), 2);
        }
        return numer / denom;
    }

    @Override
    public boolean lowerIsBetter() {
        return true;
    }

    @Override
    public RelativeSquaredError clone() {
        return new RelativeSquaredError(this);
    }

    @Override
    public int hashCode() {// XXX this is a strange hashcode method
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {// XXX check for equality of fields and obj == null
        if (this.getClass().isAssignableFrom(obj.getClass()) && obj.getClass().isAssignableFrom(this.getClass())) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Relative Squared Error";
    }

}
