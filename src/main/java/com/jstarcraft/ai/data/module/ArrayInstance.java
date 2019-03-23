package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.QualityAccessor;

public class ArrayInstance implements DataInstance {

	/** 离散秩 */
	private int qualityOrder;

	/** 连续秩 */
	private int quantityOrder;

	/** 离散特征 */
	private int[] qualityFeatures;

	/** 连续特征 */
	private float[] quantityFeatures;

	/** 离散标记 */
	private int qualityMark;

	/** 连续标记 */
	private float quantityMark;

	public ArrayInstance(DataInstance instance) {
		this.qualityOrder = instance.getQualityOrder();
		this.quantityOrder = instance.getQuantityOrder();
		this.qualityFeatures = new int[instance.getQualityOrder()];
		{
			for (int index = 0, size = instance.getQualityOrder(); index < size; index++) {
				this.qualityFeatures[index] = DataInstance.defaultInteger;
			}
		}
		this.quantityFeatures = new float[instance.getQuantityOrder()];
		{
			for (int index = 0, size = instance.getQuantityOrder(); index < size; index++) {
				this.quantityFeatures[index] = DataInstance.defaultFloat;
			}
		}
		instance.iterateQualityFeatures((index, value) -> {
			this.qualityFeatures[index] = value;
		});
		instance.iterateQuantityFeatures((index, value) -> {
			this.quantityFeatures[index] = value;
		});
		this.qualityMark = instance.getQualityMark();
		this.quantityMark = instance.getQuantityMark();
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
		return this.quantityFeatures[index];
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
		for (int index = 0; index < quantityOrder; index++) {
			accessor.accessorFeature(index, this.quantityFeatures[index]);
		}
		return this;
	}

	@Override
	public int getQualityMark() {
		return qualityMark;
	}

	@Override
	public float getQuantityMark() {
		return quantityMark;
	}

	@Override
	public int getQualityOrder() {
		return qualityOrder;
	}

	@Override
	public int getQuantityOrder() {
		return quantityOrder;
	}

	public void setQualityFeature(int index, int value) {
		this.qualityFeatures[index] = value;
	}

	public void setQuantityFeature(int index, float value) {
		this.quantityFeatures[index] = value;
	}

	public void setQualityMark(int qualityMark) {
		this.qualityMark = qualityMark;
	}

	public void setQuantityMark(float quantityMark) {
		this.quantityMark = quantityMark;
	}

}
