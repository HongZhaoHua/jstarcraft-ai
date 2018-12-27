package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;

public class SparseInstance implements DataInstance {

	private int cursor;

	/** 离散特征 */
	private int[] discreteFeatures;

	private IntegerArray discretePoints;

	private IntegerArray discreteIndexes;

	private IntegerArray discreteValues;

	/** 连续特征 */
	private float[] continuousFeatures;

	private IntegerArray continuousPoints;

	private IntegerArray continuousIndexes;

	private FloatArray continuousValues;

	SparseInstance(int cursor, SparseModule module) {
		this.cursor = cursor;
		this.discreteFeatures = new int[module.getDiscreteOrder()];
		{
			for (int index = 0, size = module.getDiscreteOrder(); index < size; index++) {
				this.discreteFeatures[index] = -1;
			}
		}
		this.discretePoints = module.getDiscretePoints();
		this.discreteIndexes = module.getDiscreteIndexes();
		this.discreteValues = module.getDiscreteValues();
		this.continuousFeatures = new float[module.getContinuousOrder()];
		{
			for (int index = 0, size = module.getContinuousOrder(); index < size; index++) {
				this.continuousFeatures[index] = Float.NaN;
			}
		}
		this.continuousPoints = module.getContinuousPoints();
		this.continuousIndexes = module.getContinuousIndexes();
		this.continuousValues = module.getContinuousValues();
		{
			int from = this.discretePoints.getData(this.cursor);
			int to = this.discretePoints.getData(this.cursor + 1);
			for (int position = from; position < to; position++) {
				int index = this.discreteIndexes.getData(position);
				this.discreteFeatures[index] = this.discreteValues.getData(position);
			}
		}
		{
			int from = this.continuousPoints.getData(this.cursor);
			int to = this.continuousPoints.getData(this.cursor + 1);
			for (int position = from; position < to; position++) {
				int index = this.continuousIndexes.getData(position);
				this.continuousFeatures[index] = this.continuousValues.getData(position);
			}
		}
	}

	@Override
	public void setCursor(int cursor) {
		{
			int from = this.discretePoints.getData(this.cursor);
			int to = this.discretePoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.discreteIndexes.getData(current);
				this.discreteFeatures[index] = -1;
			}
		}
		{
			int from = this.continuousPoints.getData(this.cursor);
			int to = this.continuousPoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.continuousIndexes.getData(current);
				this.continuousFeatures[index] = Float.NaN;
			}
		}
		this.cursor = cursor;
		{
			int from = this.discretePoints.getData(this.cursor);
			int to = this.discretePoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.discreteIndexes.getData(current);
				this.discreteFeatures[index] = this.discreteValues.getData(current);
			}
		}
		{
			int from = this.continuousPoints.getData(this.cursor);
			int to = this.continuousPoints.getData(this.cursor + 1);
			for (int current = from; current < to; current++) {
				int index = this.continuousIndexes.getData(current);
				this.continuousFeatures[index] = this.continuousValues.getData(current);
			}
		}
	}

	@Override
	public int getCursor() {
		return cursor;
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
	public SparseInstance iterateDiscreteFeature(DiscreteAccessor accessor) {
		int from = this.discretePoints.getData(this.cursor);
		int to = this.discretePoints.getData(this.cursor + 1);
		for (int position = from; position < to; position++) {
			int index = this.discreteIndexes.getData(position);
			accessor.accessorFeature(index, this.discreteFeatures[index]);
		}
		return this;
	}

	@Override
	public SparseInstance iterateContinuousFeature(ContinuousAccessor accessor) {
		int from = this.continuousPoints.getData(this.cursor);
		int to = this.continuousPoints.getData(this.cursor + 1);
		for (int position = from; position < to; position++) {
			int index = this.continuousIndexes.getData(position);
			accessor.accessorFeature(index, this.continuousFeatures[index]);
		}
		return this;
	}

}
