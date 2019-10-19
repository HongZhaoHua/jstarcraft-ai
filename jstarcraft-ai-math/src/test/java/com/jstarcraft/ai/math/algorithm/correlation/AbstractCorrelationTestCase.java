package com.jstarcraft.ai.math.algorithm.correlation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.algorithm.correlation.MathCorrelation;
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

    protected abstract MathCorrelation getCorrelation();

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

            MathCorrelation correlation = getCorrelation();
            SymmetryMatrix symmetryMatrix = new SymmetryMatrix(scoreMatrix.getRowSize());
            correlation.calculateCoefficients(scoreMatrix, false, symmetryMatrix::setValue);
            assertEquals(rowSize, symmetryMatrix.getRowSize());
            for (MatrixScalar term : symmetryMatrix) {
                assertTrue(checkCorrelation(term.getValue()));
            }

            // 判断相等的情况.
            for (int index = 0, size = rowSize; index < size; index++) {
                MathVector rowVector = scoreMatrix.getRowVector(index);
                Assert.assertEquals(getIdentical(), correlation.getCoefficient(rowVector, rowVector), 0.001F);
                MathVector columnVector = scoreMatrix.getColumnVector(index);
                Assert.assertEquals(getIdentical(), correlation.getCoefficient(columnVector, columnVector), 0.001F);
                Assert.assertEquals(getIdentical(), symmetryMatrix.getValue(index, index), 0.001F);
            }

            symmetryMatrix = new SymmetryMatrix(scoreMatrix.getColumnSize());
            correlation.calculateCoefficients(scoreMatrix, true, symmetryMatrix::setValue);
            assertEquals(columnSize, symmetryMatrix.getRowSize());
            for (MatrixScalar term : symmetryMatrix) {
                assertTrue(checkCorrelation(term.getValue()));
            }
            for (int index = 0, size = columnSize; index < size; index++) {
                Assert.assertThat(symmetryMatrix.getValue(index, index), CoreMatchers.equalTo(getIdentical()));
            }
        });
        try {
            future.get();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
