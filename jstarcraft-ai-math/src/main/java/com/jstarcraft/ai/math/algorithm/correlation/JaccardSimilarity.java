package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.Iterator;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * Jaccard相似度
 * 
 * @author Birdy
 *
 */
public class JaccardSimilarity extends AbstractSimilarity {

	@Override
	public float getCoefficient(MathVector leftVector, MathVector rightVector, float scale) {
		// compute similarity
		int intersection = 0;
		int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
		if (leftSize != 0 && rightSize != 0) {
			Iterator<VectorScalar> leftIterator = leftVector.iterator();
			Iterator<VectorScalar> rightIterator = rightVector.iterator();
			VectorScalar leftTerm = leftIterator.next();
			VectorScalar rightTerm = rightIterator.next();
			// 判断两个有序数组中是否存在相同的数字
			while (leftIndex < leftSize && rightIndex < rightSize) {
				if (leftTerm.getIndex() == rightTerm.getIndex()) {
					intersection++;
					leftTerm = leftIterator.next();
					rightTerm = rightIterator.next();
					leftIndex++;
					rightIndex++;
				} else if (leftTerm.getIndex() > rightTerm.getIndex()) {
					rightTerm = rightIterator.next();
					rightIndex++;
				} else if (leftTerm.getIndex() < rightTerm.getIndex()) {
					leftTerm = leftIterator.next();
					leftIndex++;
				}
			}
		}
		float union = leftSize + rightSize - intersection;
		return (intersection) / union;
	}

}
