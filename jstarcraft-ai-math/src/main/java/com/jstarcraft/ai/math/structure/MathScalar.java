package com.jstarcraft.ai.math.structure;

import java.util.Iterator;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.Nd4jVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * 数学标量
 * 
 * @author Birdy
 *
 */
public interface MathScalar {

	/**
	 * 获取标量的值
	 * 
	 * @return
	 */
	float getValue();

	/**
	 * 缩放标量的值
	 * 
	 * @param value
	 */
	void scaleValue(float value);

	/**
	 * 设置标量的值
	 * 
	 * @param value
	 */
	void setValue(float value);

	/**
	 * 偏移标量的值
	 * 
	 * @param value
	 */
	void shiftValue(float value);

	/**
	 * 标量点积运算
	 * 
	 * @param leftVector
	 * @param rightVector
	 */
	default MathScalar dotProduct(MathVector leftVector, MathVector rightVector) {
		Float one = 1F;
		float zero = 0F;
		float value = zero;

		if (leftVector instanceof Nd4jVector && rightVector instanceof Nd4jVector) {
			int size = rightVector.getElementSize();
			INDArray leftArray = Nd4jVector.class.cast(leftVector).getArray();
			INDArray rightArray = Nd4jVector.class.cast(rightVector).getArray();
			value = (float) Nd4j.getBlasWrapper().level1().dot(size, one, leftArray, rightArray);
		} else if (leftVector.isConstant() && rightVector.isConstant()) {
			for (int position = 0, size = leftVector.getElementSize(); position < size; position++) {
				value += leftVector.getValue(position) * rightVector.getValue(position);
			}
		} else if (!leftVector.isConstant() && !rightVector.isConstant()) {
			int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
			if (leftSize != 0 && rightSize != 0) {
				Iterator<VectorScalar> leftIterator = leftVector.iterator();
				Iterator<VectorScalar> rightIterator = rightVector.iterator();
				VectorScalar leftTerm = leftIterator.next();
				VectorScalar rightTerm = rightIterator.next();
				// 判断两个有序数组中是否存在相同的数字
				while (leftCursor < leftSize && rightCursor < rightSize) {
					if (leftTerm.getIndex() == rightTerm.getIndex()) {
						value += leftTerm.getValue() * rightTerm.getValue();
						if (leftIterator.hasNext()) {
							leftTerm = leftIterator.next();
						}
						if (rightIterator.hasNext()) {
							rightTerm = rightIterator.next();
						}
						leftCursor++;
						rightCursor++;
					} else if (leftTerm.getIndex() > rightTerm.getIndex()) {
						if (rightIterator.hasNext()) {
							rightTerm = rightIterator.next();
						}
						rightCursor++;
					} else if (leftTerm.getIndex() < rightTerm.getIndex()) {
						if (leftIterator.hasNext()) {
							leftTerm = leftIterator.next();
						}
						leftCursor++;
					}
				}
			}
		} else if (!leftVector.isConstant()) {
			for (VectorScalar term : leftVector) {
				value += term.getValue() * rightVector.getValue(term.getIndex());
			}
		} else if (!rightVector.isConstant()) {
			for (VectorScalar term : rightVector) {
				value += leftVector.getValue(term.getIndex()) * term.getValue();
			}
		}

		setValue(value);
		return this;
	}

	/**
	 * 标量累积运算
	 * 
	 * @param leftVector
	 * @param rightVector
	 */
	default MathScalar accumulateProduct(MathVector leftVector, MathVector rightVector) {
		Float one = 1F;
		float zero = 0F;
		float value = zero;

		if (leftVector instanceof Nd4jVector && rightVector instanceof Nd4jVector) {
			int size = rightVector.getElementSize();
			INDArray leftArray = Nd4jVector.class.cast(leftVector).getArray();
			INDArray rightArray = Nd4jVector.class.cast(rightVector).getArray();
			value = (float) Nd4j.getBlasWrapper().level1().dot(size, one, leftArray, rightArray);
		} else if (leftVector.isConstant() && rightVector.isConstant()) {
			for (int position = 0, size = leftVector.getElementSize(); position < size; position++) {
				value += leftVector.getValue(position) * rightVector.getValue(position);
			}
		} else if (!leftVector.isConstant() && !rightVector.isConstant()) {
			int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
			if (leftSize != 0 && rightSize != 0) {
				Iterator<VectorScalar> leftIterator = leftVector.iterator();
				Iterator<VectorScalar> rightIterator = rightVector.iterator();
				VectorScalar leftTerm = leftIterator.next();
				VectorScalar rightTerm = rightIterator.next();
				// 判断两个有序数组中是否存在相同的数字
				while (leftCursor < leftSize && rightCursor < rightSize) {
					if (leftTerm.getIndex() == rightTerm.getIndex()) {
						value += leftTerm.getValue() * rightTerm.getValue();
						if (leftIterator.hasNext()) {
							leftTerm = leftIterator.next();
						}
						if (rightIterator.hasNext()) {
							rightTerm = rightIterator.next();
						}
						leftCursor++;
						rightCursor++;
					} else if (leftTerm.getIndex() > rightTerm.getIndex()) {
						if (rightIterator.hasNext()) {
							rightTerm = rightIterator.next();
						}
						rightCursor++;
					} else if (leftTerm.getIndex() < rightTerm.getIndex()) {
						if (leftIterator.hasNext()) {
							leftTerm = leftIterator.next();
						}
						leftCursor++;
					}
				}
			}
		} else if (!leftVector.isConstant()) {
			for (VectorScalar term : leftVector) {
				value += term.getValue() * rightVector.getValue(term.getIndex());
			}
		} else if (!rightVector.isConstant()) {
			for (VectorScalar term : rightVector) {
				value += leftVector.getValue(term.getIndex()) * term.getValue();
			}
		}

		shiftValue(value);
		return this;
	}

}
