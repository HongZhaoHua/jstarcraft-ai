package be.tarsos.lsh.families;

import be.tarsos.lsh.Vector;

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
    public float distance(Vector one, Vector other) {
        float sum = 0F;
        for (int d = 0; d < one.getDimensions(); d++) {
            float delta = one.getValue(d) - other.getValue(d);
            sum += delta * delta;
        }
        return (float) Math.sqrt(sum);
    }
}
