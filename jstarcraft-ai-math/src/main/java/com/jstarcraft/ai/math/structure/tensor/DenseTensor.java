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

//    public int[] shape() {
//        return shape;
//    }
//
//    public int[] mult() {
//        return stride;
//    }
//
//    public int size() {
//        return size;
//    }
//
//    public DenseTensor add(DenseTensor o) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] + o.values[i];
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor add(double d) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] + d;
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor sub(DenseTensor o) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] - o.values[i];
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor sub(double d) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] - d;
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor mul(DenseTensor o) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] * o.values[i];
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor mul(double d) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] * d;
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor div(DenseTensor o) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] / o.values[i];
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor div(double d) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = values[i] / d;
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public DenseTensor dot(DenseTensor o) {
//        // basically matrix multiply
//        // both must be 2D matrices
//
//        double[] res = new double[shape[1] * o.shape[0]];
//        int idx = 0;
//
//        for (int i = 0; i < shape[1]; i++) {
//            for (int j = 0; j < o.shape[0]; j++) {
//                for (int k = 0; k < shape[0]; k++) {
//                    res[idx] += values[k * shape[1] + i] * o.values[j * o.shape[1] + k];
//                }
//                idx++;
//            }
//        }
//
//        // transpose because the array is column-wise, not row-wise
//        return new DenseTensor(new int[] { shape[1], o.shape[0] }, res).T();
//    }
//
//    public DenseTensor T() { // transposes 2D matrix
//        if (shape.length < 2)
//            return this;
//
//        double[] res = new double[size];
//        int idx = 0;
//        for (int i = 0; i < shape[1]; i++) {
//            for (int j = 0; j < shape[0]; j++) {
//                res[idx] = values[j * shape[1] + i];
//                idx++;
//            }
//        }
//        return new DenseTensor(new int[] { shape[1], shape[0] }, res);
//    }
//
//    public DenseTensor flatten() {
//        return new DenseTensor(new int[] { 1, size }, values);
//    }
//
//    public DenseTensor reshape(int... s) {
//        return new DenseTensor(s, values);
//    }
//
//    public DenseTensor map(Function f) {
//        double[] res = new double[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = f.apply(values[i]);
//        }
//        return new DenseTensor(shape, res);
//    }
//
//    public double reduce(double init, Function2 f) {
//        double res = init;
//        for (int i = 0; i < size; i++) {
//            res = f.apply(res, values[i]);
//        }
//        return res;
//    }
//
//    // reduce only the last dimension
//    public DenseTensor reduceLast(double init, Function2 f) {
//        double[] res = new double[size / shape[shape.length - 1]];
//        for (int i = 0; i < res.length; i++) {
//            res[i] = init;
//        }
//
//        for (int i = 0; i < size; i++) {
//            int idx = i / shape[shape.length - 1];
//            res[idx] = f.apply(res[idx], values[i]);
//        }
//
//        int[] newShape;
//        if (shape.length == 2) {
//            newShape = new int[] { 1, shape[0] };
//        } else {
//            newShape = new int[shape.length - 1];
//            for (int i = 0; i < shape.length - 1; i++) {
//                newShape[i] = shape[i];
//            }
//        }
//        return new DenseTensor(newShape, res);
//    }
//
//    // duplicate along last dimension + 1
//    public DenseTensor dupLast(int length) {
//        double[] res = new double[size * length];
//        for (int i = 0; i < res.length; i++) {
//            res[i] = values[i / length];
//        }
//
//        int[] newShape;
//        if (shape[0] == 1 && shape.length == 2) {
//            newShape = new int[] { shape[1], length };
//        } else {
//            newShape = new int[shape.length + 1];
//            for (int i = 0; i < shape.length; i++) {
//                newShape[i] = shape[i];
//            }
//            newShape[shape.length] = length;
//        }
//
//        return new DenseTensor(newShape, res);
//    }
//
//    // stack copies of the tensor
//    public DenseTensor dupFirst(int length) {
//        double[] res = new double[length * size];
//        for (int i = 0; i < res.length; i++) {
//            int idx = i % size;
//            res[i] = values[idx];
//        }
//
//        int[] newShape;
//        if (shape[0] == 1 && shape.length == 2) {
//            newShape = new int[] { length, shape[1] };
//        } else {
//            newShape = new int[shape.length + 1];
//            for (int i = 0; i < shape.length; i++) {
//                newShape[i + 1] = shape[i];
//            }
//            newShape[0] = length;
//        }
//
//        return new DenseTensor(newShape, res);
//    }
//
//    public double flatGet(int idx) {
//        return values[idx];
//    }
//
//    public DenseTensor get(int idx) {
//        double[] res = new double[stride[0]];
//        for (int i = 0; i < stride[0]; i++) {
//            res[i] = values[idx * stride[0] + i];
//        }
//
//        int[] newShape;
//        if (shape.length == 2) {
//            newShape = new int[] { 1, shape[1] };
//        } else {
//            newShape = new int[shape.length - 1];
//            for (int i = 1; i < shape.length; i++) {
//                newShape[i - 1] = shape[i];
//            }
//        }
//
//        return new DenseTensor(newShape, res);
//    }
//
//    public interface Function {
//        public double apply(double x);
//    }
//
//    public interface Function2 {
//        public double apply(double a, double b);
//    }
//
//    private void calcMult() {
//        stride = new int[shape.length];
//        stride[shape.length - 1] = 1;
//        size = shape[shape.length - 1];
//        for (int i = shape.length - 2; i >= 0; i--) {
//            stride[i] = stride[i + 1] * shape[i + 1];
//            size *= shape[i];
//        }
//    }
//
//    @Override
//    public DenseTensor clone() {
//        return new DenseTensor(shape, values);
//    }

    @Override
    public DenseTensor scaleValues(float value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DenseTensor setValues(float value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DenseTensor shiftValues(float value) {
        // TODO Auto-generated method stub
        return null;
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
