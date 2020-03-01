package com.jstarcraft.ai.model.estimate;

public class DiscreteEstimator implements QualityEstimator {

    private float prior;

    private final float[] counts;

    private float sum;

    public DiscreteEstimator(int symbols, boolean laplace) {
        this(symbols, laplace ? 1F : 0F);
    }

    public DiscreteEstimator(int symbols, float prior) {
        this.counts = new float[symbols];
        this.prior = prior;
        for (int symbol = 0; symbol < symbols; symbol++) {
            this.counts[symbol] = prior;
        }
        this.sum = symbols * prior;
    }

    @Override
    public void updateProbability(Integer data, float weight) {
        counts[data] += weight;
        sum += weight;
    }

    @Override
    public float getProbability(Integer data) {
        if (sum == 0F) {
            return 0F;
        }
        return counts[(int) data] / sum;
    }

}
