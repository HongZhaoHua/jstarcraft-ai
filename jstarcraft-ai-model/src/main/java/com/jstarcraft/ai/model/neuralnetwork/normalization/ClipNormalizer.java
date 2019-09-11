package com.jstarcraft.ai.model.neuralnetwork.normalization;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class ClipNormalizer implements Normalizer {

    private final float threshold;

    public ClipNormalizer(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public void normalize(Map<String, MathMatrix> gradients) {
        for (MathMatrix gradient : gradients.values()) {
            gradient.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
                float value = scalar.getValue();
                value = value < -threshold ? -threshold : (value > threshold ? threshold : value);
                scalar.scaleValue(value);
            });
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
            ClipNormalizer that = (ClipNormalizer) object;
            EqualsBuilder equal = new EqualsBuilder();
            equal.append(this.threshold, that.threshold);
            return equal.isEquals();
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(threshold);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        return "ClipNormalizer(threshold=" + threshold + ")";
    }

}
