package com.jstarcraft.ai.math.structure;

import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class DenseCache implements MathCache {

    @Override
    public MathMatrix makeMatrix(int rowSize, int columnSize) {
        DenseMatrix matrix = DenseMatrix.valueOf(rowSize, columnSize);
        return matrix;
    }

    @Override
    public MathVector makeVector(int capacitySize) {
        DenseVector vector = DenseVector.valueOf(capacitySize);
        return vector;
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
        return "DenseMatrixFactory()";
    }

}
