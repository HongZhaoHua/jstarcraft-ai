package com.jstarcraft.ai.math.structure.matrix;

import java.util.WeakHashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathIterator;
import com.jstarcraft.ai.math.structure.MathMonitor;
import com.jstarcraft.ai.modem.ModemDefinition;

/**
 * 组合矩阵
 * 
 * <pre>
 * 由一系列组件矩阵组成.
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "indexed", "components", "points", "splits", "rowSize", "columnSize", "elementSize", "knownSize", "unknownSize" })
public abstract class GlobalMatrix implements MathMatrix, MathMonitor<MatrixScalar> {

    /** 是否索引 */
    protected boolean indexed;

    /** 矩阵 */
    protected MathMatrix[] components;

    /** 指针(indexed为true时有效,为false时无效) */
    protected int[] points;

    /** 划分 */
    protected int[] splits;

    protected int rowSize, columnSize;

    protected int elementSize, knownSize, unknownSize;

    private transient WeakHashMap<MathMonitor<MatrixScalar>, Object> monitors = new WeakHashMap<>();

    GlobalMatrix() {
    }

    @Override
    public int getElementSize() {
        return elementSize;
    }

    @Override
    public int getKnownSize() {
        return knownSize;
    }

    @Override
    public int getUnknownSize() {
        return unknownSize;
    }

    @Override
    public GlobalMatrix setValues(float value) {
        for (MathMatrix matrix : components) {
            matrix.setValues(value);
        }
        return this;
    }

    @Override
    public GlobalMatrix scaleValues(float value) {
        for (MathMatrix matrix : components) {
            matrix.scaleValues(value);
        }
        return this;
    }

    @Override
    public GlobalMatrix shiftValues(float value) {
        for (MathMatrix matrix : components) {
            matrix.shiftValues(value);
        }
        return this;
    }

    @Override
    public float getSum(boolean absolute) {
        float sum = 0F;
        for (MathMatrix matrix : components) {
            sum += matrix.getSum(absolute);
        }
        return sum;
    }

    @Override
    public void attachMonitor(MathMonitor<MatrixScalar> monitor) {
        monitors.put(monitor, null);
    }

    @Override
    public void detachMonitor(MathMonitor<MatrixScalar> monitor) {
        monitors.remove(monitor);
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
    public boolean isIndexed() {
        return indexed;
    }

    @Override
    public synchronized void notifySizeChanged(MathIterator<MatrixScalar> iterator, int oldElementSize, int newElementSize, int oldKnownSize, int newKnownSize, int oldUnknownSize, int newUnknownSize) {
        {
            int changedElementSize = newElementSize - oldElementSize;
            oldElementSize = elementSize;
            elementSize += changedElementSize;
            newElementSize = elementSize;
        }
        {
            int changedKnownSize = newKnownSize - oldKnownSize;
            oldKnownSize = knownSize;
            knownSize += changedKnownSize;
            newKnownSize = knownSize;
        }
        {
            int changedUnknownSize = newUnknownSize - oldUnknownSize;
            oldUnknownSize = unknownSize;
            unknownSize += changedUnknownSize;
            newUnknownSize = unknownSize;
        }

        for (MathMonitor<MatrixScalar> monitor : monitors.keySet()) {
            monitor.notifySizeChanged(this, oldElementSize, newElementSize, oldKnownSize, newKnownSize, oldUnknownSize, newUnknownSize);
        }
    }

    public MathMatrix getComponentMatrix(int index) {
        return components[index];
    }

    public MathMatrix[] getComponentMatrixes() {
        return components;
    }

    public int getComponentSize() {
        return components.length;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        GlobalMatrix that = (GlobalMatrix) object;
        EqualsBuilder equal = new EqualsBuilder();
        equal.append(this.components, that.components);
        return equal.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(components);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (MathMatrix matrix : components) {
            buffer.append(matrix.toString());
        }
        return buffer.toString();
    }

    protected class GlobalMatrixScalar implements MatrixScalar {

        private MatrixScalar term;

        private int row, column;

        protected void update(MatrixScalar term, int row, int column) {
            this.term = term;
            this.row = row;
            this.column = column;
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public float getValue() {
            return term.getValue();
        }

        @Override
        public void scaleValue(float value) {
            term.scaleValue(value);
        }

        @Override
        public void setValue(float value) {
            term.setValue(value);
        }

        @Override
        public void shiftValue(float value) {
            term.shiftValue(value);
        }

    }

}
