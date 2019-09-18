package be.tarsos.lsh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.tarsos.lsh.Vector;

/**
 * Makes it easy to generate random test data sets.
 * 
 * @author Joren Six
 */
public class TestUtils {

    /**
     * Generate a data set.
     * 
     * @param dimensions  The number of dimensions of the vector.
     * @param datasetSize The size of the data set.
     * @param maxValue    The maximum value of the coordinates of the vectors (a
     *                    number between 0 and maxValue is chosen randomly).
     * @return A data set with the properties described above.
     */
    public static List<Vector> generate(Random rand, int dimensions, int datasetSize, int maxValue) {
        List<Vector> ret = new ArrayList<Vector>();
        for (int i = 0; i < datasetSize; i++) {
            Vector item = new Vector(dimensions);
            for (int d = 0; d < dimensions; d++) {
                float point = rand.nextInt(maxValue);
                item.set(d, point);
            }
            ret.add(item);
        }
        return ret;
    }

    /**
     * Adds neighbours to each element in a data set. Each element gets a number of
     * neighbours added. E.g. If a data set has 10 elements and 2 neighbours are
     * added, you end up with <code>10 + 2 x 10 = 30</code> elements. Each element
     * is copied and moved slightly according to radius.
     * 
     * @param dataset                 The data set.
     * @param numberOfNeighboursToAdd The number of neighbours to add. E.g. If a
     *                                data set has 10 elements and 2 neighbours are
     *                                added, you end up with
     *                                <code>10 + 2 x 10 = 30</code> elements.
     * @param radius                  The radius used to create neigbours. In each
     *                                dimension a random number between -radius and
     *                                +radius is added to the current value.
     */
    public static void addNeighbours(Random rand, List<Vector> dataset, int numberOfNeighboursToAdd, float radius) {
        int datasetSize = dataset.size();
        for (int i = 0; i < datasetSize; i++) {
            Vector original = dataset.get(i);
            for (int neighbours = 0; neighbours < numberOfNeighboursToAdd; neighbours++) {
                Vector neighbour = new Vector(original);
                for (int d = 0; d < neighbour.getDimensions(); d++) {
                    // copy the point but add or subtract a value between -radius and +radius
                    float diff = radius + (-radius - radius) * rand.nextFloat();
                    float point = neighbour.get(d) + diff;
                    neighbour.set(d, point);
                }
                dataset.add(neighbour);
            }
        }
    }
}
