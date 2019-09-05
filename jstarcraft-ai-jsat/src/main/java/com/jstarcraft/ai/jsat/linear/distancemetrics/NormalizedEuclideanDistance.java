package com.jstarcraft.ai.jsat.linear.distancemetrics;

import java.util.List;

import com.jstarcraft.ai.jsat.DataSet;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.datatransform.UnitVarianceTransform;
import com.jstarcraft.ai.jsat.linear.MatrixStatistics;
import com.jstarcraft.ai.jsat.linear.Vec;
import com.jstarcraft.ai.jsat.linear.VecOps;
import com.jstarcraft.ai.jsat.regression.RegressionDataSet;
import com.jstarcraft.ai.jsat.utils.DoubleList;
import com.jstarcraft.ai.jsat.utils.concurrent.ParallelUtils;

/**
 * Implementation of the Normalized Euclidean Distance Metric. The normalized
 * version divides each variable by its standard deviation, and then continues
 * as the normal {@link EuclideanDistance}. <br>
 * The same results can be achieved by first applying
 * {@link UnitVarianceTransform} to a data set before using the L2 norm.<br>
 * It is equivalent to the {@link MahalanobisDistance} if only the diagonal
 * values were used.
 * 
 * 
 * @author Edward Raff
 */
public class NormalizedEuclideanDistance extends TrainableDistanceMetric {

    private static final long serialVersionUID = 210109457671623688L;
    private Vec invStndDevs;

    /**
     * Creates a new Normalized Euclidean distance metric
     */
    public NormalizedEuclideanDistance() {
    }

    @Override
    public <V extends Vec> void train(List<V> dataSet) {
        invStndDevs = MatrixStatistics.covarianceDiag(MatrixStatistics.meanVector(dataSet), dataSet);
        invStndDevs.applyFunction((x) -> x * x);
        invStndDevs.applyFunction((x) -> 1 / x);
    }

    @Override
    public <V extends Vec> void train(List<V> dataSet, boolean parallel) {
        train(dataSet);
    }

    @Override
    public void train(DataSet dataSet) {
        invStndDevs = dataSet.getColumnMeanVariance()[1];
        invStndDevs.applyFunction((x) -> x * x);
        invStndDevs.applyFunction((x) -> 1 / x);
    }

    @Override
    public void train(DataSet dataSet, boolean parallel) {
        train(dataSet);
    }

    @Override
    public void train(ClassificationDataSet dataSet) {
        train((DataSet) dataSet);
    }

    @Override
    public void train(ClassificationDataSet dataSet, boolean parallel) {
        train(dataSet);
    }

    @Override
    public boolean supportsClassificationTraining() {
        return true;
    }

    @Override
    public void train(RegressionDataSet dataSet) {
        train((DataSet) dataSet);
    }

    @Override
    public void train(RegressionDataSet dataSet, boolean parallel) {
        train(dataSet);
    }

    @Override
    public boolean supportsRegressionTraining() {
        return true;
    }

    @Override
    public boolean needsTraining() {
        return invStndDevs == null;
    }

    @Override
    public NormalizedEuclideanDistance clone() {
        NormalizedEuclideanDistance clone = new NormalizedEuclideanDistance();
        if (this.invStndDevs != null)
            clone.invStndDevs = this.invStndDevs.clone();
        return clone;
    }

    @Override
    public double dist(Vec a, Vec b) {
        double r = VecOps.accumulateSum(invStndDevs, a, b, (double x) -> x * x);

        return Math.sqrt(r);
    }

    @Override
    public boolean isSymmetric() {
        return true;
    }

    @Override
    public boolean isSubadditive() {
        return true;
    }

    @Override
    public boolean isIndiscemible() {
        return true;
    }

    @Override
    public double metricBound() {
        return Double.POSITIVE_INFINITY;
    }

    /*
     * TODO when moving to java8, convert TrainableDistanceMetric into an interface,
     * fix this class up. Then extend WeightedEuclideanDistance
     */

    @Override
    public boolean supportsAcceleration() {
        return true;
    }

    @Override
    public List<Double> getAccelerationCache(List<? extends Vec> vecs, boolean parallel) {
        // Store the pnorms in the cache
        double[] cache = new double[vecs.size()];
        ParallelUtils.run(parallel, vecs.size(), (start, end) -> {
            for (int i = start; i < end; i++) {
                Vec v = vecs.get(i);
                cache[i] = VecOps.weightedDot(invStndDevs, v, v);
            }
        });
        return DoubleList.view(cache, vecs.size());
    }

    @Override
    public double dist(int a, int b, List<? extends Vec> vecs, List<Double> cache) {
        if (cache == null)
            return dist(vecs.get(a), vecs.get(b));

        return Math.sqrt(cache.get(a) + cache.get(b) - 2 * VecOps.weightedDot(invStndDevs, vecs.get(a), vecs.get(b)));
    }

    @Override
    public double dist(int a, Vec b, List<? extends Vec> vecs, List<Double> cache) {
        if (cache == null)
            return dist(vecs.get(a), b);

        return Math.sqrt(cache.get(a) + VecOps.weightedDot(invStndDevs, b, b) - 2 * VecOps.weightedDot(invStndDevs, vecs.get(a), b));
    }

    @Override
    public List<Double> getQueryInfo(Vec q) {
        DoubleList qi = new DoubleList(1);
        qi.add(VecOps.weightedDot(invStndDevs, q, q));
        return qi;
    }

    @Override
    public double dist(int a, Vec b, List<Double> qi, List<? extends Vec> vecs, List<Double> cache) {
        if (cache == null)
            return dist(vecs.get(a), b);

        return Math.sqrt(cache.get(a) + qi.get(0) - 2 * VecOps.weightedDot(invStndDevs, vecs.get(a), b));
    }
}
