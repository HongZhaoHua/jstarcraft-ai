package jstarcraft.ai.math.algorithm.lsh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.model.knn.Knn;
import com.jstarcraft.core.utility.RandomUtility;
import com.jstarcraft.core.utility.StringUtility;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public abstract class LshHashFamilyTestCase {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int[] signature(MathVector data, VectorHashFunction[] functions) {
        int size = functions.length;
        int[] signature = new int[size];
        for (int index = 0; index < size; index++) {
            VectorHashFunction function = functions[index];
            signature[index] = function.hash(data);
        }
        return signature;
    }

    protected abstract LshHashFamily getHashFamily(int dimensions);

    @Test
    public void testHashFunction() {
        MathVector left = new ArrayVector(7, new float[] { 1F, 1F, 0F, 0F, 0F, 1F, 1F });
        MathVector middle = new ArrayVector(7, new float[] { 0F, 0F, 1F, 1F, 1F, 0F, 0F });
        MathVector right = new ArrayVector(7, new float[] { 1F, 0F, 0F, 0F, 0F, 1F, 1F });
        MathVector query = new ArrayVector(7, new float[] { 0F, 1F, 1F, 1F, 1F, 0F, 0F });

        List<MathVector> dataset = new ArrayList<>(4);
        dataset.add(left);
        dataset.add(middle);
        dataset.add(right);
        dataset.add(query);

        LshHashFamily family = getHashFamily(7);
        Random random = new Random(0);
        VectorHashFunction[] functions = new VectorHashFunction[3];
        for (int index = 0; index < 3; index++) {
            VectorHashFunction function = family.getHashFunction(random);
            functions[index] = function;
        }

        if (logger.isDebugEnabled()) {
            for (MathVector data : dataset) {
                String message = StringUtility.format("family is {}, signature is {}", family.getClass().getName(), Arrays.toString(signature(data, functions)));
                logger.debug(message);
            }
        }

        Assert.assertArrayEquals(signature(left, functions), signature(right, functions));
        Assert.assertArrayEquals(signature(middle, functions), signature(query, functions));
    }

    protected abstract float getRandomData();

    @Test
    public void testQuery() {
        // 段大小
        int stageSize = 5;
        // 桶大小
        int bucketSize = 200;
        // 签名大小
        int signatureSize = 5;

        int dimensionSize = stageSize * signatureSize * 40;

        Int2ObjectOpenHashMap<ArrayList<NameVector>>[] tables = new Int2ObjectOpenHashMap[stageSize];
        LshHashFamily family = getHashFamily(dimensionSize);
        Random random = new Random(0);
        VectorHashFunction[][] functions = new VectorHashFunction[stageSize][signatureSize];
        for (int stageIndex = 0; stageIndex < stageSize; stageIndex++) {
            tables[stageIndex] = new Int2ObjectOpenHashMap<>();
            for (int signatureIndex = 0; signatureIndex < signatureSize; signatureIndex++) {
                VectorHashFunction function = family.getHashFunction(random);
                functions[stageIndex][signatureIndex] = function;
            }
        }

        for (int instanceIndex = 0; instanceIndex < 1000; instanceIndex++) {
            float[] data = new float[dimensionSize];
            for (int dimensionIndex = 0; dimensionIndex < dimensionSize; dimensionIndex++) {
                data[dimensionIndex] = getRandomData();
            }
            NameVector vector = new NameVector("data" + instanceIndex, data);
            for (int stageIndex = 0; stageIndex < stageSize; stageIndex++) {
                int buckIndex = Arrays.hashCode(signature(vector, functions[stageIndex])) % bucketSize;
                Int2ObjectOpenHashMap<ArrayList<NameVector>> table = tables[stageIndex];
                ArrayList<NameVector> vectors = table.get(buckIndex);
                if (vectors == null) {
                    vectors = new ArrayList<>(1000);
                    table.put(buckIndex, vectors);
                }
                vectors.add(vector);
            }
        }

        float[] data = new float[dimensionSize];
        for (int dimensionIndex = 0; dimensionIndex < dimensionSize; dimensionIndex++) {
            data[dimensionIndex] = getRandomData();
        }
        NameVector query = new NameVector("query", data);
        Collection<NameVector> neighbors = new HashSet<>();
        for (int neighborIndex = 0; neighborIndex < 5; neighborIndex++) {
            float[] copy = Arrays.copyOf(data, data.length);
            copy[RandomUtility.randomInteger(dimensionSize)] = copy[RandomUtility.randomInteger(dimensionSize)] == 0F ? 1F : 0F;
            NameVector vector = new NameVector("neighbor" + neighborIndex, copy);
            for (int stageIndex = 0; stageIndex < stageSize; stageIndex++) {
                int buckIndex = Arrays.hashCode(signature(vector, functions[stageIndex])) % bucketSize;
                Int2ObjectOpenHashMap<ArrayList<NameVector>> table = tables[stageIndex];
                ArrayList<NameVector> vectors = table.get(buckIndex);
                if (vectors == null) {
                    vectors = new ArrayList<>(1000);
                    table.put(buckIndex, vectors);
                }
                vectors.add(vector);
            }
            neighbors.add(vector);
        }

        Knn<NameVector> knn = new Knn<>(5, new DistanceComparator(query, family));
        for (int stageIndex = 0; stageIndex < stageSize; stageIndex++) {
            int buckIndex = Arrays.hashCode(signature(query, functions[stageIndex])) % bucketSize;
            Int2ObjectOpenHashMap<ArrayList<NameVector>> table = tables[stageIndex];
            ArrayList<NameVector> vectors = table.get(buckIndex);
            if (vectors != null) {
                for (NameVector vector : vectors) {
                    knn.updateNeighbor(vector);
                }
            }
        }

        for (NameVector neighbor : knn.getNeighbors()) {
            Assert.assertTrue(neighbors.contains(neighbor));
        }

        if (logger.isDebugEnabled()) {
            String message = StringUtility.format("family is {}.\nquery is {}.\nneighbors is {}", family.getClass().getName(), query, knn.getNeighbors());
            logger.debug(message);
        }
    }

}
