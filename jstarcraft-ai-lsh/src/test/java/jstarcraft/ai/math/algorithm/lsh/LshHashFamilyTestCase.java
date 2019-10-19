package jstarcraft.ai.math.algorithm.lsh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.StringUtility;

public abstract class LshHashFamilyTestCase {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<MathVector> dataset = new ArrayList<>(4);

    private MathVector left = new ArrayVector(7, new float[] { 1F, 1F, 0F, 0F, 0F, 1F, 1F });

    private MathVector middle = new ArrayVector(7, new float[] { 0F, 0F, 1F, 1F, 1F, 0F, 0F });

    private MathVector right = new ArrayVector(7, new float[] { 1F, 0F, 0F, 0F, 0F, 1F, 1F });

    private MathVector query = new ArrayVector(7, new float[] { 0F, 1F, 1F, 1F, 1F, 0F, 0F });

    {
        dataset.add(left);
        dataset.add(middle);
        dataset.add(right);
        dataset.add(query);
    }

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
        LshHashFamily family = getHashFamily(7);
        Random random = new Random(0);
        VectorHashFunction[] functions = new VectorHashFunction[3];
        for (int index = 0; index < 3; index++) {
            VectorHashFunction function = family.getHashFunction(random);
            functions[index] = function;
        }

        if (logger.isDebugEnabled()) {
            for (MathVector data : dataset) {
                String message = StringUtility.format("{}:{}", family.getClass().getName(), Arrays.toString(signature(data, functions)));
                logger.debug(message);
            }
        }

        Assert.assertArrayEquals(signature(left, functions), signature(right, functions));
        Assert.assertArrayEquals(signature(middle, functions), signature(query, functions));
    }

    public void testQuery() {

    }

}
