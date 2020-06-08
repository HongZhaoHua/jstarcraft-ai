package com.jstarcraft.ai.math.algorithm.correlation.distance;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistanceTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.MathCorrelation;

public class MSDDistanceTestCase extends AbstractDistanceTestCase {

    // TODO 注意MSD与MSE相似度是计算两个向量的均方误差,范围是0-正无穷.且if (row == column) value = 0D;
    @Override
    protected MathCorrelation getCorrelation() {
        return new MSDDistance();
    }

}
