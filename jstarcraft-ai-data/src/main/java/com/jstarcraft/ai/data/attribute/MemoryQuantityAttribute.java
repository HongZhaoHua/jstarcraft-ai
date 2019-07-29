package com.jstarcraft.ai.data.attribute;

/**
 * 基于内存的连续属性
 * 
 * @author Birdy
 *
 */
public class MemoryQuantityAttribute<T extends Number> implements QuantityAttribute<T> {

	/** 属性名称 */
	private String name;

	/** 属性类型 */
	private Class<T> type;

	private float maximum;

	private float minimum;

	public MemoryQuantityAttribute(String name, Class<T> type) {
		this.name = name;
		this.type = type;
		this.maximum = Float.NEGATIVE_INFINITY;
		this.minimum = Float.POSITIVE_INFINITY;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

	@Override
	public synchronized float convertData(T data) {
		float feature = data.floatValue();
		if (feature > maximum) {
			maximum = feature;
		}
		if (feature < minimum) {
			minimum = feature;
		}
		return feature;
	}

	@Override
	public float getMaximum() {
		return maximum;
	}

	@Override
	public float getMinimum() {
		return minimum;
	}

}
