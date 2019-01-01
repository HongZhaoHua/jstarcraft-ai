package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.utility.IntegerArray;

public class ReferenceInstance implements DataInstance {

	private int cursor;

	/** 引用 */
	private IntegerArray references;

	/** 模型 */
	private DataInstance instance;

	public ReferenceInstance(int cursor, IntegerArray references, DataModule module) {
		this.cursor = cursor;
		this.references = references;
		this.instance = module.getInstance(references.getData(cursor));
	}

	@Override
	public void setCursor(int cursor) {
		this.cursor = cursor;
		instance.setCursor(references.getData(cursor));
	}

	@Override
	public int getCursor() {
		return cursor;
	}

	@Override
	public int getDiscreteFeature(int index) {
		return instance.getDiscreteFeature(index);
	}

	@Override
	public float getContinuousFeature(int index) {
		return instance.getContinuousFeature(index);
	}

	@Override
	public DataInstance iterateDiscreteFeature(DiscreteAccessor accessor) {
		instance.iterateDiscreteFeature(accessor);
		return this;
	}

	@Override
	public DataInstance iterateContinuousFeature(ContinuousAccessor accessor) {
		instance.iterateContinuousFeature(accessor);
		return this;
	}

}
