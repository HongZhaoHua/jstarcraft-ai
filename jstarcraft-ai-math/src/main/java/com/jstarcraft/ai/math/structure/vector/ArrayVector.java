package com.jstarcraft.ai.math.structure.vector;

import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathMonitor;
import com.jstarcraft.ai.math.structure.ScalarIterator;

/**
 * 数组向量
 * 
 * <pre>
 * 提供比稀疏向量更快的访问速度,且能够在一定范围变化索引.
 * </pre>
 * 
 * @author Birdy
 *
 */
public class ArrayVector implements MathVector {

    protected int capacity;

    protected int size;

    /** 索引 */
    protected int[] indexes;

    /** 值 */
    protected float[] values;

    protected transient WeakHashMap<MathMonitor<VectorScalar>, Object> monitors = new WeakHashMap<>();

    @Override
    public int getDimensionSize() {
        return size;
    }

    @Override
    public int getElementSize() {
        return size;
    }

    @Override
    public int getKnownSize() {
        return getElementSize();
    }

    @Override
    public int getUnknownSize() {
        return capacity - getElementSize();
    }

    @Override
    public ScalarIterator<VectorScalar> iterateElement(MathCalculator mode, MathAccessor<VectorScalar>... accessors) {
        switch (mode) {
        case SERIAL: {
            ArrayVectorScalar scalar = new ArrayVectorScalar();
            for (int position = 0; position < size; position++) {
                scalar.update(position);
                for (MathAccessor<VectorScalar> accessor : accessors) {
                    accessor.accessElement(scalar);
                }
            }
            return this;
        }
        default: {
            EnvironmentContext context = EnvironmentContext.getContext();
            Semaphore semaphore = MathCalculator.getSemaphore();
            for (int position = 0; position < size; position++) {
                int index = position;
                context.doStructureByAny(position, () -> {
                    ArrayVectorScalar scalar = new ArrayVectorScalar();
                    scalar.update(index);
                    for (MathAccessor<VectorScalar> accessor : accessors) {
                        accessor.accessElement(scalar);
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
    public ArrayVector setValues(float value) {
        for (int position = 0; position < size; position++) {
            values[position] = value;
        }
        return this;
    }

    @Override
    public ArrayVector scaleValues(float value) {
        for (int position = 0; position < size; position++) {
            values[position] *= value;
        }
        return this;
    }

    @Override
    public ArrayVector shiftValues(float value) {
        for (int position = 0; position < size; position++) {
            values[position] += value;
        }
        return this;
    }

    @Override
    public float getSum(boolean absolute) {
        float sum = 0F;
        if (absolute) {
            for (int position = 0; position < size; position++) {
                sum += FastMath.abs(values[position]);
            }
        } else {
            for (int position = 0; position < size; position++) {
                sum += values[position];
            }
        }
        return sum;
    }

    @Override
    public void attachMonitor(MathMonitor<VectorScalar> monitor) {
        monitors.put(monitor, null);
    }

    @Override
    public void detachMonitor(MathMonitor<VectorScalar> monitor) {
        monitors.remove(monitor);
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public int getIndex(int position) {
        return indexes[position];
    }

    @Override
    public float getValue(int position) {
        return values[position];
    }

    @Override
    public void setValue(int position, float value) {
        values[position] = value;
    }

    @Override
    public void scaleValue(int position, float value) {
        values[position] *= value;
    }

    @Override
    public void shiftValue(int position, float value) {
        values[position] += value;
    }

    public void modifyIndexes(int... indices) {
        assert indexes.length >= indices.length;
        int current = Integer.MIN_VALUE;
        int position = 0;
        for (int index : indices) {
            if (current >= index) {
                throw new IllegalArgumentException();
            }
            current = index;
            if (current < 0) {
                throw new IllegalArgumentException();
            }
            indexes[position] = indices[position];
            position++;
        }
        int oldElementSize = size;
        int oldKnownSize = getKnownSize();
        int oldUnknownSize = getUnknownSize();
        size = indices.length;
        int newElementSize = size;
        int newKnownSize = getKnownSize();
        int newUnknownSize = getUnknownSize();
        for (MathMonitor<VectorScalar> monitor : monitors.keySet()) {
            monitor.notifySizeChanged(this, oldElementSize, newElementSize, oldKnownSize, newKnownSize, oldUnknownSize, newUnknownSize);
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
        ArrayVector that = (ArrayVector) object;
        EqualsBuilder equal = new EqualsBuilder();
        equal.append(this.capacity, that.capacity);
        equal.append(this.size, that.size);
        equal.append(this.indexes, that.indexes);
        equal.append(this.values, that.values);
        return equal.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(capacity);
        hash.append(size);
        hash.append(indexes);
        hash.append(values);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int index = 0; index < size; index++) {
            buffer.append(values[index]).append(", ");
        }
        buffer.append("\n");
        return buffer.toString();
    }

    @Override
    public Iterator<VectorScalar> iterator() {
        return new ArrayVectorIterator();
    }

    private class ArrayVectorIterator implements Iterator<VectorScalar> {

        private int cursor = 0;

        private final ArrayVectorScalar term = new ArrayVectorScalar();

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public VectorScalar next() {
            term.update(cursor++);
            return term;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class ArrayVectorScalar implements VectorScalar {

        private int cursor;

        private void update(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public int getIndex() {
            return indexes[cursor];
        }

        @Override
        public float getValue() {
            return values[cursor];
        }

        @Override
        public void scaleValue(float value) {
            values[cursor] *= value;
        }

        @Override
        public void setValue(float value) {
            values[cursor] = value;
        }

        @Override
        public void shiftValue(float value) {
            values[cursor] += value;
        }

    }

    ArrayVector() {
    }

    public ArrayVector(SparseVector vector) {
        this.capacity = vector.getKnownSize() + vector.getUnknownSize();
        this.size = vector.getElementSize();
        this.indexes = new int[size];
        this.values = new float[size];
        int index = 0;
        for (VectorScalar term : vector) {
            this.indexes[index] = term.getIndex();
            this.values[index] = term.getValue();
            index++;
        }
    }
    
    public ArrayVector(HashVector vector) {
        this.capacity = vector.getKnownSize() + vector.getUnknownSize();
        this.size = vector.getElementSize();
        this.indexes = new int[size];
        this.values = new float[size];
        int index = 0;
        for (VectorScalar term : vector) {
            this.indexes[index] = term.getIndex();
            this.values[index] = term.getValue();
            index++;
        }
    }

    public ArrayVector(int capacity, int[] indexes) {
        this.capacity = capacity;
        this.size = indexes.length;
        assert capacity >= size;
        this.indexes = indexes;
        this.values = new float[size];
    }

    public ArrayVector(int capacity, float[] values) {
        this.capacity = capacity;
        this.size = values.length;
        assert capacity >= size;
        this.indexes = new int[size];
        for (int index = 0; index < size; index++) {
            indexes[index] = index;
        }
        this.values = values;
    }

    public ArrayVector(int capacity, int[] indexes, float[] values) {
        assert indexes.length == values.length;
        this.capacity = capacity;
        this.size = indexes.length;
        assert capacity >= size;
        this.indexes = indexes;
        this.values = values;
    }

}
