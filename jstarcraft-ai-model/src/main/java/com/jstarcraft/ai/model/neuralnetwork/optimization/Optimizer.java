package com.jstarcraft.ai.model.neuralnetwork.optimization;

import java.util.Map;
import java.util.concurrent.Callable;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.step.StepFunction;

/**
 * 优化器
 * 
 * @author Birdy
 *
 */
public interface Optimizer {

    StepFunction getFunction();

    void doCache(Callable<Float> scorer, Map<String, MathMatrix> gradients, Map<String, MathMatrix> parameters);

    /**
     * Calls optimize
     * 
     * @return whether the convex optimizer converted or not
     */
    boolean optimize(float score);

}
