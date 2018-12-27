package com.jstarcraft.ai.neuralnetwork.loss;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.KeyValue;

/**
 * F Measure目标函数
 * 
 * <pre>
 * https://link.springer.com/chapter/10.1007/978-3-642-38679-4_37
 * </pre>
 * 
 * @author Birdy
 *
 */
public class FMeasureLossFunction implements LossFunction {

	public static final float DEFAULT_BETA = 1F;

	private final float beta;

	public FMeasureLossFunction() {
		this(DEFAULT_BETA);
	}

	public FMeasureLossFunction(float beta) {
		if (beta <= 0) {
			throw new UnsupportedOperationException("Invalid value: beta must be > 0. Got: " + beta);
		}
		this.beta = beta;
	}

	private KeyValue<Float, Float> computeNumeratorWithDenominator(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		int size = tests.getColumnSize();
		if (size != 1 && size != 2) {
			throw new UnsupportedOperationException("For binary classification: expect output size of 1 or 2. Got: " + size);
		}
		// First: determine positives and negatives
		float tp = 0F;
		float fp = 0F;
		float fn = 0F;

		float isPositiveLabel;
		float isNegativeLabel;
		float pClass0;
		float pClass1;
		if (size == 1) {
			for (int row = 0; row < trains.getRowSize(); row++) {
				if (tests.getValue(row, 0) != 0F) {
					isPositiveLabel = 1F;
					isNegativeLabel = 0F;
				} else {
					isPositiveLabel = 0F;
					isNegativeLabel = 1F;
				}

				float score = trains.getValue(row, 0);
				pClass0 = 1F - score;
				pClass1 = score;

				tp += isPositiveLabel * pClass1;
				fp += isNegativeLabel * pClass1;
				fn += isPositiveLabel * pClass0;
			}
		} else {
			for (int row = 0; row < trains.getRowSize(); row++) {
				isPositiveLabel = tests.getValue(row, 1);
				isNegativeLabel = tests.getValue(row, 0);
				pClass0 = trains.getValue(row, 0);
				pClass1 = trains.getValue(row, 1);

				tp += isPositiveLabel * pClass1;
				fp += isNegativeLabel * pClass1;
				fn += isPositiveLabel * pClass0;
			}
		}

		float numerator = (1F + beta * beta) * tp;
		float denominator = (1F + beta * beta) * tp + beta * beta * fn + fp;

		return new KeyValue<Float, Float>(numerator, denominator);
	}

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		KeyValue<Float, Float> keyValue = computeNumeratorWithDenominator(tests, trains, masks);
		float numerator = keyValue.getKey();
		float denominator = keyValue.getValue();
		if (numerator == 0F && denominator == 0F) {
			return 0F;
		}
		return 1F - numerator / denominator;
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		KeyValue<Float, Float> keyValue = computeNumeratorWithDenominator(tests, trains, masks);
		float numerator = keyValue.getKey();
		float denominator = keyValue.getValue();
		if (numerator == 0F && denominator == 0F) {
			// Zero score -> zero gradient
			gradients.setValues(0F);
			return;
		}

		float secondTerm = numerator / (denominator * denominator);
		// TODO 避免重复分配内存
		int size = tests.getColumnSize();
		if (size == 1) {
			// Single binary output case
			gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = tests.getValue(row, column) * (1F + beta * beta) / denominator - secondTerm;
				scalar.setValue(-value);
			});
		} else {
			// Softmax case: the getColumn(1) here is to account for the fact
			// that we're using prob(class1)
			// only in the score function; column(1) is equivalent to output for
			// the single output case
			MathVector vector = gradients.getColumnVector(1);
			MathVector label = tests.getColumnVector(1);
			vector.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int index = scalar.getIndex();
				float value = label.getValue(index) * (1F + beta * beta) / denominator - secondTerm;
				scalar.setValue(-value);
			});
		}

		// Negate relative to description in paper, as we want to *minimize*
		// 1.0-fMeasure, which is equivalent to
		// maximizing fMeasure
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
			FMeasureLossFunction that = (FMeasureLossFunction) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.beta, that.beta);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(beta);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "FMeasureLossFunction(beta=" + beta + ")";
	}

}
