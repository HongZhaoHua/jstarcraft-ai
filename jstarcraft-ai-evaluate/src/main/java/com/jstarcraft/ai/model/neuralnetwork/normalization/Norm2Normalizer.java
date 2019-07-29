package com.jstarcraft.ai.model.neuralnetwork.normalization;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

public class Norm2Normalizer implements Normalizer {

	private final Mode mode;

	public Norm2Normalizer(Mode mode) {
		this.mode = mode;
	}

	@Override
	public void normalize(Map<String, MathMatrix> gradients) {
		switch (mode) {
		case GLOBAL: {
			float norm = 0F;
			for (MathMatrix gradient : gradients.values()) {
				for (MatrixScalar term : gradient) {
					norm += term.getValue() * term.getValue();
				}
			}
			norm = (float) (1F / FastMath.sqrt(norm));
			for (MathMatrix gradient : gradients.values()) {
				gradient.scaleValues(norm);
			}
			break;
		}

		case LOCAL: {
			for (MathMatrix gradient : gradients.values()) {
				float norm = 0F;
				for (MatrixScalar term : gradient) {
					norm += term.getValue() * term.getValue();
				}
				norm = (float) (1F / FastMath.sqrt(norm));
				gradient.scaleValues(norm);
			}
			break;
		}
		}
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
			Norm2Normalizer that = (Norm2Normalizer) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.mode, that.mode);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(mode);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "Norm2Normalizer(mode=" + mode + ")";
	}

}
