package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.QualityAccessor;

public class ArrayInstance implements DataInstance {

	/** 离散秩 */
	private int qualityOrder;

	/** 连续秩 */
	private int continuousOrder;

	/** 离散特征 */
	private int[] qualityFeatures;

	/** 连续特征 */
	private float[] continuousFeatures;

	/** 离散标记 */
	private int qualityMark;

	/** 连续标记 */
	private float continuousMark;

	public ArrayInstance(DataInstance instance) {
		this.qualityOrder = instance.getQualityOrder();
		this.continuousOrder = instance.getQuantityOrder();
		this.qualityFeatures = new int[instance.getQualityOrder()];
		{
			for (int index = 0, size = instance.getQualityOrder(); index < size; index++) {
				this.qualityFeatures[index] = DataInstance.defaultInteger;
			}
		}
		this.continuousFeatures = new float[instance.getQuantityOrder()];
		{
			for (int index = 0, size = instance.getQuantityOrder(); index < size; index++) {
				this.continuousFeatures[index] = DataInstance.defaultFloat;
			}
		}
		instance.iterateQualityFeatures((index, value) -> {
			this.qualityFeatures[index] = value;
		});
		instance.iterateQuantityFeatures((index, value) -> {
			this.continuousFeatures[index] = value;
		});
		this.qualityMark = instance.getQualityMark();
		this.continuousMark = instance.getQuantityMark();
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
	public int getQualityFeature(int index) {
		return this.qualityFeatures[index];
	}

	@Override
	public float getQuantityFeature(int index) {
		return this.continuousFeatures[index];
	}

	@Override
	public ArrayInstance iterateQualityFeatures(QualityAccessor accessor) {
		for (int index = 0; index < qualityOrder; index++) {
			accessor.accessorFeature(index, this.qualityFeatures[index]);
		}
		return this;
	}

	@Override
	public ArrayInstance iterateQuantityFeatures(QuantityAccessor accessor) {
		for (int index = 0; index < continuousOrder; index++) {
			accessor.accessorFeature(index, this.continuousFeatures[index]);
		}
		return this;
	}

	@Override
	public int getQualityMark() {
		return qualityMark;
	}

	@Override
	public float getQuantityMark() {
		return continuousMark;
	}

	@Override
	public int getQualityOrder() {
		return qualityOrder;
	}

	@Override
	public int getQuantityOrder() {
		return continuousOrder;
	}

	public void setQualityFeature(int index, int value) {
		this.qualityFeatures[index] = value;
	}

	public void setContinuousFeature(int index, float value) {
		this.continuousFeatures[index] = value;
	}

	public void setQualityMark(int qualityMark) {
		this.qualityMark = qualityMark;
	}

	public void setContinuousMark(float continuousMark) {
		this.continuousMark = continuousMark;
	}

}
