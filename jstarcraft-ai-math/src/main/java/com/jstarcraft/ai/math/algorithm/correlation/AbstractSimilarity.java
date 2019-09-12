package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

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

    /**
     * 相似度计算使用交集
     */
    @Override
    protected List<Float2FloatKeyValue> getScoreList(MathVector leftVector, MathVector rightVector) {
        LinkedList<Float2FloatKeyValue> scoreList = new LinkedList<>();
        int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
        if (leftSize != 0 && rightSize != 0) {
            Iterator<VectorScalar> leftIterator = leftVector.iterator();
            Iterator<VectorScalar> rightIterator = rightVector.iterator();
            VectorScalar leftTerm = leftIterator.next();
            VectorScalar rightTerm = rightIterator.next();
            // 判断两个有序数组中是否存在相同的数字
            while (leftCursor < leftSize && rightCursor < rightSize) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    scoreList.add(new Float2FloatKeyValue(leftTerm.getValue(), rightTerm.getValue()));
                    leftTerm = leftIterator.next();
                    rightTerm = rightIterator.next();
                    leftCursor++;
                    rightCursor++;
                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
                    rightTerm = rightIterator.next();
                    rightCursor++;
                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
                    leftTerm = leftIterator.next();
                    leftCursor++;
                }
            }
        }
        return scoreList;
    }

}
