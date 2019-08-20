package com.jstarcraft.ai.math.algorithm.similarity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.structure.matrix.HashMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;

public abstract class AbstractSimilarityTestCase {

    protected abstract boolean checkCorrelation(float correlation);

    protected abstract float getIdentical();

    protected abstract Similarity getSimilarity();

    @Test
    public void test() {
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

        Similarity similarity = getSimilarity();
        SymmetryMatrix similarityMatrix = similarity.makeSimilarityMatrix(scoreMatrix, false, 0F);
        assertEquals(rowSize, similarityMatrix.getRowSize());
        for (MatrixScalar term : similarityMatrix) {
            assertTrue(checkCorrelation(term.getValue()));
        }
        
        // 判断相等的情况.
        for (int index = 0, size = rowSize; index < size; index++) {
            Assert.assertEquals(getIdentical(), similarityMatrix.getValue(index, index), 0.001F);
            MathVector rowVector = scoreMatrix.getRowVector(index);
            Assert.assertEquals(getIdentical(), similarity.getCorrelation(rowVector, rowVector, 0F), 0.001F);
            MathVector columnVector = scoreMatrix.getColumnVector(index);
            Assert.assertEquals(getIdentical(), similarity.getCorrelation(columnVector, columnVector, 0F), 0.001F);
        }

        similarityMatrix = similarity.makeSimilarityMatrix(scoreMatrix, true, 0F);
        assertEquals(columnSize, similarityMatrix.getRowSize());
        for (MatrixScalar term : similarityMatrix) {
            assertTrue(checkCorrelation(term.getValue()));
        }
        for (int index = 0, size = columnSize; index < size; index++) {
            Assert.assertThat(similarityMatrix.getValue(index, index), CoreMatchers.equalTo(getIdentical()));
        }
    }

}
