package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.MathSimilarity;
import com.jstarcraft.ai.math.algorithm.kernel.KernelTrick;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 核相似度
 * 
 * @author Birdy
 *
 */
public class KernelSimilarity implements MathSimilarity {

    private KernelTrick kernel;

    public KernelSimilarity(KernelTrick kernel) {
        this.kernel = kernel;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        float leftCoefficient = kernel.calculate(leftVector, leftVector);
        float rightCoefficient = kernel.calculate(rightVector, rightVector);
        if (leftCoefficient == 0F || rightCoefficient == 0F) {
            return 0F;
        } else {
            return kernel.calculate(leftVector, rightVector) / (float) FastMath.sqrt(leftCoefficient * rightCoefficient);
        }
    }

}
