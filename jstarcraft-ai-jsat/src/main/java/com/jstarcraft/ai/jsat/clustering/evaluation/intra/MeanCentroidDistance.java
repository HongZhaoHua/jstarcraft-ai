package com.jstarcraft.ai.jsat.clustering.evaluation.intra;

import java.util.List;

import com.jstarcraft.ai.jsat.DataSet;
import com.jstarcraft.ai.jsat.SimpleDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.linear.DenseVector;
import com.jstarcraft.ai.jsat.linear.MatrixStatistics;
import com.jstarcraft.ai.jsat.linear.Vec;
import com.jstarcraft.ai.jsat.linear.distancemetrics.DistanceMetric;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;

/**
 * Evaluates a cluster's validity by computing the mean distance of each point
 * in the cluster from the cluster's centroid.
 * 
 * @author Edward Raff
 */
public class MeanCentroidDistance implements IntraClusterEvaluation {
    private DistanceMetric dm;

    /**
     * Creates a new MeanCentroidDistance using the {@link EuclideanDistance}
     */
    public MeanCentroidDistance() {
        this(new EuclideanDistance());
    }

    /**
     * Creates a new MeanCentroidDistance.
     * 
     * @param dm the metric to measure the distance between two points by
     */
    public MeanCentroidDistance(DistanceMetric dm) {
        this.dm = dm;
    }

    /**
     * Copy constructor
     * 
     * @param toCopy the object to copy
     */
    public MeanCentroidDistance(MeanCentroidDistance toCopy) {
        this(toCopy.dm.clone());
    }

    @Override
    public double evaluate(int[] designations, DataSet dataSet, int clusterID) {
        Vec mean = new DenseVector(dataSet.getNumNumericalVars());

        int clusterSize = 0;
        for (int i = 0; i < dataSet.size(); i++)
            if (designations[i] == clusterID) {
                clusterSize++;
                mean.mutableAdd(dataSet.getDataPoint(i).getNumericalValues());
            }
        mean.mutableDivide(clusterSize);

        double dists = 0.0;

        for (int i = 0; i < dataSet.size(); i++)
            if (designations[i] == clusterID)
                dists += dm.dist(dataSet.getDataPoint(i).getNumericalValues(), mean);

        return dists / dataSet.size();
    }

    @Override
    public double evaluate(List<DataPoint> dataPoints) {
        Vec mean = MatrixStatistics.meanVector(new SimpleDataSet(dataPoints));

        double dists = 0.0;
        for (DataPoint dp : dataPoints)
            dists += dm.dist(dp.getNumericalValues(), mean);

        return dists / dataPoints.size();
    }

    @Override
    public MeanCentroidDistance clone() {
        return new MeanCentroidDistance(this);
    }

}
