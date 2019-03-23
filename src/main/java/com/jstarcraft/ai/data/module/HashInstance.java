package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.ContinuousAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DiscreteAccessor;
import com.jstarcraft.core.utility.ReflectionUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

public class HashInstance implements DataInstance {

	/** 离散秩 */
	private int discreteOrder;

	/** 连续秩 */
	private int continuousOrder;

	/** 离散特征 */
	private Int2IntSortedMap discreteFeatures;

	/** 连续特征 */
	private Int2FloatSortedMap continuousFeatures;

	/** 离散标记 */
	private int discreteMark;

	/** 连续标记 */
	private float continuousMark;

	public HashInstance(Class<? extends Int2IntSortedMap> discreteClass,  Class<? extends Int2FloatSortedMap> continuousClass, DataInstance instance) {
		this.discreteOrder = instance.getDiscreteOrder();
		this.continuousOrder = instance.getContinuousOrder();
		this.discreteFeatures = ReflectionUtility.getInstance(discreteClass);
		this.discreteFeatures.defaultReturnValue(DataInstance.defaultInteger);
		this.continuousFeatures = ReflectionUtility.getInstance(continuousClass);
		this.continuousFeatures.defaultReturnValue(DataInstance.defaultFloat);
		instance.iterateDiscreteFeatures((index, value) -> {
			this.discreteFeatures.put(index, value);
		});
		instance.iterateContinuousFeatures((index, value) -> {
			this.continuousFeatures.put(index, value);
		});
		this.discreteMark = instance.getDiscreteMark();
		this.continuousMark = instance.getContinuousMark();
	}

	@Override
	public void setCursor(int cursor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getCursor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getDiscreteFeature(int index) {
		return this.discreteFeatures.get(index);
	}

	@Override
	public float getContinuousFeature(int index) {
		return this.continuousFeatures.get(index);
	}

	@Override
	public HashInstance iterateDiscreteFeatures(DiscreteAccessor accessor) {
		for(Int2IntMap.Entry term : this.discreteFeatures.int2IntEntrySet()) {
			accessor.accessorFeature(term.getIntKey(), term.getIntValue());
		}
		return this;
	}

	@Override
	public HashInstance iterateContinuousFeatures(ContinuousAccessor accessor) {
		for(Int2FloatMap.Entry term : this.continuousFeatures.int2FloatEntrySet()) {
			accessor.accessorFeature(term.getIntKey(), term.getFloatValue());
		}
		return this;
	}

	@Override
	public int getDiscreteMark() {
		return discreteMark;
	}

	@Override
	public float getContinuousMark() {
		return continuousMark;
	}

	@Override
	public int getDiscreteOrder() {
		return discreteOrder;
	}

	@Override
	public int getContinuousOrder() {
		return continuousOrder;
	}

	public void setDiscreteFeature(int index, int value) {
		this.discreteFeatures.put(index, value);
	}

	public void setContinuousFeature(int index, float value) {
		this.continuousFeatures.put(index, value);
	}

	public void setDiscreteMark(int discreteMark) {
		this.discreteMark = discreteMark;
	}

	public void setContinuousMark(float continuousMark) {
		this.continuousMark = continuousMark;
	}

}
