package com.jstarcraft.ai.data.attribute;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * 基于内存的离散属性
 * 
 * @author Birdy
 *
 */
public class MemoryQualityAttribute<T extends Comparable<T>> implements QualityAttribute<T> {

	/** 属性名称 */
	private String name;

	/** 属性类型 */
	private Class<T> type;

	/** 外部键-内部索引映射 */
	private Object2IntOpenHashMap<Object> indexes;

	public MemoryQualityAttribute(String name, Class<T> type) {
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
	public synchronized int convertData(T data) {
		int index = indexes.getOrDefault(data, -1);
		if (index == -1) {
			index = indexes.size();
			indexes.put(data, index);
		}
		return index;
	}

	@Override
	public int getSize() {
		return indexes.size();
	}

}
