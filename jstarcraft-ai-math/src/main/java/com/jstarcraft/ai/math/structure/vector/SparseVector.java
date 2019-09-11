package com.jstarcraft.ai.math.structure.vector;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;

/**
 * 稀疏向量
 * 
 * @author Birdy
 *
 */
public class SparseVector implements MathVector, Iterable<VectorScalar> {

    private int[] points;

    private int[] indexes;

    private float[] values;

    private int capacity;

    private int beginIndex, endIndex;

    @Override
    public int getElementSize() {
        return endIndex - beginIndex;
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
            SparseVectorScalar scalar = new SparseVectorScalar();
            for (int position = beginIndex; position < endIndex; position++) {
                scalar.update(position);
                for (MathAccessor<VectorScalar> accessor : accessors) {
                    accessor.accessElement(scalar);
                }
            }
            return this;
        }
        default: {
            int size = endIndex - beginIndex;
            EnvironmentContext context = EnvironmentContext.getContext();
            Semaphore semaphore = MathCalculator.getSemaphore();
            for (int position = beginIndex; position < endIndex; position++) {
                int cursor = position;
                context.doStructureByAny(position, () -> {
                    SparseVectorScalar scalar = new SparseVectorScalar();
                    scalar.update(cursor);
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
    public SparseVector setValues(float value) {
        for (int position = beginIndex; position < endIndex; position++) {
            int cursor = points[position];
            values[cursor] = value;
        }
        return this;
    }

    @Override
    public SparseVector scaleValues(float value) {
        for (int position = beginIndex; position < endIndex; position++) {
            int cursor = points[position];
            values[cursor] *= value;
        }
        return this;
    }

    @Override
    public SparseVector shiftValues(float value) {
        for (int position = beginIndex; position < endIndex; position++) {
            int cursor = points[position];
            values[cursor] += value;
        }
        return this;
    }

    @Override
    public float getSum(boolean absolute) {
        float sum = 0F;
        if (absolute) {
            for (int position = beginIndex; position < endIndex; position++) {
                sum += FastMath.abs(values[points[position]]);
            }
        } else {
            for (int position = beginIndex; position < endIndex; position++) {
                sum += values[points[position]];
            }
        }
        return sum;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public int getIndex(int position) {
        return indexes[points[beginIndex + position]];
    }

    @Override
    public float getValue(int position) {
        return values[points[beginIndex + position]];
    }

    @Override
    public void setValue(int position, float value) {
        values[points[beginIndex + position]] = value;
    }

    @Override
    public void scaleValue(int position, float value) {
        values[points[beginIndex + position]] *= value;
    }

    @Override
    public void shiftValue(int position, float value) {
        values[points[beginIndex + position]] += value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        SparseVector that = (SparseVector) object;
        EqualsBuilder equal = new EqualsBuilder();
        equal.append(this.beginIndex, that.beginIndex);
        equal.append(this.endIndex, that.endIndex);
        equal.append(this.indexes, that.indexes);
        equal.append(this.values, that.values);
        return equal.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(beginIndex);
        hash.append(endIndex);
        hash.append(indexes);
        hash.append(values);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n", new Object[] { endIndex - beginIndex }));

        for (VectorScalar ve : this)
            if (ve.getValue() != 0)
                sb.append(String.format("%d\t%f\n", new Object[] { ve.getIndex(), ve.getValue() }));

        return sb.toString();
    }

    @Override
    public Iterator<VectorScalar> iterator() {
        return new SparseVectorIterator();
    }

    /**
     * Iterator over a sparse vector
     */
    private class SparseVectorIterator implements Iterator<VectorScalar> {

        private int cursor = beginIndex;

        private final SparseVectorScalar term = new SparseVectorScalar();

        @Override
        public boolean hasNext() {
            return cursor < endIndex;
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

    private class SparseVectorScalar implements VectorScalar {

        private int cursor;

        private void update(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public int getIndex() {
            return indexes[points[cursor]];
        }

        @Override
        public float getValue() {
            return values[points[cursor]];
        }

        @Override
        public void scaleValue(float value) {
            values[points[cursor]] *= value;
        }

        @Override
        public void setValue(float value) {
            values[points[cursor]] = value;
        }

        @Override
        public void shiftValue(float value) {
            values[points[cursor]] += value;
        }

    }

    public SparseVector(int capacity, int[] indexes, int[] keys, float[] values, int beginIndex, int endIndex) {
        assert beginIndex <= endIndex;
        assert capacity >= endIndex - beginIndex;
        this.capacity = capacity;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.points = indexes;
        this.indexes = keys;
        this.values = values;
    }

}
