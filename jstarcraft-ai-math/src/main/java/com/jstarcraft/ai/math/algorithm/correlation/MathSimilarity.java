package com.jstarcraft.ai.math.algorithm.correlation;

/**
 * 抽象相似度
 * 
 * @author Birdy
 *
 */
public interface MathSimilarity extends MathCorrelation {

    @Override
    default float getIdentical() {
        return 1F;
    }

}
