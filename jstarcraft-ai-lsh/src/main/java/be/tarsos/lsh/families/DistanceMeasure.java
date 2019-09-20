package be.tarsos.lsh.families;

import com.jstarcraft.ai.math.structure.vector.MathVector;

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
     * @param left   The first vector.
     * @param right The other vector
     * @return A value representing the distance between two vectors.
     */
    float distance(MathVector left, MathVector right);
}
