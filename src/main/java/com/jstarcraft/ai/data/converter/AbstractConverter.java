package com.jstarcraft.ai.data.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.attribute.QualityAttribute;

public abstract class AbstractConverter<T> implements DataConverter<T> {

	/** 离散属性映射 */
	protected Map<String, QualityAttribute> discreteAttributes = new HashMap<>();

	/** 连续属性映射 */
	protected Map<String, QuantityAttribute> continuousAttributes = new HashMap<>();

	protected AbstractConverter(Collection<QualityAttribute> discreteAttributes, Collection<QuantityAttribute> continuousAttributes) {
		for (QualityAttribute attribute : discreteAttributes) {
			this.discreteAttributes.put(attribute.getName(), attribute);
		}
		for (QuantityAttribute attribute : continuousAttributes) {
			this.continuousAttributes.put(attribute.getName(), attribute);
		}
	}

}
