/**
 * %SVN.HEADER%
 */
package com.jstarcraft.ai.math.algorithm.correlation.distance;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractCorrelation;
import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * 范数距离
 * 
 * <pre>
 * 闵可夫斯基距离(Minkowski Distance)
 * </pre>
 * 
 * This class implements the Norm distance. This is a generalization of the
 * Euclidean distance, in this respect that the power we use becomes a parameter
 * instead of being fixed to two.
 * 
 * The x-Norm distance between two points P=(p1,p2,...,pn) and Q=(q1,q2,...,qn)
 * in the Euclidean n-space is defined as: ((p1-q1)^x + (p2-q2)^x + ... +
 * (pn-qn)^x)^(1/x)
 * 
 * Special instances are x=1, the Manhattan- or taxicab norm. Or x=infinity
 * gives the Chebychev distance.
 * 
 * The default is the Euclidean distance where x=2.
 * 
 * @linkplain http://en.wikipedia.org/wiki/Norm_%28mathematics%29
 * 
 * @author Birdy
 * 
 */
public class NormDistance extends AbstractCorrelation implements MathDistance {

    private float power;

    /** 是否执行根号运算 */
    private boolean root;

    public NormDistance(float power, boolean root) {
        this.power = power;
        this.root = root;
    }

    private float getCoefficient(List<Float2FloatKeyValue> scores) {
        // TODO 此处对称矩阵可能会存在错误,需要Override
        // 处理power为0的情况
        if (power == 0F) {
            return scores.size();
        } else {
            float norm = 0F;
            if (power == 1F) {
                for (Float2FloatKeyValue term : scores) {
                    float distance = term.getKey() - term.getValue();
                    norm += FastMath.abs(distance);
                }
                return norm;
            }
            if (power == 2F) {
                for (Float2FloatKeyValue term : scores) {
                    float distance = term.getKey() - term.getValue();
                    norm += distance * distance;
                }
                return (float) Math.sqrt(norm);
            }
            // 处理power为2的倍数次方的情况
            if ((int) power == power && power % 2F == 0F) {
                for (Float2FloatKeyValue term : scores) {
                    float distance = term.getKey() - term.getValue();
                    norm += FastMath.pow(distance, power);
                }
            } else {
                for (Float2FloatKeyValue term : scores) {
                    float distance = term.getKey() - term.getValue();
                    norm += FastMath.pow(FastMath.abs(distance), power);
                }
            }
            return root ? (float) FastMath.pow(norm, 1F / power) : norm;
        }
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scores = getIntersectionScores(leftVector, rightVector);
        int intersection = scores.size();
        if (intersection == 0) {
            return Float.POSITIVE_INFINITY;
        }
        int union = leftVector.getElementSize() + rightVector.getElementSize() - intersection;
        float coefficient = getCoefficient(scores);
        coefficient *= union;
        coefficient /= intersection;
        return coefficient;
    }

}
