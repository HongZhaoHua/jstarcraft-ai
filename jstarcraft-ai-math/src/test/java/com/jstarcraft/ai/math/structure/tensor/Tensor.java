package com.jstarcraft.ai.math.structure.tensor;

import java.util.Random;

public class Tensor {
    
    private int[] shape;
    private double[] data;

    private int[] mult;
    private int size;

    public Tensor(int[] shape, boolean rand) {
        this.shape = shape;

        calcMult();

        data = new double[size];
        if (rand) {
            Random r = new Random();
            // for initializing weights
            int sum = 0;
            for (int i = 0; i < shape.length; i++) {
                sum += shape[i];
            }
            for (int i = 0; i < size; i++) {
                // xavier normal initialization (not truncated)
                data[i] = r.nextGaussian() * Math.sqrt(2.0 / sum);
            }
        } else {
            for (int i = 0; i < size; i++) {
                data[i] = 0;
            }
        }
    }

    public Tensor(int[] shape, double init) {
        this.shape = shape;

        calcMult();

        data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = init;
        }
    }

    // will create a column vector
    public Tensor(double[] d) {
        shape = new int[] { 1, d.length };
        calcMult();
        data = new double[size];
        for (int i = 0; i < d.length; i++) {
            data[i] = d[i];
        }
    }

    // note that the following two initializers work in row major format!
    // however, the data is internally represented as column major, so some swaps
    // happen
    public Tensor(double[][] d) {
        shape = new int[] { d[0].length, d.length };
        calcMult();
        data = new double[size];
        int idx = 0;
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                data[idx] = d[j][i];
                idx++;
            }
        }
    }

    // the first dimension is treated as the depth!
    public Tensor(double[][][] d) {
        shape = new int[] { d[0][0].length, d[0].length, d.length };
        calcMult();
        data = new double[size];
        int idx = 0;
        for (int i = 0; i < d[0][0].length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                for (int k = 0; k < d.length; k++) {
                    data[idx] = d[k][j][i];
                    idx++;
                }
            }
        }
    }

    public Tensor(int[] shape, double[] data) {
        this.shape = shape;
        calcMult();
        this.data = data;
    }

    // Convert int Data to double Data
    public Tensor(int[][] intD) {
        shape = new int[] { intD[0].length, intD.length };
        calcMult();
        data = new double[size];
        int idx = 0;
        for (int i = 0; i < intD[0].length; i++) {
            for (int j = 0; j < intD.length; j++) {
                data[idx] = (double) intD[j][i];
                idx++;
            }
        }
    }

    public Tensor(int[][][] intD) {
        shape = new int[] { intD[0][0].length, intD[0].length, intD.length };
        calcMult();
        data = new double[size];
        int idx = 0;
        for (int i = 0; i < intD[0][0].length; i++) {
            for (int j = 0; j < intD[0].length; j++) {
                for (int k = 0; k < intD.length; k++) {
                    data[idx] = (double) intD[k][j][i];
                    idx++;
                }
            }
        }
    }

    public int[] shape() {
        return shape;
    }

    public int[] mult() {
        return mult;
    }

    public int size() {
        return size;
    }

    public Tensor add(Tensor o) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] + o.data[i];
        }
        return new Tensor(shape, res);
    }

    public Tensor add(double d) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] + d;
        }
        return new Tensor(shape, res);
    }

    public Tensor sub(Tensor o) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] - o.data[i];
        }
        return new Tensor(shape, res);
    }

    public Tensor sub(double d) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] - d;
        }
        return new Tensor(shape, res);
    }

    public Tensor mul(Tensor o) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] * o.data[i];
        }
        return new Tensor(shape, res);
    }

    public Tensor mul(double d) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] * d;
        }
        return new Tensor(shape, res);
    }

    public Tensor div(Tensor o) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] / o.data[i];
        }
        return new Tensor(shape, res);
    }

    public Tensor div(double d) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i] / d;
        }
        return new Tensor(shape, res);
    }

    public Tensor dot(Tensor o) {
        // basically matrix multiply
        // both must be 2D matrices

        double[] res = new double[shape[1] * o.shape[0]];
        int idx = 0;

        for (int i = 0; i < shape[1]; i++) {
            for (int j = 0; j < o.shape[0]; j++) {
                for (int k = 0; k < shape[0]; k++) {
                    res[idx] += data[k * shape[1] + i] * o.data[j * o.shape[1] + k];
                }
                idx++;
            }
        }

        // transpose because the array is column-wise, not row-wise
        return new Tensor(new int[] { shape[1], o.shape[0] }, res).T();
    }

    public Tensor T() { // transposes 2D matrix
        if (shape.length < 2)
            return this;

        double[] res = new double[size];
        int idx = 0;
        for (int i = 0; i < shape[1]; i++) {
            for (int j = 0; j < shape[0]; j++) {
                res[idx] = data[j * shape[1] + i];
                idx++;
            }
        }
        return new Tensor(new int[] { shape[1], shape[0] }, res);
    }

    public Tensor flatten() {
        return new Tensor(new int[] { 1, size }, data);
    }

    public Tensor reshape(int... s) {
        return new Tensor(s, data);
    }

    public Tensor map(Function f) {
        double[] res = new double[size];
        for (int i = 0; i < size; i++) {
            res[i] = f.apply(data[i]);
        }
        return new Tensor(shape, res);
    }

    public double reduce(double init, Function2 f) {
        double res = init;
        for (int i = 0; i < size; i++) {
            res = f.apply(res, data[i]);
        }
        return res;
    }

    // reduce only the last dimension
    public Tensor reduceLast(double init, Function2 f) {
        double[] res = new double[size / shape[shape.length - 1]];
        for (int i = 0; i < res.length; i++) {
            res[i] = init;
        }

        for (int i = 0; i < size; i++) {
            int idx = i / shape[shape.length - 1];
            res[idx] = f.apply(res[idx], data[i]);
        }

        int[] newShape;
        if (shape.length == 2) {
            newShape = new int[] { 1, shape[0] };
        } else {
            newShape = new int[shape.length - 1];
            for (int i = 0; i < shape.length - 1; i++) {
                newShape[i] = shape[i];
            }
        }
        return new Tensor(newShape, res);
    }

    // duplicate along last dimension + 1
    public Tensor dupLast(int length) {
        double[] res = new double[size * length];
        for (int i = 0; i < res.length; i++) {
            res[i] = data[i / length];
        }

        int[] newShape;
        if (shape[0] == 1 && shape.length == 2) {
            newShape = new int[] { shape[1], length };
        } else {
            newShape = new int[shape.length + 1];
            for (int i = 0; i < shape.length; i++) {
                newShape[i] = shape[i];
            }
            newShape[shape.length] = length;
        }

        return new Tensor(newShape, res);
    }

    // stack copies of the tensor
    public Tensor dupFirst(int length) {
        double[] res = new double[length * size];
        for (int i = 0; i < res.length; i++) {
            int idx = i % size;
            res[i] = data[idx];
        }

        int[] newShape;
        if (shape[0] == 1 && shape.length == 2) {
            newShape = new int[] { length, shape[1] };
        } else {
            newShape = new int[shape.length + 1];
            for (int i = 0; i < shape.length; i++) {
                newShape[i + 1] = shape[i];
            }
            newShape[0] = length;
        }

        return new Tensor(newShape, res);
    }

    public double flatGet(int idx) {
        return data[idx];
    }

    public Tensor get(int idx) {
        double[] res = new double[mult[0]];
        for (int i = 0; i < mult[0]; i++) {
            res[i] = data[idx * mult[0] + i];
        }

        int[] newShape;
        if (shape.length == 2) {
            newShape = new int[] { 1, shape[1] };
        } else {
            newShape = new int[shape.length - 1];
            for (int i = 1; i < shape.length; i++) {
                newShape[i - 1] = shape[i];
            }
        }

        return new Tensor(newShape, res);
    }

    public interface Function {
        public double apply(double x);
    }

    public interface Function2 {
        public double apply(double a, double b);
    }

    private void calcMult() {
        mult = new int[shape.length];
        mult[shape.length - 1] = 1;
        size = shape[shape.length - 1];
        for (int i = shape.length - 2; i >= 0; i--) {
            mult[i] = mult[i + 1] * shape[i + 1];
            size *= shape[i];
        }
    }

    @Override
    public Tensor clone() {
        return new Tensor(shape, data);
    }

}
