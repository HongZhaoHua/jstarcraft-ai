package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.QualityAccessor;
import com.jstarcraft.ai.utility.IntegerArray;

/**
 * 引用实例
 * 
 * @author Birdy
 *
 */
public class ReferenceInstance implements DataInstance {

	/** 游标 */
	private int cursor;

	/** 引用 */
	private IntegerArray references;

	/** 实例 */
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
	public int getQualityFeature(int index) {
		return instance.getQualityFeature(index);
	}

	@Override
	public float getQuantityFeature(int index) {
		return instance.getQuantityFeature(index);
	}

	@Override
	public DataInstance iterateQualityFeatures(QualityAccessor accessor) {
		instance.iterateQualityFeatures(accessor);
		return this;
	}

	@Override
	public DataInstance iterateQuantityFeatures(QuantityAccessor accessor) {
		instance.iterateQuantityFeatures(accessor);
		return this;
	}

	@Override
	public int getQualityOrder() {
		return instance.getQualityOrder();
	}

	@Override
	public int getQuantityOrder() {
		return instance.getQuantityOrder();
	}

	@Override
	public int getQualityMark() {
		return instance.getQualityMark();
	}

	@Override
	public float getQuantityMark() {
		return instance.getQuantityMark();
	}

	@Override
	public float getWeight() {
		return instance.getWeight();
	}

	@Override
	public void setQualityMark(int mark) {
		instance.setQualityMark(mark);
	}

	@Override
	public void setQuantityMark(float mark) {
		instance.setQuantityMark(mark);
	}

	@Override
	public void setWeight(float weight) {
		instance.setWeight(weight);
	}

}
