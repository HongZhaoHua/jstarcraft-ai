package be.tarsos.lsh.families;

import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Calculates the
 * <a href="http://en.wikipedia.org/wiki/Euclidean_distance">Euclidean
 * distance</a> between two vectors. Sometimes this is also called the
 * L<sub>2</sub> distance.
 * 
 * @author Joren Six
 */
public class EuclideanDistance implements DistanceMeasure {

    /*
     * (non-Javadoc)
     * 
     * @see
     * be.hogent.tarsos.lsh.families.DistanceMeasure#distance(be.hogent.tarsos.lsh.
     * Vector, be.hogent.tarsos.lsh.Vector)
     */
    @Override
    public float distance(MathVector left, MathVector right) {
        float sum = 0F;
        for (int dimension = 0; dimension < left.getDimensionSize(); dimension++) {
            float delta = left.getValue(dimension) - right.getValue(dimension);
            sum += delta * delta;
        }
        return (float) Math.sqrt(sum);
    }
}
