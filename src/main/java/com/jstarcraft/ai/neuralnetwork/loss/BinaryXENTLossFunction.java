package com.jstarcraft.ai.neuralnetwork.loss;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

/**
 * Binomial XENT目标函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 *
 * https://en.wikipedia.org/wiki/Cross_entropy#Cross-entropy_error_function_and_logistic_regression
 * </pre>
 * 
 * @author Birdy
 *
 */
public class BinaryXENTLossFunction implements LossFunction {

	public static final float DEFAULT_CLIP = 1E-5F;

	private float clip;

	private boolean isSoftMaximum;

	BinaryXENTLossFunction() {
	}

	public BinaryXENTLossFunction(boolean isSoftMaximum) {
		this(DEFAULT_CLIP, isSoftMaximum);
	}

	/**
	 * Binary cross entropy where each the output is (optionally) weighted/scaled by
	 * a fixed scalar value. Note that the weights array must be a row vector, of
	 * length equal to the labels/output dimension 1 size. A weight vector of 1s
	 * should give identical results to no weight vector.
	 *
	 * @param clip
	 *            Epsilon value for clipping. Probabilities are clipped in range of
	 *            [eps, 1-eps]. Default eps: 1e-5
	 * @param weights
	 *            Weights array (row vector). May be null.
	 */
	public BinaryXENTLossFunction(float clip, boolean isSoftMaximum) {
		if (clip < 0F || clip > 0.5F) {
			throw new IllegalArgumentException("Invalid clipping epsilon value: epsilon should be >= 0 (but near zero)." + "Got: " + clip);
		}
		this.clip = clip;
		this.isSoftMaximum = isSoftMaximum;
	}

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		float score = 0f;
		if (isSoftMaximum) {
			// TODO 此处需要测试(Log(SoftMax(X)))
			for (MatrixScalar term : trains) {
				score += -(FastMath.log(term.getValue()) * tests.getValue(term.getRow(), term.getColumn()));
			}
		} else {
			float minimum = clip;
			double maximum = 1D - clip;
			for (MatrixScalar term : trains) {
				double value = term.getValue();
				value = value < minimum ? minimum : (value > maximum ? maximum : value);
				double left = FastMath.log(value) * tests.getValue(term.getRow(), term.getColumn());
				double right = FastMath.log(1D - value) * (1D - tests.getValue(term.getRow(), term.getColumn()));
				score += -(left + right);
			}
		}
		// TODO 暂时不处理masks
		return score;
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		float minimum = clip;
		float maximum = 1F - clip;
		gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = trains.getValue(row, column);
			value = value < minimum ? minimum : (value > maximum ? maximum : value);
			float numerator = value - tests.getValue(row, column);
			float denominator = value * (1F - value);
			scalar.setValue(numerator / denominator);
		});
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
			BinaryXENTLossFunction that = (BinaryXENTLossFunction) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.clip, that.clip);
			equal.append(this.isSoftMaximum, that.isSoftMaximum);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(clip);
		hash.append(isSoftMaximum);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "BinaryXENTLossFunction(clip=" + clip + ", isSoftMaximum=" + isSoftMaximum + ")";
	}

}
