package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.exception.DataCapacityException;
import com.jstarcraft.ai.data.exception.DataCursorException;
import com.jstarcraft.ai.data.exception.DataException;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * 稠密模块
 * 
 * @author Birdy
 *
 */
public class DenseModule implements DataModule {

	/** 离散特征 */
	private int discreteOrder;

	private IntegerArray[] discreteValues;

	/** 连续特征 */
	private int continuousOrder;

	private FloatArray[] continuousValues;

	private int capacity;

	private int size;

	public DenseModule(int discreteOrder, int continuousOrder, int instanceCapacity) {
		this.discreteOrder = discreteOrder;
		this.discreteValues = new IntegerArray[discreteOrder];
		for (int index = 0; index < discreteOrder; index++) {
			this.discreteValues[index] = new IntegerArray(1000, instanceCapacity);
		}
		this.continuousOrder = continuousOrder;
		this.continuousValues = new FloatArray[continuousOrder];
		for (int index = 0; index < continuousOrder; index++) {
			this.continuousValues[index] = new FloatArray(1000, instanceCapacity);
		}
		this.capacity = instanceCapacity;
	}

	IntegerArray[] getDiscreteValues() {
		return discreteValues;
	}

	FloatArray[] getContinuousValues() {
		return continuousValues;
	}

	@Override
	public int getDiscreteOrder() {
		return discreteOrder;
	}

	@Override
	public int getContinuousOrder() {
		return continuousOrder;
	}

	@Override
	public void associateInstance(Int2IntSortedMap discreteFeatures, Int2FloatSortedMap continuousFeatures) {
		if (capacity == size) {
			throw new DataCapacityException();
		}
		if (discreteFeatures.firstIntKey() < 0 || discreteFeatures.lastIntKey() >= discreteOrder) {
			throw new DataException();
		}
		if (continuousFeatures.firstIntKey() < 0 || continuousFeatures.lastIntKey() >= continuousOrder) {
			throw new DataException();
		}
		assert discreteOrder == discreteFeatures.size();
		assert continuousOrder == continuousFeatures.size();
		for (Int2IntMap.Entry term : discreteFeatures.int2IntEntrySet()) {
			discreteValues[term.getIntKey()].associateData(term.getIntValue());
		}
		for (Int2FloatMap.Entry term : continuousFeatures.int2FloatEntrySet()) {
			continuousValues[term.getIntKey()].associateData(term.getFloatValue());
		}
		size++;
	}

	@Override
	public DataInstance getInstance(int cursor) {
		if (cursor < 0 && cursor >= size) {
			throw new DataCursorException();
		}
		return new DenseInstance(cursor, this);
	}

	@Override
	public int getSize() {
		return size;
	}

}
