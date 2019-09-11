package com.jstarcraft.ai.model.neuralnetwork.parameter;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class IgnoreParameterFactory implements ParameterFactory {

    @Override
    public void setValues(MathMatrix matrix) {
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
            return true;
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "IgnoreParameterFactory()";
    }

}
