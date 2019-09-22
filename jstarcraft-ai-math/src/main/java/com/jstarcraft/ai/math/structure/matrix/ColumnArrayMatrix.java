package com.jstarcraft.ai.math.structure.matrix;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

public class ColumnArrayMatrix extends ArrayMatrix {

    @Override
    public ScalarIterator<MatrixScalar> iterateElement(MathCalculator mode, MathAccessor<MatrixScalar>... accessors) {
        switch (mode) {
        case SERIAL: {
            ArrayMatrixScalar scalar = new ArrayMatrixScalar();
            for (int cursor = 0; cursor < columnSize; cursor++) {
                int columnIndex = cursor;
                ArrayVector vector = vectors[columnIndex];
                for (VectorScalar term : vector) {
                    scalar.update(term, term.getIndex(), columnIndex);
                    for (MathAccessor<MatrixScalar> accessor : accessors) {
                        accessor.accessElement(scalar);
                    }
                }
            }
            return this;
        }
        default: {
            EnvironmentContext context = EnvironmentContext.getContext();
            Semaphore semaphore = MathCalculator.getSemaphore();
            for (int cursor = 0; cursor < columnSize; cursor++) {
                int columnIndex = cursor;
                context.doStructureByAny(cursor, () -> {
                    ArrayMatrixScalar scalar = new ArrayMatrixScalar();
                    ArrayVector vector = vectors[columnIndex];
                    for (VectorScalar term : vector) {
                        scalar.update(term, term.getIndex(), columnIndex);
                        for (MathAccessor<MatrixScalar> accessor : accessors) {
                            accessor.accessElement(scalar);
                        }
                    }
                    semaphore.release();
                });
            }
            try {
                semaphore.acquire(columnSize);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            return this;
        }
        }
    }

    @Override
    public ArrayVector getRowVector(int rowIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayVector getColumnVector(int columnIndex) {
        return vectors[columnIndex];
    }

    @Override
    public MathMatrix addMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getColumnSize(); index < size; index++) {
            getColumnVector(index).addVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix subtractMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getColumnSize(); index < size; index++) {
            getColumnVector(index).subtractVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix multiplyMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getColumnSize(); index < size; index++) {
            getColumnVector(index).multiplyVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix divideMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getColumnSize(); index < size; index++) {
            getColumnVector(index).divideVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix copyMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getColumnSize(); index < size; index++) {
            getColumnVector(index).copyVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix dotProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
        // TODO 此处可以考虑性能优化.
        // TODO 可能触发元素变更.
        switch (mode) {
        case SERIAL: {
            for (MatrixScalar term : this) {
                int rowIndex = term.getRow();
                int columnIndex = term.getColumn();
                MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
                MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
                term.dotProduct(leftVector, rightVector);
            }
            return this;
        }
        default: {
            int size = this.getColumnSize();
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (int index = 0; index < size; index++) {
                int columnIndex = index;
                MathVector columnVector = this.getColumnVector(index);
                context.doStructureByAny(index, () -> {
                    for (VectorScalar term : columnVector) {
                        int rowIndex = term.getIndex();
                        MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
                        MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
                        term.dotProduct(leftVector, rightVector);
                    }
                    latch.countDown();
                });
            }
            try {
                latch.await();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            return this;
        }
        }
    }

    @Override
    public MathMatrix dotProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
        // TODO 可能触发元素变更.
        switch (mode) {
        case SERIAL: {
            for (VectorScalar term : columnVector) {
                float columnValue = term.getValue();
                MathVector leftVector = this.getColumnVector(term.getIndex());
                MathVector rightVector = rowVector;
                int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
                if (leftSize != 0 && rightSize != 0) {
                    Iterator<VectorScalar> leftIterator = leftVector.iterator();
                    Iterator<VectorScalar> rightIterator = rightVector.iterator();
                    VectorScalar leftTerm = leftIterator.next();
                    VectorScalar rightTerm = rightIterator.next();
                    // 判断两个有序数组中是否存在相同的数字
                    while (leftCursor < leftSize && rightCursor < rightSize) {
                        if (leftTerm.getIndex() == rightTerm.getIndex()) {
                            leftTerm.setValue(columnValue * rightTerm.getValue());
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
            }
            return this;
        }
        default: {
            int size = columnVector.getElementSize();
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (VectorScalar term : columnVector) {
                float columnValue = term.getValue();
                MathVector leftVector = this.getColumnVector(term.getIndex());
                MathVector rightVector = rowVector;
                context.doStructureByAny(term.getIndex(), () -> {
                    int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
                    if (leftSize != 0 && rightSize != 0) {
                        Iterator<VectorScalar> leftIterator = leftVector.iterator();
                        Iterator<VectorScalar> rightIterator = rightVector.iterator();
                        VectorScalar leftTerm = leftIterator.next();
                        VectorScalar rightTerm = rightIterator.next();
                        // 判断两个有序数组中是否存在相同的数字
                        while (leftCursor < leftSize && rightCursor < rightSize) {
                            if (leftTerm.getIndex() == rightTerm.getIndex()) {
                                leftTerm.setValue(columnValue * rightTerm.getValue());
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
                    latch.countDown();
                });
            }
            try {
                latch.await();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            return this;
        }
        }
    }

    @Override
    @Deprecated
    // TODO 准备与dotProduct整合
    public MathMatrix accumulateProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
        // TODO 此处可以考虑性能优化.
        // TODO 可能触发元素变更.
        switch (mode) {
        case SERIAL: {
            for (MatrixScalar term : this) {
                int rowIndex = term.getRow();
                int columnIndex = term.getColumn();
                MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
                MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
                term.accumulateProduct(leftVector, rightVector);
            }
            return this;
        }
        default: {
            int size = this.getColumnSize();
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (int index = 0; index < size; index++) {
                int columnIndex = index;
                MathVector columnVector = this.getColumnVector(index);
                context.doStructureByAny(index, () -> {
                    for (VectorScalar term : columnVector) {
                        int rowIndex = term.getIndex();
                        MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
                        MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
                        term.accumulateProduct(leftVector, rightVector);
                    }
                    latch.countDown();
                });
            }
            try {
                latch.await();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            return this;
        }
        }
    }

    @Override
    @Deprecated
    // TODO 准备与dotProduct整合
    public MathMatrix accumulateProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
        // TODO 可能触发元素变更.
        switch (mode) {
        case SERIAL: {
            for (VectorScalar term : columnVector) {
                float columnValue = term.getValue();
                MathVector leftVector = this.getColumnVector(term.getIndex());
                MathVector rightVector = rowVector;
                int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
                if (leftSize != 0 && rightSize != 0) {
                    Iterator<VectorScalar> leftIterator = leftVector.iterator();
                    Iterator<VectorScalar> rightIterator = rightVector.iterator();
                    VectorScalar leftTerm = leftIterator.next();
                    VectorScalar rightTerm = rightIterator.next();
                    // 判断两个有序数组中是否存在相同的数字
                    while (leftCursor < leftSize && rightCursor < rightSize) {
                        if (leftTerm.getIndex() == rightTerm.getIndex()) {
                            leftTerm.shiftValue(columnValue * rightTerm.getValue());
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
            }
            return this;
        }
        default: {
            int size = columnVector.getElementSize();
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (VectorScalar term : columnVector) {
                float columnValue = term.getValue();
                MathVector leftVector = this.getColumnVector(term.getIndex());
                MathVector rightVector = rowVector;
                context.doStructureByAny(term.getIndex(), () -> {
                    int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
                    if (leftSize != 0 && rightSize != 0) {
                        Iterator<VectorScalar> leftIterator = leftVector.iterator();
                        Iterator<VectorScalar> rightIterator = rightVector.iterator();
                        VectorScalar leftTerm = leftIterator.next();
                        VectorScalar rightTerm = rightIterator.next();
                        // 判断两个有序数组中是否存在相同的数字
                        while (leftCursor < leftSize && rightCursor < rightSize) {
                            if (leftTerm.getIndex() == rightTerm.getIndex()) {
                                leftTerm.shiftValue(columnValue * rightTerm.getValue());
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
                    latch.countDown();
                });
            }
            try {
                latch.await();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            return this;
        }
        }
    }

    @Override
    public Iterator<MatrixScalar> iterator() {
        return new ColumnArrayMatrixIterator();
    }

    private class ColumnArrayMatrixIterator implements Iterator<MatrixScalar> {

        private int index;

        private int current = vectors[index].getElementSize();

        private int cursor;

        private int size = elementSize;

        private Iterator<VectorScalar> iterator = vectors[index].iterator();

        private ArrayMatrixScalar term = new ArrayMatrixScalar();

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public MatrixScalar next() {
            if (cursor++ < current) {
                VectorScalar scalar = iterator.next();
                term.update(scalar, scalar.getIndex(), index);
            }
            if (cursor == current && current != size) {
                current += vectors[++index].getElementSize();
                iterator = vectors[index].iterator();
            }
            return term;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static ColumnArrayMatrix valueOf(int rowSize, ArrayVector... components) {
        assert components.length != 0;
        ColumnArrayMatrix instance = new ColumnArrayMatrix();
        for (ArrayVector vector : components) {
            assert rowSize >= vector.getKnownSize() + vector.getUnknownSize();
            vector.attachMonitor(instance);
            instance.elementSize += vector.getElementSize();
            instance.knownSize += vector.getKnownSize();
            instance.unknownSize += vector.getUnknownSize();
        }
        instance.rowSize = rowSize;
        instance.columnSize = components.length;
        instance.vectors = components;
        return instance;
    }

}
