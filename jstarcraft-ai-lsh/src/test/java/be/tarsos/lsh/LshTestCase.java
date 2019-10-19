package be.tarsos.lsh;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jstarcraft.ai.math.algorithm.correlation.distance.EuclideanDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.ManhattanDistance;

import be.tarsos.lsh.util.TestUtils;
import jstarcraft.ai.math.algorithm.lsh.CosineHashFamily;
import jstarcraft.ai.math.algorithm.lsh.EuclideanHashFamily;
import jstarcraft.ai.math.algorithm.lsh.LshHashFamily;
import jstarcraft.ai.math.algorithm.lsh.ManhattanHashFamily;
import jstarcraft.ai.math.algorithm.lsh.MinHashFamily;

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
        LshHashFamily[] families = { new EuclideanHashFamily(radiusEuclidean, dimensions), new ManhattanHashFamily(radiusCityBlock, dimensions), new CosineHashFamily(dimensions), new MinHashFamily() };

        for (LshHashFamily family : families) {
            LSH lsh = new LSH(dataset, family);
            lsh.buildIndex(rand, 2, 128);
            System.out.println(family.getClass().getSimpleName());
            lsh.benchmark(4, family.getDistance());
        }
    }

}
