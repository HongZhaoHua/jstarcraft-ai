package com.jstarcraft.ai.math.structure.matrix;

import java.util.Iterator;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathIterator;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 缓存矩阵
 * 
 * @author Birdy
 *
 */
public class CacheMatrix implements MathMatrix {

    private ConcurrentLinkedHashMap<Integer, MathVector> cache;

    public CacheMatrix(int minimunSize, int maximunSize, int concurrencyLevel) {
        Builder<Integer, MathVector> builder = new Builder<>();
        builder.initialCapacity(minimunSize);
        builder.maximumWeightedCapacity(maximunSize);
        builder.concurrencyLevel(concurrencyLevel);
        builder.listener(new EvictionListener<Integer, MathVector>() {

            public void onEviction(Integer key, MathVector value) {
            };

        });
        this.cache = builder.build();
    }

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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getKnownSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnknownSize() {
        // TODO Auto-generated method stub
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
    public int getRowSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getColumnSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MathVector getRowVector(int rowIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MathVector getColumnVector(int columnIndex) {
        // TODO Auto-generated method stub
        return null;
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
