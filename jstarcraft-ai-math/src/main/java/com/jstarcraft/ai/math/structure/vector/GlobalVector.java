package com.jstarcraft.ai.math.structure.vector;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathIterator;
import com.jstarcraft.ai.math.structure.MathMonitor;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.matrix.GlobalMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 组合向量
 * 
 * <pre>
 * 由一系列组件向量组成.用于配合{@link GlobalMatrix}
 * </pre>
 * 
 * @author Birdy
 *
 */
public class GlobalVector implements MathVector, MathMonitor<VectorScalar> {

    /** 向量 */
    private MathVector[] components;

    /** 指针(constant为true时有效,为false时无效) */
    private int[] points;

    /** 划分 */
    private int[] splits;

    private int elementSize, knownSize, unknownSize;

    private transient WeakHashMap<MathMonitor<VectorScalar>, Object> monitors = new WeakHashMap<>();

    GlobalVector() {
    }

    @Override
    public int getDimensionSize() {
        return knownSize + unknownSize;
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
    public ScalarIterator<VectorScalar> iterateElement(MathCalculator mode, MathAccessor<VectorScalar>... accessors) {
        switch (mode) {
        case SERIAL: {
            CompositeVectorScalar scalar = new CompositeVectorScalar();
            int size = components.length;
            for (int point = 0; point < size; point++) {
                MathVector vector = components[point];
                int split = splits[point];
                for (VectorScalar term : vector) {
                    scalar.update(term, term.getIndex() + split);
                    for (MathAccessor<VectorScalar> accessor : accessors) {
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
                MathVector vector = components[point];
                int split = splits[point];
                context.doStructureByAny(point, () -> {
                    CompositeVectorScalar scalar = new CompositeVectorScalar();
                    for (VectorScalar term : vector) {
                        scalar.update(term, term.getIndex() + split);
                        for (MathAccessor<VectorScalar> accessor : accessors) {
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
    public GlobalVector setValues(float value) {
        for (MathVector vector : components) {
            vector.setValues(value);
        }
        return this;
    }

    @Override
    public GlobalVector scaleValues(float value) {
        for (MathVector vector : components) {
            vector.scaleValues(value);
        }
        return this;
    }

    @Override
    public GlobalVector shiftValues(float value) {
        for (MathVector vector : components) {
            vector.shiftValues(value);
        }
        return this;
    }

    @Override
    public float getSum(boolean absolute) {
        float sum = 0F;
        for (MathVector vector : components) {
            sum += vector.getSum(absolute);
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
        return points != null;
    }

    @Override
    public int getIndex(int position) {
        int point = points[position];
        return components[point].getIndex(position - splits[point]);
    }

    @Override
    public float getValue(int position) {
        int point = points[position];
        return components[point].getValue(position - splits[point]);
    }

    @Override
    public void setValue(int position, float value) {
        int point = points[position];
        components[point].setValue(position - splits[point], value);
    }

    @Override
    public void scaleValue(int position, float value) {
        int point = points[position];
        components[point].scaleValue(position - splits[point], value);
    }

    @Override
    public void shiftValue(int position, float value) {
        int point = points[position];
        components[point].shiftValue(position - splits[point], value);
    }

    @Override
    public MathVector dotProduct(MathMatrix leftMatrix, boolean transpose, MathVector rightVector, MathCalculator mode) {
        switch (mode) {
        case SERIAL: {
            for (VectorScalar term : this) {
                int index = term.getIndex();
                MathVector leftVector = transpose ? leftMatrix.getColumnVector(index) : leftMatrix.getRowVector(index);
                term.dotProduct(leftVector, rightVector);
            }
            return this;
        }
        default: {
            int size = components.length;
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (int position = 0; position < size; position++) {
                MathVector component = components[position];
                int split = splits[position];
                context.doStructureByAny(position, () -> {
                    for (VectorScalar term : component) {
                        int index = term.getIndex() + split;
                        MathVector leftVector = transpose ? leftMatrix.getColumnVector(index) : leftMatrix.getRowVector(index);
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
    public MathVector dotProduct(MathVector leftVector, MathMatrix rightMatrix, boolean transpose, MathCalculator mode) {
        switch (mode) {
        case SERIAL: {
            for (VectorScalar term : this) {
                int index = term.getIndex();
                MathVector rightVector = transpose ? rightMatrix.getRowVector(index) : rightMatrix.getColumnVector(index);
                term.dotProduct(leftVector, rightVector);
            }
            return this;
        }
        default: {
            int size = components.length;
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (int position = 0; position < size; position++) {
                MathVector component = components[position];
                int split = splits[position];
                context.doStructureByAny(position, () -> {
                    for (VectorScalar term : component) {
                        int index = term.getIndex() + split;
                        MathVector rightVector = transpose ? rightMatrix.getRowVector(index) : rightMatrix.getColumnVector(index);
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
    @Deprecated
    // TODO 准备与dotProduct整合
    public MathVector accumulateProduct(MathMatrix leftMatrix, boolean transpose, MathVector rightVector, MathCalculator mode) {
        switch (mode) {
        case SERIAL: {
            for (VectorScalar term : this) {
                int index = term.getIndex();
                MathVector leftVector = transpose ? leftMatrix.getColumnVector(index) : leftMatrix.getRowVector(index);
                term.accumulateProduct(leftVector, rightVector);
            }
            return this;
        }
        default: {
            int size = components.length;
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (int position = 0; position < size; position++) {
                MathVector component = components[position];
                int split = splits[position];
                context.doStructureByAny(position, () -> {
                    for (VectorScalar term : component) {
                        int index = term.getIndex() + split;
                        MathVector leftVector = transpose ? leftMatrix.getColumnVector(index) : leftMatrix.getRowVector(index);
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
    public MathVector accumulateProduct(MathVector leftVector, MathMatrix rightMatrix, boolean transpose, MathCalculator mode) {
        switch (mode) {
        case SERIAL: {
            for (VectorScalar term : this) {
                int index = term.getIndex();
                MathVector rightVector = transpose ? rightMatrix.getRowVector(index) : rightMatrix.getColumnVector(index);
                term.accumulateProduct(leftVector, rightVector);
            }
            return this;
        }
        default: {
            int size = components.length;
            EnvironmentContext context = EnvironmentContext.getContext();
            CountDownLatch latch = new CountDownLatch(size);
            for (int position = 0; position < size; position++) {
                MathVector component = components[position];
                int split = splits[position];
                context.doStructureByAny(position, () -> {
                    for (VectorScalar term : component) {
                        int index = term.getIndex() + split;
                        MathVector rightVector = transpose ? rightMatrix.getRowVector(index) : rightMatrix.getColumnVector(index);
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
    public synchronized void notifySizeChanged(MathIterator<VectorScalar> iterator, int oldElementSize, int newElementSize, int oldKnownSize, int newKnownSize, int oldUnknownSize, int newUnknownSize) {
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

        for (MathMonitor<VectorScalar> monitor : monitors.keySet()) {
            monitor.notifySizeChanged(this, oldElementSize, newElementSize, oldKnownSize, newKnownSize, oldUnknownSize, newUnknownSize);
        }
    }

    public MathVector getComponentVector(int point) {
        return components[point];
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
        GlobalVector that = (GlobalVector) object;
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
        for (MathVector vector : components) {
            buffer.append(vector.toString());
        }
        return buffer.toString();
    }

    @Override
    public Iterator<VectorScalar> iterator() {
        return new CompositeVectorIterator();
    }

    private class CompositeVectorIterator implements Iterator<VectorScalar> {

        private int index;

        private int split = components[index].getElementSize();

        private int size = elementSize;

        private int cursor;

        private Iterator<VectorScalar> iterator = components[index].iterator();

        private CompositeVectorScalar term = new CompositeVectorScalar();

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public VectorScalar next() {
            if (cursor < split) {
                VectorScalar scalar = iterator.next();
                term.update(scalar, cursor++);
            }
            if (cursor == split && split != size) {
                split += components[++index].getElementSize();
                iterator = components[index].iterator();
            }
            return term;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class CompositeVectorScalar implements VectorScalar {

        private VectorScalar term;

        private int index;

        private void update(VectorScalar term, int index) {
            this.term = term;
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
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

    public static GlobalVector valueOf(int[] points, MathVector... components) {
        assert components.length != 0;
        GlobalVector instance = new GlobalVector();
        instance.components = components;
        instance.splits = new int[components.length + 1];
        for (int point = 0, size = components.length; point < size; point++) {
            MathVector vector = components[point];
            instance.splits[point + 1] = instance.splits[point] + vector.getKnownSize() + vector.getUnknownSize();
            instance.elementSize += vector.getElementSize();
            instance.knownSize += vector.getKnownSize();
            instance.unknownSize += vector.getUnknownSize();
        }
        instance.points = points;
        for (ScalarIterator<VectorScalar> vector : components) {
            vector.attachMonitor(instance);
        }
        return instance;
    }

    public static GlobalVector attachOf(MathVector... components) {
        Collection<MathVector> elements = new LinkedList<>();
        for (MathVector component : components) {
            if (component instanceof GlobalVector) {
                GlobalVector element = GlobalVector.class.cast(component);
                Collections.addAll(elements, element.components);
            } else {
                elements.add(component);
            }
        }
        MathVector[] vectors = elements.toArray(new MathVector[elements.size()]);
        boolean constant = true;
        int size = 0;
        for (MathVector vector : vectors) {
            if (constant && !vector.isConstant()) {
                constant = false;
                break;
            }
            size += vector.getElementSize();
        }
        int[] points = null;
        if (constant) {
            points = new int[size];
            int cursor = 0;
            int point = 0;
            for (MathVector vector : vectors) {
                size = vector.getElementSize();
                for (int position = 0; position < size; position++) {
                    points[cursor++] = point;
                }
                point++;
            }
        }
        return valueOf(points, vectors);
    }

    public static GlobalVector detachOf(GlobalVector components, int from, int to) {
        Collection<MathVector> elements = new LinkedList<>();
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
        MathVector[] vectors = elements.toArray(new MathVector[elements.size()]);
        boolean constant = true;
        int size = 0;
        for (MathVector vector : vectors) {
            if (constant && !vector.isConstant()) {
                constant = false;
                break;
            }
            // TODO 此处修改为vector.getKnownSize() + vector.getUnknownSize()可能更为合理?
            size += vector.getElementSize();
        }
        int[] points = null;
        if (constant) {
            points = new int[size];
            int cursor = 0;
            int point = 0;
            for (MathVector vector : vectors) {
                size = vector.getElementSize();
                for (int position = 0; position < size; position++) {
                    points[cursor++] = point;
                }
                point++;
            }
        }
        return valueOf(points, vectors);
    }

}
