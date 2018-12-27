package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;

public class DenseInstance implements DataInstance {

	private int cursor;

	/** 离散特征 */
	private int discreteOrder;

	/** 连续特征 */
	private int continuousOrder;

	/** 离散特征 */
	private IntegerArray[] discreteValues;

	/** 连续特征 */
	private FloatArray[] continuousValues;

	DenseInstance(int cursor, DenseModule module) {
		this.cursor = cursor;
		this.discreteOrder = module.getDiscreteOrder();
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
	public int getDiscreteFeature(int index) {
		return discreteValues[index].getData(cursor);
	}

	@Override
	public float getContinuousFeature(int index) {
		return continuousValues[index].getData(cursor);
	}

	@Override
	public DenseInstance iterateDiscreteFeature(DiscreteAccessor accessor) {
		for (int index = 0; index < discreteOrder; index++) {
			accessor.accessorFeature(index, discreteValues[index].getData(cursor));
		}
		return this;
	}

	@Override
	public DenseInstance iterateContinuousFeature(ContinuousAccessor accessor) {
		for (int index = 0; index < continuousOrder; index++) {
			accessor.accessorFeature(index, continuousValues[index].getData(cursor));
		}
		return this;
	}

}
