package be.tarsos.lsh.families;

import be.tarsos.lsh.Vector;

/**
 * A distance measure defines how distance is calculated, measured as it were,
 * between two vectors. Each hash family has a corresponding distance measure
 * which is abstracted using this interface.
 * 
 * @author Joren Six
 */
public interface DistanceMeasure {

    /**
     * Calculate the distance between two vectors. From one to two.
     * 
     * @param one   The first vector.
     * @param other The other vector
     * @return A value representing the distance between two vectors.
     */
    float distance(Vector one, Vector other);
}
