package com.jstarcraft.ai.math.algorithm.correlation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;
import com.jstarcraft.ai.math.structure.matrix.HashMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;

public abstract class AbstractCorrelationTestCase {

    protected abstract boolean checkCorrelation(float correlation);

    protected abstract float getIdentical();

    protected abstract Correlation getCorrelation();

    @Test
    public void test() {
        EnvironmentContext context = EnvironmentFactory.getContext();
        Future<?> future = context.doTask(() -> {
            int rowSize = 50;
            int columnSize = 100;
            HashMatrix table = new HashMatrix(true, rowSize, columnSize, new Long2FloatRBTreeMap());
            for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
                for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
                    if (RandomUtility.randomBoolean()) {
                        table.setValue(rowIndex, columnIndex, RandomUtility.randomFloat(1F));
                    }
                }
            }
            SparseMatrix scoreMatrix = SparseMatrix.valueOf(rowSize, columnSize, table);

            Correlation similarity = getCorrelation();
            SymmetryMatrix similarityMatrix = similarity.makeCorrelationMatrix(scoreMatrix, false);
            assertEquals(rowSize, similarityMatrix.getRowSize());
            for (MatrixScalar term : similarityMatrix) {
                assertTrue(checkCorrelation(term.getValue()));
            }

            // 判断相等的情况.
            for (int index = 0, size = rowSize; index < size; index++) {
                MathVector rowVector = scoreMatrix.getRowVector(index);
                Assert.assertEquals(getIdentical(), similarity.getCoefficient(rowVector, rowVector), 0.001F);
                MathVector columnVector = scoreMatrix.getColumnVector(index);
                Assert.assertEquals(getIdentical(), similarity.getCoefficient(columnVector, columnVector), 0.001F);
                Assert.assertEquals(getIdentical(), similarityMatrix.getValue(index, index), 0.001F);
            }

            similarityMatrix = similarity.makeCorrelationMatrix(scoreMatrix, true);
            assertEquals(columnSize, similarityMatrix.getRowSize());
            for (MatrixScalar term : similarityMatrix) {
                assertTrue(checkCorrelation(term.getValue()));
            }
            for (int index = 0, size = columnSize; index < size; index++) {
                Assert.assertThat(similarityMatrix.getValue(index, index), CoreMatchers.equalTo(getIdentical()));
            }
        });
        try {
            future.get();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
