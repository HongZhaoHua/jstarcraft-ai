package com.jstarcraft.ai.math.structure.matrix;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.vector.DenseVector;

/**
 * 稠密矩阵
 * 
 * @author Birdy
 *
 */
public class DenseMatrix implements MathMatrix {

    /** 大小 */
    private int rowSize, columnSize;

    /** 数据 */
    private float[] values;

    private DenseMatrix() {
    }

    private DenseMatrix(int rowSize, int columnSize, float[] values) {
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.values = values;
    }

    @Override
    public int getElementSize() {
        return rowSize * columnSize;
    }

    @Override
    public int getKnownSize() {
        return getElementSize();
    }

    @Override
    public int getUnknownSize() {
        return 0;
    }

    @Override
    public ScalarIterator<MatrixScalar> iterateElement(MathCalculator mode, MathAccessor<MatrixScalar>... accessors) {
        switch (mode) {
        case SERIAL: {
            DenseMatrixScalar scalar = new DenseMatrixScalar();
            for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
                for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
                    int cursor = rowIndex * columnSize + columnIndex;
                    scalar.update(cursor);
                    for (MathAccessor<MatrixScalar> accessor : accessors) {
                        accessor.accessElement(scalar);
                    }
                }
            }
            return this;
        }
        default: {
            if (this.getColumnSize() <= this.getRowSize()) {
                int size = columnSize;
                EnvironmentContext context = EnvironmentContext.getContext();
                Semaphore semaphore = MathCalculator.getSemaphore();
                for (int index = 0; index < size; index++) {
                    int columnIndex = index;
                    context.doStructureByAny(index, () -> {
                        DenseMatrixScalar scalar = new DenseMatrixScalar();
                        for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
                            int cursor = rowIndex * columnSize + columnIndex;
                            scalar.update(cursor);
                            for (MathAccessor<MatrixScalar> accessor : accessors) {
                                accessor.accessElement(scalar);
                            }
                        }
                        semaphore.release();
                    });
                }
                try {
                    semaphore.acquire(size);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
                return this;
            } else {
                int size = rowSize;
                EnvironmentContext context = EnvironmentContext.getContext();
                Semaphore semaphore = MathCalculator.getSemaphore();
                for (int index = 0; index < size; index++) {
                    int rowIndex = index;
                    context.doStructureByAny(index, () -> {
                        DenseMatrixScalar scalar = new DenseMatrixScalar();
                        for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
                            int cursor = rowIndex * columnSize + columnIndex;
                            scalar.update(cursor);
                            for (MathAccessor<MatrixScalar> accessor : accessors) {
                                accessor.accessElement(scalar);
                            }
                        }
                        semaphore.release();
                    });
                }
                try {
                    semaphore.acquire(size);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
                return this;
            }
        }
        }
    }

    @Override
    public DenseMatrix setValues(float value) {
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                values[row * columnSize + column] = value;
            }
        }
        return this;
    }

    @Override
    public DenseMatrix scaleValues(float value) {
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                values[row * columnSize + column] *= value;
            }
        }
        return this;
    }

    @Override
    public DenseMatrix shiftValues(float value) {
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                values[row * columnSize + column] += value;
            }
        }
        return this;
    }

    @Override
    public float getSum(boolean absolute) {
        float sum = 0F;
        if (absolute) {
            for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
                for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
                    sum += FastMath.abs(values[rowIndex * columnSize + columnIndex]);
                }
            }
        } else {
            for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
                for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
                    sum += values[rowIndex * columnSize + columnIndex];
                }
            }
        }
        return sum;
    }

    @Override
    public int getRowSize() {
        return rowSize;
    }

    @Override
    public int getColumnSize() {
        return columnSize;
    }

    @Override
    public DenseVector getRowVector(int rowIndex) {
        return new DenseVector(values, rowIndex * columnSize, 1, columnSize);
    }

    @Override
    public DenseVector getColumnVector(int columnIndex) {
        return new DenseVector(values, columnIndex, columnSize, rowSize);
    }

    @Override
    public boolean isIndexed() {
        return true;
    }

    @Override
    public float getValue(int rowIndex, int columnIndex) {
        return values[rowIndex * columnSize + columnIndex];
    }

    @Override
    public void setValue(int rowIndex, int columnIndex, float value) {
        values[rowIndex * columnSize + columnIndex] = value;
    }

    @Override
    public void scaleValue(int rowIndex, int columnIndex, float value) {
        values[rowIndex * columnSize + columnIndex] *= value;
    }

    @Override
    public void shiftValue(int rowIndex, int columnIndex, float value) {
        values[rowIndex * columnSize + columnIndex] += value;
    }

    @Override
    public MathMatrix addMatrix(MathMatrix matrix, boolean transpose) {
        if (matrix instanceof DenseMatrix) {
            DenseMatrix that = DenseMatrix.class.cast(matrix);
            if (transpose) {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.rowSize; column++) {
                        this.values[row * this.columnSize + column] += that.values[column * that.columnSize + row];
                    }
                }
            } else {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.columnSize; column++) {
                        this.values[row * this.columnSize + column] += that.values[row * that.columnSize + column];
                    }
                }
            }
            return this;
        } else {
            return MathMatrix.super.addMatrix(matrix, transpose);
        }
    }

    @Override
    public MathMatrix subtractMatrix(MathMatrix matrix, boolean transpose) {
        if (matrix instanceof DenseMatrix) {
            DenseMatrix that = DenseMatrix.class.cast(matrix);
            if (transpose) {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.rowSize; column++) {
                        this.values[row * this.columnSize + column] -= that.values[column * that.columnSize + row];
                    }
                }
            } else {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.columnSize; column++) {
                        this.values[row * this.columnSize + column] -= that.values[row * that.columnSize + column];
                    }
                }
            }
            return this;
        } else {
            return MathMatrix.super.subtractMatrix(matrix, transpose);
        }
    }

    @Override
    public MathMatrix multiplyMatrix(MathMatrix matrix, boolean transpose) {
        if (matrix instanceof DenseMatrix) {
            DenseMatrix that = DenseMatrix.class.cast(matrix);
            if (transpose) {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.rowSize; column++) {
                        this.values[row * this.columnSize + column] *= that.values[column * that.columnSize + row];
                    }
                }
            } else {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.columnSize; column++) {
                        this.values[row * this.columnSize + column] *= that.values[row * that.columnSize + column];
                    }
                }
            }
            return this;
        } else {
            return MathMatrix.super.multiplyMatrix(matrix, transpose);
        }
    }

    @Override
    public MathMatrix divideMatrix(MathMatrix matrix, boolean transpose) {
        if (matrix instanceof DenseMatrix) {
            DenseMatrix that = DenseMatrix.class.cast(matrix);
            if (transpose) {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.rowSize; column++) {
                        this.values[row * this.columnSize + column] /= that.values[column * that.columnSize + row];
                    }
                }
            } else {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.columnSize; column++) {
                        this.values[row * this.columnSize + column] /= that.values[row * that.columnSize + column];
                    }
                }
            }
            return this;
        } else {
            return MathMatrix.super.addMatrix(matrix, transpose);
        }
    }

    @Override
    public MathMatrix copyMatrix(MathMatrix matrix, boolean transpose) {
        if (matrix instanceof DenseMatrix) {
            DenseMatrix that = DenseMatrix.class.cast(matrix);
            if (transpose) {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.rowSize; column++) {
                        this.values[row * this.columnSize + column] = that.values[column * that.columnSize + row];
                    }
                }
            } else {
                for (int row = 0; row < this.rowSize; row++) {
                    for (int column = 0; column < that.columnSize; column++) {
                        this.values[row * this.columnSize + column] = that.values[row * that.columnSize + column];
                    }
                }
            }
            return this;
        } else {
            return MathMatrix.super.addMatrix(matrix, transpose);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        DenseMatrix that = (DenseMatrix) object;
        EqualsBuilder equal = new EqualsBuilder();
        equal.append(this.values, that.values);
        return equal.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(values);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                buffer.append(getValue(row, column)).append(", ");
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }

    @Override
    public Iterator<MatrixScalar> iterator() {
        return new DenseMatrixIterator();
    }

    private class DenseMatrixIterator implements Iterator<MatrixScalar> {

        private int size = rowSize * columnSize;

        private int cursor;

        private DenseMatrixScalar term = new DenseMatrixScalar();

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public MatrixScalar next() {
            term.update(cursor++);
            return term;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class DenseMatrixScalar implements MatrixScalar {

        private int index, row, column;

        private void update(int index) {
            this.index = index;
        }

        @Override
        public int getRow() {
            return index / columnSize;
        }

        @Override
        public int getColumn() {
            return index % columnSize;
        }

        @Override
        public float getValue() {
            return values[index];
        }

        @Override
        public void scaleValue(float value) {
            values[index] *= value;
        }

        @Override
        public void setValue(float value) {
            values[index] = value;
        }

        @Override
        public void shiftValue(float value) {
            values[index] += value;
        }

    }

    public static DenseMatrix copyOf(DenseMatrix matrix) {
        float[] data = new float[matrix.rowSize * matrix.columnSize];
        DenseMatrix instance = new DenseMatrix(matrix.rowSize, matrix.columnSize, data);
        for (int row = 0; row < matrix.rowSize; row++) {
            for (int column = 0; column < matrix.columnSize; column++) {
                int index = row * matrix.columnSize + column;
                data[index] = matrix.getValue(row, column);
            }
        }
        instance.values = data;
        return instance;
    }

    public static DenseMatrix valueOf(int rowSize, int columnSize) {
        DenseMatrix instance = new DenseMatrix(rowSize, columnSize, new float[rowSize * columnSize]);
        return instance;
    }

    public static DenseMatrix valueOf(int rowSize, int columnSize, float[] data) {
        assert data.length >= rowSize * columnSize;
        DenseMatrix instance = new DenseMatrix(rowSize, columnSize, data);
        return instance;
    }

}
