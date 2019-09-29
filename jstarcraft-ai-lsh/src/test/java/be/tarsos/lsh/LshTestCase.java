package be.tarsos.lsh;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jstarcraft.ai.math.algorithm.correlation.distance.EuclideanDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.ManhattanDistance;

import be.tarsos.lsh.families.CityBlockHashFamily;
import be.tarsos.lsh.families.CosineHashFamily;
import be.tarsos.lsh.families.EuclidianHashFamily;
import be.tarsos.lsh.families.HashFamily;
import be.tarsos.lsh.families.MinHashFamily;
import be.tarsos.lsh.util.TestUtils;

public class LshTestCase {

    @Test
    public void test() {
        Random rand = new Random(0L);
        int dimensions = 256;
        int radius = 10;
        List<KeyVector> dataset = TestUtils.generate(rand, dimensions, 100, 512);
        TestUtils.addNeighbours(rand, dataset, 4, radius);

        System.out.println("Radius for Euclidean distance.");
        int radiusEuclidean = (int) LSH.determineRadius(rand, dataset, new EuclideanDistance(), 20);
        System.out.println("\nRadius for City block distance.");
        int radiusCityBlock = (int) LSH.determineRadius(rand, dataset, new ManhattanDistance(), 20);
        System.out.printf("%10s%15s%10s%10s%10s%10s%10s%10s\n", "#hashes", "#hashTables", "Correct", "Touched", "linear", "LSH", "Precision", "Recall");
        HashFamily[] families = { new EuclidianHashFamily(radiusEuclidean, dimensions), new CityBlockHashFamily(radiusCityBlock, dimensions), new CosineHashFamily(dimensions), new MinHashFamily() };

        for (HashFamily family : families) {
            LSH lsh = new LSH(dataset, family);
            lsh.buildIndex(rand, 2, 128);
            System.out.println(family.getClass().getSimpleName());
            lsh.benchmark(4, family.createDistanceMeasure());
        }
    }

}
