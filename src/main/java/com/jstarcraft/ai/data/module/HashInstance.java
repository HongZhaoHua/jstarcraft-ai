package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.QualityAccessor;
import com.jstarcraft.core.utility.ReflectionUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * 哈希实例
 * 
 * @author Birdy
 *
 */
public class HashInstance implements DataInstance {

	/** 离散秩 */
	private int qualityOrder;

	/** 连续秩 */
	private int quantityOrder;

	/** 离散特征 */
	private Int2IntSortedMap qualityFeatures;

	/** 连续特征 */
	private Int2FloatSortedMap quantityFeatures;

	/** 离散标记 */
	private int qualityMark;

	/** 连续标记 */
	private float quantityMark;

	/** 权重 */
	private float weight;

	public HashInstance(Class<? extends Int2IntSortedMap> qualityClass, Class<? extends Int2FloatSortedMap> quantityClass, DataInstance instance) {
		this.qualityOrder = instance.getQualityOrder();
		this.quantityOrder = instance.getQuantityOrder();
		this.qualityFeatures = ReflectionUtility.getInstance(qualityClass);
		this.qualityFeatures.defaultReturnValue(DataInstance.defaultInteger);
		this.quantityFeatures = ReflectionUtility.getInstance(quantityClass);
		this.quantityFeatures.defaultReturnValue(DataInstance.defaultFloat);
		instance.iterateQualityFeatures((index, value) -> {
			this.qualityFeatures.put(index, value);
		});
		instance.iterateQuantityFeatures((index, value) -> {
			this.quantityFeatures.put(index, value);
		});
		this.qualityMark = instance.getQualityMark();
		this.quantityMark = instance.getQuantityMark();
		this.weight = instance.getWeight();
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
		return this.qualityFeatures.get(index);
	}

	@Override
	public float getQuantityFeature(int index) {
		return this.quantityFeatures.get(index);
	}

	@Override
	public HashInstance iterateQualityFeatures(QualityAccessor accessor) {
		for (Int2IntMap.Entry term : this.qualityFeatures.int2IntEntrySet()) {
			accessor.accessorFeature(term.getIntKey(), term.getIntValue());
		}
		return this;
	}

	@Override
	public HashInstance iterateQuantityFeatures(QuantityAccessor accessor) {
		for (Int2FloatMap.Entry term : this.quantityFeatures.int2FloatEntrySet()) {
			accessor.accessorFeature(term.getIntKey(), term.getFloatValue());
		}
		return this;
	}

	@Override
	public int getQualityOrder() {
		return qualityOrder;
	}

	@Override
	public int getQuantityOrder() {
		return quantityOrder;
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
	public float getWeight() {
		return weight;
	}

	@Override
	public void setQualityMark(int qualityMark) {
		this.qualityMark = qualityMark;
	}

	@Override
	public void setQuantityMark(float quantityMark) {
		this.quantityMark = quantityMark;
	}

	@Override
	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setQualityFeature(int index, int value) {
		this.qualityFeatures.put(index, value);
	}

	public void setquantityFeature(int index, float value) {
		this.quantityFeatures.put(index, value);
	}

}
