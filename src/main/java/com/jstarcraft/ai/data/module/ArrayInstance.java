package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.ContinuousAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DiscreteAccessor;

public class ArrayInstance implements DataInstance {

	/** 离散秩 */
	private int discreteOrder;

	/** 连续秩 */
	private int continuousOrder;

	/** 离散特征 */
	private int[] discreteFeatures;

	/** 连续特征 */
	private float[] continuousFeatures;

	/** 离散标记 */
	private int discreteMark;

	/** 连续标记 */
	private float continuousMark;

	public ArrayInstance(DataInstance instance) {
		this.discreteOrder = instance.getDiscreteOrder();
		this.continuousOrder = instance.getContinuousOrder();
		this.discreteFeatures = new int[instance.getDiscreteOrder()];
		{
			for (int index = 0, size = instance.getDiscreteOrder(); index < size; index++) {
				this.discreteFeatures[index] = DataInstance.defaultInteger;
			}
		}
		this.continuousFeatures = new float[instance.getContinuousOrder()];
		{
			for (int index = 0, size = instance.getContinuousOrder(); index < size; index++) {
				this.continuousFeatures[index] = DataInstance.defaultFloat;
			}
		}
		instance.iterateDiscreteFeatures((index, value) -> {
			this.discreteFeatures[index] = value;
		});
		instance.iterateContinuousFeatures((index, value) -> {
			this.continuousFeatures[index] = value;
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
		return this.discreteFeatures[index];
	}

	@Override
	public float getContinuousFeature(int index) {
		return this.continuousFeatures[index];
	}

	@Override
	public ArrayInstance iterateDiscreteFeatures(DiscreteAccessor accessor) {
		for (int index = 0; index < discreteOrder; index++) {
			accessor.accessorFeature(index, this.discreteFeatures[index]);
		}
		return this;
	}

	@Override
	public ArrayInstance iterateContinuousFeatures(ContinuousAccessor accessor) {
		for (int index = 0; index < continuousOrder; index++) {
			accessor.accessorFeature(index, this.continuousFeatures[index]);
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
		this.discreteFeatures[index] = value;
	}

	public void setContinuousFeature(int index, float value) {
		this.continuousFeatures[index] = value;
	}

	public void setDiscreteMark(int discreteMark) {
		this.discreteMark = discreteMark;
	}

	public void setContinuousMark(float continuousMark) {
		this.continuousMark = continuousMark;
	}

}
