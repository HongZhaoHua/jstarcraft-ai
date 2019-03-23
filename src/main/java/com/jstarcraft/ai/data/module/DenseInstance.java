package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.QualityAccessor;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;

public class DenseInstance implements DataInstance {

	private int cursor;

	/** 离散秩 */
	private int discreteOrder;

	/** 连续秩 */
	private int continuousOrder;

	/** 离散特征 */
	private IntegerArray[] discreteValues;

	/** 连续特征 */
	private FloatArray[] continuousValues;

	/** 离散标记 */
	protected IntegerArray discreteMarks;

	/** 连续标记 */
	protected FloatArray continuousMarks;

	DenseInstance(int cursor, DenseModule module) {
		this.cursor = cursor;
		this.discreteOrder = module.getQualityOrder();
		this.continuousOrder = module.getContinuousOrder();
		this.discreteValues = module.getDiscreteValues();
		this.continuousValues = module.getContinuousValues();
	}

	@Override
	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	@Override
	public int getCursor() {
		return cursor;
	}

	@Override
	public int getQualityFeature(int index) {
		return discreteValues[index].getData(cursor);
	}

	@Override
	public float getQuantityFeature(int index) {
		return continuousValues[index].getData(cursor);
	}

	@Override
	public DenseInstance iterateQualityFeatures(QualityAccessor accessor) {
		for (int index = 0; index < discreteOrder; index++) {
			accessor.accessorFeature(index, discreteValues[index].getData(cursor));
		}
		return this;
	}

	@Override
	public DenseInstance iterateQuantityFeatures(QuantityAccessor accessor) {
		for (int index = 0; index < continuousOrder; index++) {
			accessor.accessorFeature(index, continuousValues[index].getData(cursor));
		}
		return this;
	}

	@Override
	public int getQualityMark() {
		return discreteMarks.getData(cursor);
	}

	@Override
	public float getQuantityMark() {
		return continuousMarks.getData(cursor);
	}

	@Override
	public int getQualityOrder() {
		return discreteOrder;
	}

	@Override
	public int getQuantityOrder() {
		return continuousOrder;
	}

}
