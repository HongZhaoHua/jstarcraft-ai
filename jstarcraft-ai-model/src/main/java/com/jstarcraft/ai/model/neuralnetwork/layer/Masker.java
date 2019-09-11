package com.jstarcraft.ai.model.neuralnetwork.layer;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 掩盖器(本质为Dropout)
 * 
 * <pre>
 * 说明
 * Note 1: As per all IDropout instances, dropout is applied at training time only - and is automatically not applied at test time (for evaluation, etc)
 * Note 2: Care should be taken when setting lower (probability of retaining) values for (too much information may be lost with aggressive (very low) dropout values).
 * Note 3: Frequently, dropout is not applied to (or, has higher retain probability for) input (first layer) layers. Dropout is also often not applied to output layers.
 * Note 4: Implementation detail (most users can ignore): DL4J uses inverted dropout.
 * </pre>
 * 
 * @author Birdy
 *
 */
public interface Masker {

    /**
     * 掩盖
     * 
     * @param middleData
     * @param iteration
     * @param epoch
     */
    void mask(MathMatrix middleData, int iteration, int epoch);

}
