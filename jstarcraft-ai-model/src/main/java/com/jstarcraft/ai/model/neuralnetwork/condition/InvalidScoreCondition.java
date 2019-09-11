package com.jstarcraft.ai.model.neuralnetwork.condition;

import java.util.Map;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 无效分数条件
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class InvalidScoreCondition implements Condition {

    @Override
    public boolean stop(double newScore, double oldScore, Map<String, MathMatrix> gradients) {
        return Double.isNaN(newScore) || Double.isInfinite(newScore);
    }

}
