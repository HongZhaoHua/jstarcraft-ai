package com.jstarcraft.ai.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jstarcraft.ai.data.DataAttribute;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.data.attribute.MemoryQuantityAttribute;
import com.jstarcraft.ai.data.attribute.MemoryQualityAttribute;
import com.jstarcraft.ai.data.module.DenseModule;
import com.jstarcraft.ai.data.module.SparseModule;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 数据空间
 * 
 * <pre>
 * 配合{@link DataAttribute},{@link DataModule}.
 * </pre>
 * 
 * @author Birdy
 *
 */
public class DataSpace {

	/** 离散属性映射 */
	private Map<String, QualityAttribute> discreteAttributes = new HashMap<>();

	/** 连续属性映射 */
	private Map<String, QuantityAttribute> continuousAttributes = new HashMap<>();

	/** 模型映射 */
	private Map<String, DataModule> modules = new HashMap<>();

	public DataSpace(Map<String, Class<?>> discreteDifinitions, Map<String, Class<?>> continuousDifinitions) {
		for (Entry<String, Class<?>> keyValue : discreteDifinitions.entrySet()) {
			if (continuousAttributes.containsKey(keyValue.getKey())) {
				throw new IllegalArgumentException("属性冲突");
			}
			QualityAttribute attribute = new MemoryQualityAttribute(keyValue.getKey(), keyValue.getValue());
			discreteAttributes.put(attribute.getName(), attribute);
		}
		for (Entry<String, Class<?>> keyValue : continuousDifinitions.entrySet()) {
			if (discreteAttributes.containsKey(keyValue.getKey())) {
				throw new IllegalArgumentException("属性冲突");
			}
			QuantityAttribute attribute = new MemoryQuantityAttribute(keyValue.getKey(), keyValue.getValue());
			continuousAttributes.put(attribute.getName(), attribute);
		}
	}

	public QualityAttribute getDiscreteAttribute(String attributeName) {
		return discreteAttributes.get(attributeName);
	}

	public QuantityAttribute getContinuousAttribute(String attributeName) {
		return continuousAttributes.get(attributeName);
	}

	public Collection<QualityAttribute> getDiscreteAttributes() {
		return discreteAttributes.values();
	}

	public Collection<QuantityAttribute> getContinuousAttributes() {
		return continuousAttributes.values();
	}

	/**
	 * 制作稠密模块
	 * 
	 * @param name
	 * @param configuration
	 * @param capacity
	 * @return
	 */
	public DataModule makeDenseModule(String name, TreeMap<Integer, String> configuration, int capacity) {
		DataModule model = modules.get(name);
		if (model == null) {
			List<KeyValue<KeyValue<String, Boolean>, Integer>> definition = new ArrayList<>(configuration.size());
			int current = 0;
			for (Entry<Integer, String> term : configuration.entrySet()) {
				if (discreteAttributes.containsKey(term.getValue())) {
					KeyValue<KeyValue<String, Boolean>, Integer> keyValue = new KeyValue<>(new KeyValue<>(term.getValue(), true), term.getKey() - current);
					definition.add(keyValue);
					current = term.getKey();
					continue;
				}
				if (continuousAttributes.containsKey(term.getValue())) {
					KeyValue<KeyValue<String, Boolean>, Integer> keyValue = new KeyValue<>(new KeyValue<>(term.getValue(), false), term.getKey() - current);
					definition.add(keyValue);
					current = term.getKey();
					continue;
				}
				throw new IllegalArgumentException("属性缺失");
			}
			model = new DenseModule(name, definition, capacity);
			modules.put(name, model);
			return model;
		} else {
			throw new IllegalArgumentException("模型冲突");
		}
	}

	/**
	 * 制作稀疏模块
	 * 
	 * @param name
	 * @param configuration
	 * @param capacity
	 * @return
	 */
	public DataModule makeSparseModule(String name, TreeMap<Integer, String> configuration, int capacity) {
		DataModule model = modules.get(name);
		if (model == null) {
			List<KeyValue<KeyValue<String, Boolean>, Integer>> definition = new ArrayList<>(configuration.size());
			int current = 0;
			for (Entry<Integer, String> term : configuration.entrySet()) {
				if (discreteAttributes.containsKey(term.getValue())) {
					KeyValue<KeyValue<String, Boolean>, Integer> keyValue = new KeyValue<>(new KeyValue<>(term.getValue(), true), term.getKey() - current);
					definition.add(keyValue);
					current = term.getKey();
					continue;
				}
				if (continuousAttributes.containsKey(term.getValue())) {
					KeyValue<KeyValue<String, Boolean>, Integer> keyValue = new KeyValue<>(new KeyValue<>(term.getValue(), false), term.getKey() - current);
					definition.add(keyValue);
					current = term.getKey();
					continue;
				}
				throw new IllegalArgumentException("属性缺失");
			}
			model = new SparseModule(name, definition, capacity);
			modules.put(name, model);
			return model;
		} else {
			throw new IllegalArgumentException("模型冲突");
		}
	}

	/**
	 * 获取数据模块
	 * 
	 * @param moduleName
	 * @return
	 */
	public DataModule getModule(String moduleName) {
		return modules.get(moduleName);
	}

}
