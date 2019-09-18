package be.tarsos.lsh.hamming;

import java.util.Comparator;

/**
 * This comparator can be used to sort candidate neighbours according to their
 * distance to a query vector. Either for linear search or to sort the LSH
 * candidates found in colliding hash bins.
 * 
 * @author Joren Six
 */
public class HammingDistanceComparator implements Comparator<BinVector> {

    /**
     * 
     * @param query           The query vector.
     * @param distanceMeasure The distance vector to use.
     */
    public HammingDistanceComparator() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(BinVector one, BinVector other) {
        return one.hammingDistance(other);
    }
}
