package com.jstarcraft.ai.math.structure.tensor;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentThread;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathIterator;

public class DenseTensor implements MathTensor {

    /** 方向 */
    private boolean orientation;

    /** 形状 */
    private int[] shape;

    /** 步幅 */
    private int[] stride;

    private int length;

    private float[] values;

    private void setStride() {
        this.stride = new int[this.shape.length];
        int length = this.length;
        if (this.orientation) {
            for (int index = 0, size = this.shape.length; index < size; index++) {
                length /= this.shape[index];
                this.stride[index] = length;
            }
        } else {
            for (int index = this.shape.length - 1; index > -1; index--) {
                length /= this.shape[index];
                this.stride[index] = length;
            }
        }
    }

    public DenseTensor(boolean orientation, int[] shape) {
        this.orientation = orientation;
        this.shape = shape;
        this.length = 1;
        for (int size : shape) {
            this.length *= size;
        }
        this.setStride();
        this.values = new float[this.length];
    }

    public DenseTensor(boolean orientation, int[] shape, float[] values) {
        this.orientation = orientation;
        this.shape = shape;
        this.length = 1;
        for (int size : shape) {
            this.length *= size;
        }
        assert values.length >= this.length;
        this.setStride();
        this.values = values;
    }

    @Override
    public DenseTensor setValues(float value) {
        for (int cursor = 0; cursor < length; cursor++) {
            values[cursor] = value;
        }
        return this;
    }

    @Override
    public DenseTensor scaleValues(float value) {
        for (int cursor = 0; cursor < length; cursor++) {
            values[cursor] *= value;
        }
        return this;
    }

    @Override
    public DenseTensor shiftValues(float value) {
        for (int cursor = 0; cursor < length; cursor++) {
            values[cursor] += value;
        }
        return this;
    }

    @Override
    public int getElementSize() {
        return length;
    }

    @Override
    public int getKnownSize() {
        return length;
    }

    @Override
    public int getUnknownSize() {
        return 0;
    }

    private class DenseTensorScalar implements TensorScalar {

        private int cursor;

        private int[] indexes = new int[shape.length];

        private float[] data;

        private DenseTensorScalar(float[] data) {
            this.data = data;
        }

        private void update(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public int[] getIndexes() {
            int cursor = this.cursor;
            if (orientation) {
                for (int dimension = 0, size = shape.length; dimension < size; dimension++) {
                    int index = cursor / stride[dimension];
                    cursor -= index * stride[dimension];
                    indexes[dimension] = index;
                }
            } else {
                for (int dimension = shape.length - 1; dimension > -1; dimension--) {
                    int index = cursor / stride[dimension];
                    cursor -= index * stride[dimension];
                    indexes[dimension] = index;
                }
            }
            return indexes;
        }

        @Override
        public float getValue() {
            return data[cursor];
        }

        @Override
        public void scaleValue(float value) {
            data[cursor] *= value;
        }

        @Override
        public void setValue(float value) {
            data[cursor] = value;
        }

        @Override
        public void shiftValue(float value) {
            data[cursor] += value;
        }

    }

    @Override
    public MathIterator<TensorScalar> iterateElement(MathCalculator mode, MathAccessor<TensorScalar>... accessors) {
        switch (mode) {
        case SERIAL: {
            DenseTensorScalar scalar = new DenseTensorScalar(values);
            for (int cursor = 0; cursor < length; cursor++) {
                scalar.update(cursor);
                for (MathAccessor<TensorScalar> accessor : accessors) {
                    accessor.accessElement(scalar);
                }
            }
            return this;
        }
        default: {
            int size = shape[0];
            EnvironmentThread thread = EnvironmentThread.getThread(EnvironmentThread.class);
            EnvironmentContext context = thread.getContext();
            Semaphore semaphore = MathCalculator.getSemaphore();
            for (int index = 0; index < size; index++) {
                int from = index * stride[0];
                int to = from + stride[0];
                context.doStructureByAny(index, () -> {
                    DenseTensorScalar scalar = new DenseTensorScalar(values);
                    for (int cursor = from; cursor < to; cursor++) {
                        scalar.update(cursor);
                        for (MathAccessor<TensorScalar> accessor : accessors) {
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
    public Iterator<TensorScalar> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] getShape() {
        return shape;
    }

    @Override
    public void setShape(int... shape) {
        int length = 1;
        for (int size : shape) {
            length *= size;
        }
        assert this.length == length;
        this.shape = shape;
        this.setStride();
    }

    @Override
    public int[] getStride() {
        return stride;
    }

    @Override
    public int getOrderSize() {
        return shape.length;
    }

    @Override
    public int getDimensionSize(int dimension) {
        return shape[dimension];
    }

    @Override
    public boolean isIndexed() {
        return true;
    }

    @Override
    public float getValue(int[] indices) {
        assert indices.length == stride.length;
        int cursor = 0;
        for (int index = 0, size = stride.length; index < size; index++) {
            cursor += indices[index] * stride[index];
        }
        return values[cursor];
    }

    @Override
    public void setValue(int[] indices, float value) {
        assert indices.length == stride.length;
        int cursor = 0;
        for (int index = 0, size = stride.length; index < size; index++) {
            cursor += indices[index] * stride[index];
        }
        values[cursor] = value;
    }

    @Override
    public void scaleValue(int[] indices, float value) {
        assert indices.length == stride.length;
        int cursor = 0;
        for (int index = 0, size = stride.length; index < size; index++) {
            cursor += indices[index] * stride[index];
        }
        values[cursor] *= value;
    }

    @Override
    public void shiftValue(int[] indices, float value) {
        assert indices.length == stride.length;
        int cursor = 0;
        for (int index = 0, size = stride.length; index < size; index++) {
            cursor += indices[index] * stride[index];
        }
        values[cursor] += value;
    }

}
