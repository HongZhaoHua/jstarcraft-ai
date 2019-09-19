package be.tarsos.lsh.families;

import java.util.Comparator;

import be.tarsos.lsh.KeyVector;

/**
 * This comparator can be used to sort candidate neighbours according to their
 * distance to a query vector. Either for linear search or to sort the LSH
 * candidates found in colliding hash bins.
 * 
 * @author Joren Six
 */
public class DistanceComparator implements Comparator<KeyVector> {

    private final KeyVector query;
    private final DistanceMeasure distanceMeasure;

    /**
     * 
     * @param query           The query vector.
     * @param distanceMeasure The distance vector to use.
     */
    public DistanceComparator(KeyVector query, DistanceMeasure distanceMeasure) {
        this.query = query;
        this.distanceMeasure = distanceMeasure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(KeyVector one, KeyVector other) {
        float oneDistance = distanceMeasure.distance(query, one);
        float otherDistance = distanceMeasure.distance(query, other);
        return Float.compare(oneDistance, otherDistance);
    }
}
