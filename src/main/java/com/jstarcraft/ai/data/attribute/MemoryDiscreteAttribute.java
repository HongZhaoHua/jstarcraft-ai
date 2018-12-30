package com.jstarcraft.ai.data.attribute;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * 基于内存的离散属性
 * 
 * @author Birdy
 *
 */
public class MemoryDiscreteAttribute<T extends Comparable<T>> implements DiscreteAttribute<T> {

	/** 属性名称 */
	private String name;

	/** 属性类型 */
	private Class<T> type;

	/** 外部键-内部索引映射 */
	private Object2IntOpenHashMap<Object> indexes;

	MemoryDiscreteAttribute(String name, Class<T> type) {
		this.name = name;
		this.type = type;
		this.indexes = new Object2IntOpenHashMap<>();
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
	public synchronized int convertValue(T value) {
		int index = indexes.getOrDefault(value, -1);
		if (index == -1) {
			index = indexes.size();
			indexes.put(value, index);
		}
		return index;
	}

	public int getSize() {
		return indexes.size();
	}

}
