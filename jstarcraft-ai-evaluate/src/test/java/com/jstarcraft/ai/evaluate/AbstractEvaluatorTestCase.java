package com.jstarcraft.ai.evaluate;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.structure.matrix.HashMatrix;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Integer2FloatKeyValue;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;

public abstract class AbstractEvaluatorTestCase<L, R> {

    protected abstract Evaluator<L, R> getEvaluator(SparseMatrix featureMatrix);

    protected abstract float getMeasure();

    @Test
    public void test() throws Exception {
        RandomUtility.setSeed(0L);
        int rowSize = 1000;
        int columnSize = 1000;
        HashMatrix featureTable = new HashMatrix(true, rowSize, columnSize, new Long2FloatRBTreeMap());
        for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
                if (RandomUtility.randomFloat(1F) < 0.5F) {
                    featureTable.setValue(rowIndex, columnIndex, RandomUtility.randomFloat(1F));
                }
            }
        }
        SparseMatrix featureMatrix = SparseMatrix.valueOf(rowSize, columnSize, featureTable);
        Evaluator<L, R> evaluator = getEvaluator(featureMatrix);
        Integer2FloatKeyValue sum = evaluate(evaluator, featureMatrix);
        Assert.assertThat(sum.getValue() / sum.getKey(), CoreMatchers.equalTo(getMeasure()));
    }

    protected abstract L getLeft(MathVector vector);

    protected abstract R getRight(MathVector vector);

    private Integer2FloatKeyValue evaluate(Evaluator<L, R> evaluator, SparseMatrix featureMatrix) {
        Integer2FloatKeyValue sum = new Integer2FloatKeyValue(0, 0F);
        for (int index = 0, size = featureMatrix.getRowSize(); index < size; index++) {
            MathVector vector = featureMatrix.getRowVector(index);
            // 训练映射
            L leftCollection = getLeft(vector);
            // 推荐列表
            R rightList = getRight(vector);
            // 测量列表
            Integer2FloatKeyValue measure = evaluator.evaluate(leftCollection, rightList);
            sum.setKey(sum.getKey() + measure.getKey());
            sum.setValue(sum.getValue() + measure.getValue());
        }
        return sum;
    }

}
