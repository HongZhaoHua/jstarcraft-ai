package com.jstarcraft.ai.data.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jstarcraft.ai.data.attribute.ContinuousAttribute;
import com.jstarcraft.ai.data.attribute.DiscreteAttribute;

public abstract class AbstractConverter<T> implements DataConverter<T> {

	/** 离散属性映射 */
	protected Map<String, DiscreteAttribute> discreteAttributes = new HashMap<>();

	/** 连续属性映射 */
	protected Map<String, ContinuousAttribute> continuousAttributes = new HashMap<>();

	protected AbstractConverter(Collection<DiscreteAttribute> discreteAttributes, Collection<ContinuousAttribute> continuousAttributes) {
		for (DiscreteAttribute attribute : discreteAttributes) {
			this.discreteAttributes.put(attribute.getName(), attribute);
		}
		for (ContinuousAttribute attribute : continuousAttributes) {
			this.continuousAttributes.put(attribute.getName(), attribute);
		}
	}

}
