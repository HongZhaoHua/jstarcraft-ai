package jstarcraft.ai.math.algorithm.lsh;

import java.util.Comparator;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * This comparator can be used to sort candidate neighbours according to their
 * distance to a query vector. Either for linear search or to sort the LSH
 * candidates found in colliding hash bins.
 * 
 * @author Joren Six
 */
public class DistanceComparator implements Comparator<MathVector> {

    private final MathVector query;
    private final AbstractDistance distanceMeasure;

    /**
     * 
     * @param query           The query vector.
     * @param distanceMeasure The distance vector to use.
     */
    public DistanceComparator(MathVector query, AbstractDistance distanceMeasure) {
        this.query = query;
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public int compare(MathVector left, MathVector right) {
        float oneDistance = distanceMeasure.getCoefficient(query, left);
        float otherDistance = distanceMeasure.getCoefficient(query, right);
        return Float.compare(oneDistance, otherDistance);
    }
}
