package com.jstarcraft.ai.math.structure.tensor;

import java.util.Iterator;

import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathIterator;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

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
    public ScalarIterator<MatrixScalar> scaleValues(float value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScalarIterator<MatrixScalar> setValues(float value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScalarIterator<MatrixScalar> shiftValues(float value) {
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

    @Override
    public MathIterator<MatrixScalar> iterateElement(MathCalculator mode, MathAccessor<MatrixScalar>... accessors) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<MatrixScalar> iterator() {
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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getDimensionSize(int dimension) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isIndexed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public float getValue(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setValue(int rowIndex, int columnIndex, float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void scaleValue(int rowIndex, int columnIndex, float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void shiftValue(int rowIndex, int columnIndex, float value) {
        // TODO Auto-generated method stub

    }

}
