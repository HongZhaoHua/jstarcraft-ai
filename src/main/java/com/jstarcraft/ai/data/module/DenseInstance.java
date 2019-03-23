package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.QualityAccessor;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;

public class DenseInstance implements DataInstance {

	private int cursor;

	/** 离散秩 */
	private int qualityOrder;

	/** 连续秩 */
	private int quantityOrder;

	/** 离散特征 */
	private IntegerArray[] qualityValues;

	/** 连续特征 */
	private FloatArray[] quantityValues;

	/** 离散标记 */
	protected IntegerArray qualityMarks;

	/** 连续标记 */
	protected FloatArray quantityMarks;

	DenseInstance(int cursor, DenseModule module) {
		this.cursor = cursor;
		this.qualityOrder = module.getQualityOrder();
		this.quantityOrder = module.getQuantityOrder();
		this.qualityValues = module.getQualityValues();
		this.quantityValues = module.getquantityValues();
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
		return qualityValues[index].getData(cursor);
	}

	@Override
	public float getQuantityFeature(int index) {
		return quantityValues[index].getData(cursor);
	}

	@Override
	public DenseInstance iterateQualityFeatures(QualityAccessor accessor) {
		for (int index = 0; index < qualityOrder; index++) {
			accessor.accessorFeature(index, qualityValues[index].getData(cursor));
		}
		return this;
	}

	@Override
	public DenseInstance iterateQuantityFeatures(QuantityAccessor accessor) {
		for (int index = 0; index < quantityOrder; index++) {
			accessor.accessorFeature(index, quantityValues[index].getData(cursor));
		}
		return this;
	}

	@Override
	public int getQualityMark() {
		return qualityMarks.getData(cursor);
	}

	@Override
	public float getQuantityMark() {
		return quantityMarks.getData(cursor);
	}

	@Override
	public int getQualityOrder() {
		return qualityOrder;
	}

	@Override
	public int getQuantityOrder() {
		return quantityOrder;
	}

}
