package com.jstarcraft.ai.math.structure.matrix;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.vector.GlobalVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class RowGlobalMatrix extends GlobalMatrix {

    @Override
    public ScalarIterator<MatrixScalar> iterateElement(MathCalculator mode, MathAccessor<MatrixScalar>... accessors) {
        switch (mode) {
        case SERIAL: {
            GlobalMatrixScalar scalar = new GlobalMatrixScalar();
            int size = components.length;
            for (int point = 0; point < size; point++) {
                MathMatrix matrix = components[point];
                int split = splits[point];
                for (MatrixScalar term : matrix) {
                    scalar.update(term, term.getRow() + split, term.getColumn());
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
            int size = components.length;
            for (int point = 0; point < size; point++) {
                MathMatrix matrix = components[point];
                int split = splits[point];
                context.doStructureByAny(point, () -> {
                    GlobalMatrixScalar scalar = new GlobalMatrixScalar();
                    for (MatrixScalar term : matrix) {
                        scalar.update(term, term.getRow() + split, term.getColumn());
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

    @Override
    public MathVector getRowVector(int rowIndex) {
        int point = points[rowIndex];
        return components[point].getRowVector(rowIndex - splits[point]);
    }

    @Override
    public MathVector getColumnVector(int columnIndex) {
        MathVector[] vectors = new MathVector[components.length];
        for (int point = 0, size = components.length; point < size; point++) {
            vectors[point] = components[point].getColumnVector(columnIndex);
        }
        return GlobalVector.valueOf(indexed ? points : null, vectors);
    }

    @Override
    public float getValue(int rowIndex, int columnIndex) {
        int point = points[rowIndex];
        return components[point].getValue(rowIndex - splits[point], columnIndex);
    }

    @Override
    public void setValue(int rowIndex, int columnIndex, float value) {
        int point = points[rowIndex];
        components[point].setValue(rowIndex - splits[point], columnIndex, value);
    }

    @Override
    public void scaleValue(int rowIndex, int columnIndex, float value) {
        int point = points[rowIndex];
        components[point].scaleValue(rowIndex - splits[point], columnIndex, value);
    }

    @Override
    public void shiftValue(int rowIndex, int columnIndex, float value) {
        int point = points[rowIndex];
        components[point].shiftValue(rowIndex - splits[point], columnIndex, value);
    }

    @Override
    public Iterator<MatrixScalar> iterator() {
        return new RowGlobalMatrixIterator();
    }

    @Override
    public MathMatrix addMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getRowSize(); index < size; index++) {
            getRowVector(index).addVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix subtractMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getRowSize(); index < size; index++) {
            getRowVector(index).subtractVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix multiplyMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getRowSize(); index < size; index++) {
            getRowVector(index).multiplyVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix divideMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getRowSize(); index < size; index++) {
            getRowVector(index).divideVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
        }
        return this;
    }

    @Override
    public MathMatrix copyMatrix(MathMatrix matrix, boolean transpose) {
        for (int index = 0, size = getRowSize(); index < size; index++) {
            getRowVector(index).copyVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
        }
        return this;
    }

    private class RowGlobalMatrixIterator implements Iterator<MatrixScalar> {

        private int index;

        private int current = components[index].getElementSize();

        private int cursor;

        private int size = elementSize;

        private Iterator<MatrixScalar> iterator = components[index].iterator();

        private GlobalMatrixScalar term = new GlobalMatrixScalar();

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public MatrixScalar next() {
            if (cursor++ < current) {
                MatrixScalar scalar = iterator.next();
                term.update(scalar, scalar.getRow() + splits[index], scalar.getColumn());
            }
            if (cursor == current && current != size) {
                current += components[++index].getElementSize();
                iterator = components[index].iterator();
            }
            return term;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private static RowGlobalMatrix valueOf(MathMatrix... components) {
        assert components.length != 0;
        RowGlobalMatrix instance = new RowGlobalMatrix();
        instance.components = components;
        instance.columnSize = components[0].getColumnSize();
        instance.splits = new int[components.length + 1];
        instance.indexed = true;
        for (int point = 0, size = components.length; point < size; point++) {
            MathMatrix matrix = components[point];
            instance.rowSize += matrix.getRowSize();
            instance.splits[point + 1] = instance.rowSize;
            instance.elementSize += matrix.getElementSize();
            instance.knownSize += matrix.getKnownSize();
            instance.unknownSize += matrix.getUnknownSize();
            if (instance.indexed && !matrix.isIndexed()) {
                instance.indexed = false;
            }
            matrix.attachMonitor(instance);
        }
        instance.points = new int[instance.splits[components.length]];
        int cursor = 0;
        int point = 0;
        for (MathMatrix matrix : components) {
            int size = matrix.getRowSize();
            for (int position = 0; position < size; position++) {
                instance.points[cursor++] = point;
            }
            point++;
        }
        return instance;
    }

    public static RowGlobalMatrix attachOf(MathMatrix... components) {
        Collection<MathMatrix> elements = new LinkedList<>();
        for (MathMatrix component : components) {
            if (component instanceof RowGlobalMatrix) {
                RowGlobalMatrix element = RowGlobalMatrix.class.cast(component);
                Collections.addAll(elements, element.components);
            } else {
                elements.add(component);
            }
        }
        MathMatrix[] matrixes = elements.toArray(new MathMatrix[elements.size()]);
        return valueOf(matrixes);
    }

    public static RowGlobalMatrix detachOf(RowGlobalMatrix components, int from, int to) {
        Collection<MathMatrix> elements = new LinkedList<>();
        for (int point = 0, size = components.splits.length; point < size; point++) {
            int split = components.splits[point];
            if (split == to) {
                break;
            }
            if (split == from) {
                elements.add(components.components[point]);
            } else if (!elements.isEmpty()) {
                elements.add(components.components[point]);
            }
        }
        MathMatrix[] matrixes = elements.toArray(new MathMatrix[elements.size()]);
        return valueOf(matrixes);
    }

}
