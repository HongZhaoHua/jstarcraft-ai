package be.tarsos.lsh.families;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jstarcraft.ai.math.structure.vector.MathVector;

import be.tarsos.lsh.KeyVector;

public class HashTestCase {

    private List<MathVector> dataset = new ArrayList<>(4);
    {
        MathVector left = new KeyVector("1", new float[] { 1F, 1F, 0F, 0F, 0F, 1F, 1F });
        dataset.add(left);
        MathVector middle = new KeyVector("1", new float[] { 0F, 0F, 1F, 1F, 1F, 0F, 0F });
        dataset.add(middle);
        MathVector right = new KeyVector("1", new float[] { 1F, 0F, 0F, 0F, 0F, 1F, 1F });
        dataset.add(right);
        MathVector query = new KeyVector("1", new float[] { 0F, 1F, 1F, 1F, 1F, 0F, 0F });
        dataset.add(query);
    }

    @Test
    public void testCosineHash() {
        Random rand = new Random(0);
        List<HashFunction> hashs = new ArrayList<>(4);
        for (int index = 0; index < 4; index++) {
            CosineHash hash = new CosineHash(rand, 7);
            hashs.add(hash);
        }
        for (MathVector data : dataset) {
            StringBuilder buffer = new StringBuilder("CosineHash:");
            for (HashFunction hash : hashs) {
                int hashValue = hash.hash(data);
                buffer.append(hashValue).append(",");
            }
            System.out.println(buffer.toString());
        }
    }

    @Test
    public void testEuclideanHash() {
        Random rand = new Random(0);
        List<HashFunction> hashs = new ArrayList<>(4);
        for (int index = 0; index < 4; index++) {
            EuclideanHash hash = new EuclideanHash(rand, 7, 7);
            hashs.add(hash);
        }
        for (MathVector data : dataset) {
            StringBuilder buffer = new StringBuilder("EuclideanHash:");
            for (HashFunction hash : hashs) {
                int hashValue = hash.hash(data);
                buffer.append(hashValue).append(",");
            }
            System.out.println(buffer.toString());
        }
    }

    @Test
    public void testManhattanHash() {
        Random rand = new Random(0);
        List<HashFunction> hashs = new ArrayList<>(4);
        for (int index = 0; index < 4; index++) {
            ManhattanHash hash = new ManhattanHash(rand, 7, 7);
            hashs.add(hash);
        }
        for (MathVector data : dataset) {
            StringBuilder buffer = new StringBuilder("ManhattanHash:");
            for (HashFunction hash : hashs) {
                int hashValue = hash.hash(data);
                buffer.append(hashValue).append(",");
            }
            System.out.println(buffer.toString());
        }
    }

    @Test
    public void testMinHash() {
        Random rand = new Random(0);
        List<HashFunction> hashs = new ArrayList<>(4);
        for (int index = 0; index < 4; index++) {
            MinHash hash = new MinHash(rand);
            hashs.add(hash);
        }
        for (MathVector data : dataset) {
            StringBuilder buffer = new StringBuilder("MinHash:");
            for (HashFunction hash : hashs) {
                int hashValue = hash.hash(data);
                buffer.append(hashValue).append(",");
            }
            System.out.println(buffer.toString());
        }
    }

}
