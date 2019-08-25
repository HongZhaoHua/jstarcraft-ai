package com.jstarcraft.ai.math.algorithm.correlation;

/**
 * 抽象相似度
 * 
 * @author Birdy
 *
 */
public abstract class AbstractSimilarity extends AbstractCorrelation {

    @Override
    public float getIdentical() {
        return 1F;
    }

}
