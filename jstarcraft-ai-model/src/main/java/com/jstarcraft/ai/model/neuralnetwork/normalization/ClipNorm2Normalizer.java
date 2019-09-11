package com.jstarcraft.ai.model.neuralnetwork.normalization;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

public class ClipNorm2Normalizer implements Normalizer {

    private final Mode mode;

    private final float threshold;

    public ClipNorm2Normalizer(Mode mode, float threshold) {
        this.mode = mode;
        this.threshold = threshold;
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
            if (norm > threshold) {
                norm = threshold / norm;
                for (MathMatrix gradient : gradients.values()) {
                    gradient.scaleValues(norm);
                }
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
                norm = (float) (1F / FastMath.sqrt(norm));
                if (norm > threshold) {
                    norm = threshold / norm;
                    gradient.scaleValues(norm);
                }

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
            ClipNorm2Normalizer that = (ClipNorm2Normalizer) object;
            EqualsBuilder equal = new EqualsBuilder();
            equal.append(this.mode, that.mode);
            equal.append(this.threshold, that.threshold);
            return equal.isEquals();
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(mode);
        hash.append(threshold);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        return "ClipNorm2Normalizer(mode=" + mode + ", threshold=" + threshold + ")";
    }

}
