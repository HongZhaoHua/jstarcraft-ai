package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.QualityAccessor;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;

public class SparseInstance implements DataInstance {

	private int cursor;

	/** 离散秩 */
	private int qualityOrder;

	/** 连续秩 */
	private int quantityOrder;

	/** 离散特征 */
	private int[] qualityFeatures;

	private IntegerArray qualityPoints;

	private IntegerArray qualityIndexes;

	private IntegerArray qualityValues;

	/** 连续特征 */
	private float[] quantityFeatures;

	private IntegerArray quantityPoints;

	private IntegerArray quantityIndexes;

	private FloatArray quantityValues;

	/** 离散标记 */
	protected IntegerArray qualityMarks;

	/** 连续标记 */
	protected FloatArray quantityMarks;

	SparseInstance(int cursor, SparseModule module) {
		this.cursor = cursor;
		this.qualityOrder = module.getQualityOrder();
		this.quantityOrder = module.getQuantityOrder();
		this.qualityFeatures = new int[module.getQualityOrder()];
		{
			for (int index = 0, size = module.getQualityOrder(); index < size; index++) {
				this.qualityFeatures[index] = DataInstance.defaultInteger;
			}
		}
		this.qualityPoints = module.getQualityPoints();
		this.qualityIndexes = module.getQualityIndexes();
		this.qualityValues = module.getQualityValues();
		this.quantityFeatures = new float[module.getQuantityOrder()];
		{
			for (int index = 0, size = module.getQuantityOrder(); index < size; index++) {
				this.quantityFeatures[index] = DataInstance.defaultFloat;
			}
		}
		this.quantityPoints = module.getquantityPoints();
		this.quantityIndexes = module.getquantityIndexes();
		this.quantityValues = module.getquantityValues();
		{
			int from = this.qualityPoints.getData(this.cursor);
			int to = this.qualityPoints.getData(this.cursor + 1);
			for (int position = from; position < to; position++) {
				int index = this.qualityIndexes.getData(position);
				this.qualityFeatures[index] = this.qualityValues.getData(position);
			}
		}
		{
			int from = this.quantityPoints.getData(this.cursor);
			int to = this.quantityPoints.getData(this.cursor + 1);
			for (int position = from; position < to; position++) {
				int index = this.quantityIndexes.getData(position);
				this.quantityFeatures[index] = this.quantityValues.getData(position);
			}
		}
	}

	@Override
	public void setCursor(int cursor) {
		{
			int from = this.qualityPoints.getData(this.cursor);
			int to = this.qualityPoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.qualityIndexes.getData(current);
				this.qualityFeatures[index] = DataInstance.defaultInteger;
			}
		}
		{
			int from = this.quantityPoints.getData(this.cursor);
			int to = this.quantityPoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.quantityIndexes.getData(current);
				this.quantityFeatures[index] = DataInstance.defaultFloat;
			}
		}
		this.cursor = cursor;
		{
			int from = this.qualityPoints.getData(this.cursor);
			int to = this.qualityPoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.qualityIndexes.getData(current);
				this.qualityFeatures[index] = this.qualityValues.getData(current);
			}
		}
		{
			int from = this.quantityPoints.getData(this.cursor);
			int to = this.quantityPoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.quantityIndexes.getData(current);
				this.quantityFeatures[index] = this.quantityValues.getData(current);
			}
		}
	}

	@Override
	public int getCursor() {
		return cursor;
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
	public SparseInstance iterateQualityFeatures(QualityAccessor accessor) {
		int from = this.qualityPoints.getData(this.cursor);
		int to = this.qualityPoints.getData(this.cursor + 1);
		for (int position = from; position < to; position++) {
			int index = this.qualityIndexes.getData(position);
			accessor.accessorFeature(index, this.qualityFeatures[index]);
		}
		return this;
	}

	@Override
	public SparseInstance iterateQuantityFeatures(QuantityAccessor accessor) {
		int from = this.quantityPoints.getData(this.cursor);
		int to = this.quantityPoints.getData(this.cursor + 1);
		for (int position = from; position < to; position++) {
			int index = this.quantityIndexes.getData(position);
			accessor.accessorFeature(index, this.quantityFeatures[index]);
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
