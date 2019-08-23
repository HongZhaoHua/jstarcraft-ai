package com.jstarcraft.ai.math.structure.tensor;

import com.jstarcraft.ai.math.structure.ScalarIterator;

public interface MathTensor extends ScalarIterator<TensorScalar> {

    /**
     * 获取形状
     * 
     * @return
     */
    int[] getShape();

    /**
     * 设置形状
     * 
     * @param shape
     */
    void setShape(int... shape);

    /**
     * 获取步幅
     * 
     * @return
     */
    int[] getStride();

    /**
     * 获取秩的大小
     * 
     * @return
     */
    int getOrderSize();

    /**
     * 获取维度的大小
     * 
     * @return
     */
    int getDimensionSize(int dimension);

    /**
     * 是否支持索引访问
     * 
     * <pre>
     * 与getValue,setValue,scaleValue,shiftValue相关
     * </pre>
     * 
     * @return
     */
    boolean isIndexed();

    /**
     * 获取指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    float getValue(int[] indices);

    /**
     * 设置指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @param value
     */
    void setValue(int[] indices, float value);

    /**
     * 缩放指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @param value
     */
    void scaleValue(int[] indices, float value);

    /**
     * 偏移指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @param value
     */
    void shiftValue(int[] indices, float value);

//    /**
//     * 矩阵加法运算
//     * 
//     * @param matrix
//     * @param transpose
//     * @return
//     */
//    default MathMatrix addTensor(MathMatrix matrix, boolean transpose) {
//        if (getColumnSize() <= getRowSize()) {
//            for (int index = 0, size = getColumnSize(); index < size; index++) {
//                getColumnVector(index).addVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
//            }
//        } else {
//            for (int index = 0, size = getRowSize(); index < size; index++) {
//                getRowVector(index).addVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 矩阵减法运算
//     * 
//     * @param matrix
//     * @param transpose
//     * @return
//     */
//    default MathMatrix subtractTensor(MathMatrix matrix, boolean transpose) {
//        if (getColumnSize() <= getRowSize()) {
//            for (int index = 0, size = getColumnSize(); index < size; index++) {
//                getColumnVector(index).subtractVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
//            }
//        } else {
//            for (int index = 0, size = getRowSize(); index < size; index++) {
//                getRowVector(index).subtractVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 矩阵乘法运算
//     * 
//     * @param matrix
//     * @param transpose
//     * @return
//     */
//    default MathMatrix multiplyTensor(MathMatrix matrix, boolean transpose) {
//        if (getColumnSize() <= getRowSize()) {
//            for (int index = 0, size = getColumnSize(); index < size; index++) {
//                getColumnVector(index).multiplyVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
//            }
//        } else {
//            for (int index = 0, size = getRowSize(); index < size; index++) {
//                getRowVector(index).multiplyVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 矩阵除法运算
//     * 
//     * @param matrix
//     * @param transpose
//     * @return
//     */
//    default MathMatrix divideTensor(MathMatrix matrix, boolean transpose) {
//        if (getColumnSize() <= getRowSize()) {
//            for (int index = 0, size = getColumnSize(); index < size; index++) {
//                getColumnVector(index).divideVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
//            }
//        } else {
//            for (int index = 0, size = getRowSize(); index < size; index++) {
//                getRowVector(index).divideVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 矩阵拷贝运算
//     * 
//     * @param matrix
//     * @param transpose
//     * @return
//     */
//    default MathMatrix copyTensor(MathMatrix matrix, boolean transpose) {
//        if (getColumnSize() <= getRowSize()) {
//            for (int index = 0, size = getColumnSize(); index < size; index++) {
//                getColumnVector(index).copyVector(transpose ? matrix.getRowVector(index) : matrix.getColumnVector(index));
//            }
//        } else {
//            for (int index = 0, size = getRowSize(); index < size; index++) {
//                getRowVector(index).copyVector(transpose ? matrix.getColumnVector(index) : matrix.getRowVector(index));
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 矩阵点积运算
//     * 
//     * @param leftMatrix
//     * @param leftTranspose
//     * @param rightMatrix
//     * @param rightTranspose
//     * @param mode
//     * @return
//     */
//    default MathMatrix dotProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
//        assert getUnknownSize() == 0;
//        // TODO 判断是否为对称?可以节省运算.
//        boolean isSymmetry = (leftMatrix == rightMatrix && leftTranspose != rightTranspose);
//
//        // TODO 此处可以考虑性能优化.
//        switch (mode) {
//        case SERIAL: {
//            for (MatrixScalar term : this) {
//                int rowIndex = term.getRow();
//                int columnIndex = term.getColumn();
//                MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
//                MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
//                term.dotProduct(leftVector, rightVector);
//            }
//            return this;
//        }
//        default: {
//            if (this.getColumnSize() <= this.getRowSize()) {
//                int size = this.getColumnSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (int index = 0; index < size; index++) {
//                    int columnIndex = index;
//                    MathVector columnVector = this.getColumnVector(index);
//                    context.doStructureByAny(index, () -> {
//                        for (VectorScalar term : columnVector) {
//                            int rowIndex = term.getIndex();
//                            MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
//                            MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
//                            term.dotProduct(leftVector, rightVector);
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            } else {
//                int size = this.getRowSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (int index = 0; index < size; index++) {
//                    int rowIndex = index;
//                    MathVector rowVector = this.getRowVector(index);
//                    context.doStructureByAny(index, () -> {
//                        for (VectorScalar term : rowVector) {
//                            int columnIndex = term.getIndex();
//                            MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
//                            MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
//                            term.dotProduct(leftVector, rightVector);
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            }
//            return this;
//        }
//        }
//    }
//
//    /**
//     * 矩阵点积运算
//     * 
//     * @param rowVector
//     * @param columnVector
//     * @param mode
//     * @return
//     */
//    default MathMatrix dotProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
//        assert getUnknownSize() == 0;
//        // TODO 判断是否为对称?可以节省运算.
//        boolean isSymmetry = (rowVector == columnVector);
//
//        switch (mode) {
//        case SERIAL: {
//            if (rowVector.getElementSize() < columnVector.getElementSize()) {
//                for (VectorScalar term : rowVector) {
//                    float rowValue = term.getValue();
//                    MathVector leftVector = this.getRowVector(term.getIndex());
//                    MathVector rightVector = columnVector;
//                    int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                    if (leftSize != 0 && rightSize != 0) {
//                        Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                        Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                        VectorScalar leftTerm = leftIterator.next();
//                        VectorScalar rightTerm = rightIterator.next();
//                        // 判断两个有序数组中是否存在相同的数字
//                        while (leftIndex < leftSize && rightIndex < rightSize) {
//                            if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                leftTerm.setValue(rowValue * rightTerm.getValue());
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                leftIndex++;
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                leftIndex++;
//                            }
//                        }
//                    }
//                }
//            } else {
//                for (VectorScalar term : columnVector) {
//                    float columnValue = term.getValue();
//                    MathVector leftVector = this.getColumnVector(term.getIndex());
//                    MathVector rightVector = rowVector;
//                    int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                    if (leftSize != 0 && rightSize != 0) {
//                        Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                        Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                        VectorScalar leftTerm = leftIterator.next();
//                        VectorScalar rightTerm = rightIterator.next();
//                        // 判断两个有序数组中是否存在相同的数字
//                        while (leftIndex < leftSize && rightIndex < rightSize) {
//                            if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                leftTerm.setValue(columnValue * rightTerm.getValue());
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                leftIndex++;
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                leftIndex++;
//                            }
//                        }
//                    }
//                }
//            }
//            return this;
//        }
//        default: {
//            if (this.getColumnSize() <= this.getRowSize()) {
//                int size = columnVector.getElementSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (VectorScalar term : columnVector) {
//                    float columnValue = term.getValue();
//                    MathVector leftVector = this.getColumnVector(term.getIndex());
//                    MathVector rightVector = rowVector;
//                    context.doStructureByAny(term.getIndex(), () -> {
//                        int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                        if (leftSize != 0 && rightSize != 0) {
//                            Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                            Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                            VectorScalar leftTerm = leftIterator.next();
//                            VectorScalar rightTerm = rightIterator.next();
//                            // 判断两个有序数组中是否存在相同的数字
//                            while (leftIndex < leftSize && rightIndex < rightSize) {
//                                if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                    leftTerm.setValue(columnValue * rightTerm.getValue());
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    leftIndex++;
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    leftIndex++;
//                                }
//                            }
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            } else {
//                int size = rowVector.getElementSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (VectorScalar term : rowVector) {
//                    float rowValue = term.getValue();
//                    MathVector leftVector = this.getRowVector(term.getIndex());
//                    MathVector rightVector = columnVector;
//                    context.doStructureByAny(term.getIndex(), () -> {
//                        int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                        if (leftSize != 0 && rightSize != 0) {
//                            Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                            Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                            VectorScalar leftTerm = leftIterator.next();
//                            VectorScalar rightTerm = rightIterator.next();
//                            // 判断两个有序数组中是否存在相同的数字
//                            while (leftIndex < leftSize && rightIndex < rightSize) {
//                                if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                    leftTerm.setValue(rowValue * rightTerm.getValue());
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    leftIndex++;
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    leftIndex++;
//                                }
//                            }
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            }
//            return this;
//        }
//        }
//    }
//
//    /**
//     * 矩阵累积运算
//     * 
//     * @param leftMatrix
//     * @param leftTranspose
//     * @param rightMatrix
//     * @param rightTranspose
//     * @param mode
//     * @return
//     */
//    default MathMatrix accumulateProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
//        assert getUnknownSize() == 0;
//        // TODO 判断是否为对称?可以节省运算.
//        boolean isSymmetry = (leftMatrix == rightMatrix && leftTranspose != rightTranspose);
//
//        // TODO 此处可以考虑性能优化.
//        switch (mode) {
//        case SERIAL: {
//            for (MatrixScalar term : this) {
//                int rowIndex = term.getRow();
//                int columnIndex = term.getColumn();
//                MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
//                MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
//                term.accumulateProduct(leftVector, rightVector);
//            }
//            return this;
//        }
//        default: {
//            if (this.getColumnSize() <= this.getRowSize()) {
//                int size = this.getColumnSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (int index = 0; index < size; index++) {
//                    int columnIndex = index;
//                    MathVector columnVector = this.getColumnVector(index);
//                    context.doStructureByAny(index, () -> {
//                        for (VectorScalar term : columnVector) {
//                            int rowIndex = term.getIndex();
//                            MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
//                            MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
//                            term.accumulateProduct(leftVector, rightVector);
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            } else {
//                int size = this.getRowSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (int index = 0; index < size; index++) {
//                    int rowIndex = index;
//                    MathVector rowVector = this.getRowVector(index);
//                    context.doStructureByAny(index, () -> {
//                        for (VectorScalar term : rowVector) {
//                            int columnIndex = term.getIndex();
//                            MathVector leftVector = leftTranspose ? leftMatrix.getColumnVector(rowIndex) : leftMatrix.getRowVector(rowIndex);
//                            MathVector rightVector = rightTranspose ? rightMatrix.getRowVector(columnIndex) : rightMatrix.getColumnVector(columnIndex);
//                            term.accumulateProduct(leftVector, rightVector);
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            }
//            return this;
//        }
//        }
//    }
//
//    /**
//     * 矩阵累积运算
//     * 
//     * @param rowVector
//     * @param columnVector
//     * @param mode
//     * @return
//     */
//    default MathMatrix accumulateProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
//        assert getUnknownSize() == 0;
//        // TODO 判断是否为对称?可以节省运算.
//        boolean isSymmetry = (rowVector == columnVector);
//
//        switch (mode) {
//        case SERIAL: {
//            if (rowVector.getElementSize() < columnVector.getElementSize()) {
//                for (VectorScalar term : rowVector) {
//                    float rowValue = term.getValue();
//                    MathVector leftVector = this.getRowVector(term.getIndex());
//                    MathVector rightVector = columnVector;
//                    int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                    if (leftSize != 0 && rightSize != 0) {
//                        Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                        Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                        VectorScalar leftTerm = leftIterator.next();
//                        VectorScalar rightTerm = rightIterator.next();
//                        // 判断两个有序数组中是否存在相同的数字
//                        while (leftIndex < leftSize && rightIndex < rightSize) {
//                            if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                leftTerm.shiftValue(rowValue * rightTerm.getValue());
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                leftIndex++;
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                leftIndex++;
//                            }
//                        }
//                    }
//                }
//            } else {
//                for (VectorScalar term : columnVector) {
//                    float columnValue = term.getValue();
//                    MathVector leftVector = this.getColumnVector(term.getIndex());
//                    MathVector rightVector = rowVector;
//                    int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                    if (leftSize != 0 && rightSize != 0) {
//                        Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                        Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                        VectorScalar leftTerm = leftIterator.next();
//                        VectorScalar rightTerm = rightIterator.next();
//                        // 判断两个有序数组中是否存在相同的数字
//                        while (leftIndex < leftSize && rightIndex < rightSize) {
//                            if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                leftTerm.shiftValue(columnValue * rightTerm.getValue());
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                leftIndex++;
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                if (rightIterator.hasNext()) {
//                                    rightTerm = rightIterator.next();
//                                }
//                                rightIndex++;
//                            } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                if (leftIterator.hasNext()) {
//                                    leftTerm = leftIterator.next();
//                                }
//                                leftIndex++;
//                            }
//                        }
//                    }
//                }
//            }
//            return this;
//        }
//        default: {
//            if (this.getColumnSize() <= this.getRowSize()) {
//                int size = columnVector.getElementSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (VectorScalar term : columnVector) {
//                    float columnValue = term.getValue();
//                    MathVector leftVector = this.getColumnVector(term.getIndex());
//                    MathVector rightVector = rowVector;
//                    context.doStructureByAny(term.getIndex(), () -> {
//                        int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                        if (leftSize != 0 && rightSize != 0) {
//                            Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                            Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                            VectorScalar leftTerm = leftIterator.next();
//                            VectorScalar rightTerm = rightIterator.next();
//                            // 判断两个有序数组中是否存在相同的数字
//                            while (leftIndex < leftSize && rightIndex < rightSize) {
//                                if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                    leftTerm.shiftValue(columnValue * rightTerm.getValue());
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    leftIndex++;
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    leftIndex++;
//                                }
//                            }
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            } else {
//                int size = rowVector.getElementSize();
//                EnvironmentContext context = EnvironmentContext.getContext();
//                CountDownLatch latch = new CountDownLatch(size);
//                for (VectorScalar term : rowVector) {
//                    float rowValue = term.getValue();
//                    MathVector leftVector = this.getRowVector(term.getIndex());
//                    MathVector rightVector = columnVector;
//                    context.doStructureByAny(term.getIndex(), () -> {
//                        int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
//                        if (leftSize != 0 && rightSize != 0) {
//                            Iterator<VectorScalar> leftIterator = leftVector.iterator();
//                            Iterator<VectorScalar> rightIterator = rightVector.iterator();
//                            VectorScalar leftTerm = leftIterator.next();
//                            VectorScalar rightTerm = rightIterator.next();
//                            // 判断两个有序数组中是否存在相同的数字
//                            while (leftIndex < leftSize && rightIndex < rightSize) {
//                                if (leftTerm.getIndex() == rightTerm.getIndex()) {
//                                    leftTerm.shiftValue(rowValue * rightTerm.getValue());
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    leftIndex++;
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
//                                    if (rightIterator.hasNext()) {
//                                        rightTerm = rightIterator.next();
//                                    }
//                                    rightIndex++;
//                                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
//                                    if (leftIterator.hasNext()) {
//                                        leftTerm = leftIterator.next();
//                                    }
//                                    leftIndex++;
//                                }
//                            }
//                        }
//                        latch.countDown();
//                    });
//                }
//                try {
//                    latch.await();
//                } catch (Exception exception) {
//                    throw new RuntimeException(exception);
//                }
//            }
//            return this;
//        }
//        }
//    }

}
