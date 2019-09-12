package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * 抽象距离
 * 
 * @author Birdy
 *
 */
public abstract class AbstractDistance extends AbstractCorrelation {

    @Override
    public float getIdentical() {
        return 0F;
    }

    /**
     * 距离计算使用并集
     */
    @Override
    protected List<Float2FloatKeyValue> getScoreList(MathVector leftVector, MathVector rightVector) {
        LinkedList<Float2FloatKeyValue> scoreList = new LinkedList<>();
        Iterator<VectorScalar> leftIterator = leftVector.iterator();
        Iterator<VectorScalar> rightIterator = rightVector.iterator();
        VectorScalar leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
        VectorScalar rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
        // 判断两个有序数组中是否存在相同的数字
        while (leftTerm != null || rightTerm != null) {
            if (leftTerm != null && rightTerm != null) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    scoreList.add(new Float2FloatKeyValue(leftTerm.getValue(), rightTerm.getValue()));
                    leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
                    rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
                    scoreList.add(new Float2FloatKeyValue(0F, rightTerm.getValue()));
                    rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
                    scoreList.add(new Float2FloatKeyValue(leftTerm.getValue(), 0F));
                    leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
                }
                continue;
            }
            if (leftTerm != null) {
                scoreList.add(new Float2FloatKeyValue(leftTerm.getValue(), 0F));
                leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
                continue;
            }
            if (rightTerm != null) {
                scoreList.add(new Float2FloatKeyValue(0F, rightTerm.getValue()));
                rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
                continue;
            }
        }
        return scoreList;
    }

}
