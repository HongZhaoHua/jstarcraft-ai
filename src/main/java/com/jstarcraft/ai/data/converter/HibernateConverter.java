package com.jstarcraft.ai.data.converter;

import java.util.Collection;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.ContinuousAttribute;
import com.jstarcraft.ai.data.attribute.DiscreteAttribute;
import com.jstarcraft.core.utility.ConversionUtility;
import com.jstarcraft.core.utility.KeyValue;

import it.unimi.dsi.fastutil.ints.Int2FloatRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * Hibernate转换器
 * 
 * @author Birdy
 *
 */
public class HibernateConverter extends AbstractConverter<ScrollableResults> {

	protected HibernateConverter(Collection<DiscreteAttribute> discreteAttributes, Collection<ContinuousAttribute> continuousAttributes) {
		super(discreteAttributes, continuousAttributes);
	}

	@Override
	public int convert(DataModule module, ScrollableResults iterator) {
		int count = 0;
		Int2IntSortedMap discreteFeatures = new Int2IntRBTreeMap();
		Int2FloatSortedMap continuousFeatures = new Int2FloatRBTreeMap();
		try {
			int size = module.getDiscreteOrder() + module.getContinuousOrder();
			while (iterator.next()) {
				for (int index = 0; index < size; index++) {
					Object data = iterator.get(index);
					if (data == null) {
						continue;
					}
					Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
					KeyValue<String, Boolean> keyValue = term.getValue();
					if (keyValue.getValue()) {
						DiscreteAttribute attribute = discreteAttributes.get(keyValue.getKey());
						data = ConversionUtility.convert(data, attribute.getType());
						int feature = attribute.convertValue((Comparable) data);
						discreteFeatures.put(module.getDiscreteInner(keyValue.getKey()) + index - term.getKey(), feature);
					} else {
						ContinuousAttribute attribute = continuousAttributes.get(keyValue.getKey());
						data = ConversionUtility.convert(data, attribute.getType());
						float feature = attribute.convertValue((Number) data);
						continuousFeatures.put(module.getContinuousInner(keyValue.getKey()) + index - term.getKey(), feature);
					}
				}
				module.associateInstance(discreteFeatures, continuousFeatures);
				discreteFeatures.clear();
				continuousFeatures.clear();
				count++;
			}
		} catch (HibernateException exception) {
			// TODO 处理日志.
			throw new RuntimeException(exception);
		}
		return count;
	}

}