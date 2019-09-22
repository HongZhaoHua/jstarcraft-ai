package com.jstarcraft.ai.model.neuralnetwork.condition;

import java.util.Map;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * Epsilon条件
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class EpsilonCondition implements Condition {

    private float epsilon = MathUtility.EPSILON;

    private float tolerance = MathUtility.EPSILON;

    public EpsilonCondition() {
    }

    public EpsilonCondition(float epsilon, float tolerance) {
        this.epsilon = epsilon;
        this.tolerance = tolerance;
    }

    @Override
    public boolean stop(float newScore, float oldScore, Map<String, MathMatrix> gradients) {
        // special case for initial termination, ignore
        if (newScore == 0F && oldScore == 0F) {
            return false;
        }

        float oldAbsolute = FastMath.abs(oldScore);
        float newAbsolute = FastMath.abs(newScore);

        return 2D * FastMath.abs(oldScore - newScore) <= tolerance * (oldAbsolute + newAbsolute + epsilon);
    }

}
