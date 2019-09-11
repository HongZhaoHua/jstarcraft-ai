package com.jstarcraft.ai.model.neuralnetwork.learn;

import java.util.Map;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 忽略学习器
 * 
 * @author Birdy
 *
 */
public class IgnoreLearner implements Learner {

    @Override
    public void doCache(Map<String, MathMatrix> gradients) {
    }

    @Override
    public void learn(Map<String, MathMatrix> gradients, int iteration, int epoch) {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "IgnoreLearner()";
    }

}
