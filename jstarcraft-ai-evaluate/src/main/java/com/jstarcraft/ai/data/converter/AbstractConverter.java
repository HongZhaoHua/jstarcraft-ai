package com.jstarcraft.ai.data.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;

public abstract class AbstractConverter<T> implements DataConverter<T> {

    /** 离散属性映射 */
    protected Map<String, QualityAttribute> qualityAttributes = new HashMap<>();

    /** 连续属性映射 */
    protected Map<String, QuantityAttribute> quantityAttributes = new HashMap<>();

    protected AbstractConverter(Collection<QualityAttribute> qualityAttributes, Collection<QuantityAttribute> quantityAttributes) {
        for (QualityAttribute attribute : qualityAttributes) {
            this.qualityAttributes.put(attribute.getName(), attribute);
        }
        for (QuantityAttribute attribute : quantityAttributes) {
            this.quantityAttributes.put(attribute.getName(), attribute);
        }
    }

}
