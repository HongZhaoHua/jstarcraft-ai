package be.tarsos.lsh.families;

import be.tarsos.lsh.Vector;

/**
 * This distance measure calculates the city block distance between two vectors.
 * This distance metric also known as the Manhattan distance or L<sub>1</sub>
 * distance. Wikipedia calls it
 * <a href="http://en.wikipedia.org/wiki/Taxicab_geometry">"Taxicab
 * geometry"</a>: <blockquote> Taxicab geometry, considered by Hermann Minkowski
 * in the 19th century, is a form of geometry in which the usual distance
 * function or metric of Euclidean geometry is replaced by a new metric in which
 * the distance between two points is the sum of the absolute differences of
 * their coordinates. The taxicab metric is also known as rectilinear distance,
 * L1 distance or norm (see Lp space), city block distance, Manhattan distance,
 * or Manhattan length, with corresponding variations in the name of the
 * geometry. The latter names allude to the grid layout of most streets on the
 * island of Manhattan, which causes the shortest path a car could take between
 * two intersections in the borough to have length equal to the intersections'
 * distance in taxicab geometry. </blockquote>
 * 
 * @author Joren Six
 * 
 */
public class CityBlockDistance implements DistanceMeasure {

    /*
     * (non-Javadoc)
     * 
     * @see
     * be.hogent.tarsos.lsh.families.DistanceMeasure#distance(be.hogent.tarsos.lsh.
     * Vector, be.hogent.tarsos.lsh.Vector)
     */
    @Override
    public float distance(Vector one, Vector other) {
        float distance = 0F;
        for (int d = 0; d < one.getDimensionSize(); d++) {
            distance += Math.abs(one.getValue(d) - other.getValue(d));
        }
        return distance;
    }

}
