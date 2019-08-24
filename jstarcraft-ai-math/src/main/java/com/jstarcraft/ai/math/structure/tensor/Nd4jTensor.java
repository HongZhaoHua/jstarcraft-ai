package com.jstarcraft.ai.math.structure.tensor;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.bytedeco.javacpp.FloatPointer;
import org.nd4j.linalg.api.concurrency.AffinityManager;
import org.nd4j.linalg.api.concurrency.AffinityManager.Location;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentThread;
import com.jstarcraft.ai.environment.Nd4jEnvironmentThread;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathIterator;

public class Nd4jTensor implements MathTensor {

    private static final AffinityManager manager = Nd4j.getAffinityManager();

    /** 方向 */
    private boolean orientation;

    /** 形状 */
    private int[] shape;

    /** 步幅 */
    private int[] stride;

    private int length;

    private float[] data;

    private transient INDArray tensor;

    public Nd4jTensor(INDArray tensor) {
        this.orientation = tensor.ordering() == 'c';
        int order = tensor.rank();
        this.shape = new int[order];
        this.stride = new int[order];
        for (int dimension = 0; dimension < order; dimension++) {
            this.shape[dimension] = (int) tensor.shape()[dimension];
            this.stride[dimension] = (int) tensor.stride()[dimension];
        }
        this.length = (int) tensor.length();
        this.tensor = tensor;
    }

    @Override
    public Nd4jTensor scaleValues(float value) {
        tensor.assign(value);
        return this;
    }

    @Override
    public Nd4jTensor setValues(float value) {
        tensor.muli(value);
        return this;
    }

    @Override
    public Nd4jTensor shiftValues(float value) {
        tensor.addi(value);
        return this;
    }

    @Override
    public float getSum(boolean absolute) {
        if (absolute) {
            return tensor.ameanNumber().floatValue() * tensor.length();
        } else {
            return tensor.sumNumber().floatValue();
        }
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

    private class Nd4jArrayScalar implements TensorScalar {

        private int cursor;

        private int[] indexes = new int[shape.length];

        private float[] data;

        private Nd4jArrayScalar(float[] data) {
            this.data = data;
        }

        private void update(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public int[] getIndexes() {
            int cursor = this.cursor;
            int size = shape.length - 1;
            for (int dimension = 0; dimension < size; dimension++) {
                int index = cursor / stride[dimension];
                cursor -= index * stride[dimension];
                indexes[dimension] = index;
            }
            indexes[size] = cursor % stride[size];
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
        // 保证内存与显存同步
        manager.ensureLocation(tensor, Location.HOST);
        manager.tagLocation(tensor, Location.HOST);
        Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
        data = thread.getArray();
        FloatPointer pointer = (FloatPointer) tensor.data().pointer();
        pointer.get(data, 0, length);
        switch (mode) {
        case SERIAL: {
            Nd4jArrayScalar scalar = new Nd4jArrayScalar(data);
            for (int cursor = 0; cursor < length; cursor++) {
                scalar.update(cursor);
                for (MathAccessor<TensorScalar> accessor : accessors) {
                    accessor.accessElement(scalar);
                }
            }
            pointer.put(data, 0, length);
            return this;
        }
        default: {
            int size = shape[0];
            EnvironmentContext context = thread.getContext();
            Semaphore semaphore = MathCalculator.getSemaphore();
            for (int index = 0; index < size; index++) {
                int from = index * stride[0];
                int to = from + stride[0];
                context.doStructureByAny(index, () -> {
                    Nd4jArrayScalar scalar = new Nd4jArrayScalar(data);
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
            pointer.put(data, 0, length);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setShape(int... shape) {
        // TODO Auto-generated method stub

    }

    @Override
    public int[] getStride() {
        // TODO Auto-generated method stub
        return null;
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
    public float getValue(int[] indices) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setValue(int[] indices, float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void scaleValue(int[] indices, float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void shiftValue(int[] indices, float value) {
        // TODO Auto-generated method stub

    }

}
