package com.jstarcraft.ai.model.neuralnetwork.loss;

import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * Cosine Proximity目标函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class CosineProximityLossFunction implements LossFunction {

    @Override
    public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
        /*
         * mean of -(y.dot(yhat)/||y||*||yhat||)
         */
        float score = 0F;
        for (int row = 0; row < trains.getRowSize(); row++) {
            MathVector vector = trains.getRowVector(row);
            MathVector mark = tests.getRowVector(row);
            float scoreNorm = Math.max(vector.getNorm(2), MathUtility.EPSILON);
            float markNorm = Math.max(mark.getNorm(2), MathUtility.EPSILON);
            for (VectorScalar term : vector) {
                score += -(term.getValue() * mark.getValue(term.getIndex()) / scoreNorm / markNorm);
            }
        }
        // TODO 暂时不处理masks
        return score;
    }

    @Override
    public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
        for (int row = 0; row < trains.getRowSize(); row++) {
            MathVector vector = trains.getRowVector(row);
            MathVector mark = tests.getRowVector(row);
            float scoreNorm = vector.getNorm(2F);
            float markNorm = mark.getNorm(2F);
            float squareNorm = scoreNorm * scoreNorm;
            float sum = 0F;
            for (VectorScalar term : vector) {
                sum += term.getValue() * mark.getValue(term.getIndex());
            }

            markNorm = Math.max(markNorm, MathUtility.EPSILON);
            scoreNorm = Math.max(scoreNorm, MathUtility.EPSILON);
            squareNorm = Math.max(squareNorm, MathUtility.EPSILON);
            for (VectorScalar term : vector) {
                float value = term.getValue();
                value = mark.getValue(term.getIndex()) * squareNorm - value * sum;
                value /= (markNorm * scoreNorm * squareNorm);
                gradients.setValue(row, term.getIndex(), -value);
            }
        }
        // TODO 暂时不处理masks
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
        return "CosineProximityLossFunction()";
    }

}
