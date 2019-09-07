
package com.jstarcraft.ai.jsat.clustering.dissimilarity;

import java.util.List;

import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.linear.distancemetrics.DistanceMetric;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Measures the dissimilarity of two clusters by returning the value of the
 * maximal dissimilarity of any two pairs of data points where one is from each
 * cluster.
 * 
 * @author Edward Raff
 */
public class CompleteLinkDissimilarity extends LanceWilliamsDissimilarity implements UpdatableClusterDissimilarity {
    /**
     * Creates a new CompleteLinkDissimilarity using the {@link EuclideanDistance}
     */
    public CompleteLinkDissimilarity() {
        this(new EuclideanDistance());
    }

    /**
     * Creates a new CompleteLinkDissimilarity
     * 
     * @param dm the distance metric to use between individual points
     */
    public CompleteLinkDissimilarity(DistanceMetric dm) {
        super(dm);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy the object to copy
     */
    public CompleteLinkDissimilarity(CompleteLinkDissimilarity toCopy) {
        this(toCopy.dm.clone());
    }

    @Override
    public CompleteLinkDissimilarity clone() {
        return new CompleteLinkDissimilarity(this);
    }

    @Override
    public double dissimilarity(List<DataPoint> a, List<DataPoint> b) {
        double maxDiss = Double.MIN_VALUE;

        double tmpDist;
        for (DataPoint ai : a)
            for (DataPoint bi : b)
                if ((tmpDist = distance(ai, bi)) > maxDiss)
                    maxDiss = tmpDist;

        return maxDiss;
    }

    @Override
    public double dissimilarity(IntSet a, IntSet b, double[][] distanceMatrix) {
        double maxDiss = Double.MIN_VALUE;

        for (int ai : a)
            for (int bi : b)
                if (getDistance(distanceMatrix, ai, bi) > maxDiss)
                    maxDiss = getDistance(distanceMatrix, ai, bi);

        return maxDiss;
    }

    @Override
    public double dissimilarity(int i, int ni, int j, int nj, double[][] distanceMatrix) {
        return getDistance(distanceMatrix, i, j);
    }

    @Override
    public double dissimilarity(int i, int ni, int j, int nj, int k, int nk, double[][] distanceMatrix) {
        return Math.max(getDistance(distanceMatrix, i, k), getDistance(distanceMatrix, j, k));
    }

    @Override
    public double dissimilarity(int ni, int nj, int nk, double d_ij, double d_ik, double d_jk) {
        return Math.max(d_ik, d_jk);
    }

    @Override
    protected double aConst(boolean iFlag, int ni, int nj, int nk) {
        return 0.5;
    }

    @Override
    protected double bConst(int ni, int nj, int nk) {
        return 0;
    }

    @Override
    protected double cConst(int ni, int nj, int nk) {
        return 0.5;
    }

}
