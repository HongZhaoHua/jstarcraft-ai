package com.jstarcraft.ai.data.converter;

import java.util.Collection;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
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

	protected HibernateConverter(Collection<QualityAttribute> qualityAttributes, Collection<QuantityAttribute> continuousAttributes) {
		super(qualityAttributes, continuousAttributes);
	}

	@Override
	public int convert(DataModule module, ScrollableResults iterator) {
		int count = 0;
		Int2IntSortedMap qualityFeatures = new Int2IntRBTreeMap();
		Int2FloatSortedMap continuousFeatures = new Int2FloatRBTreeMap();
		try {
			int size = module.getQualityOrder() + module.getContinuousOrder();
			while (iterator.next()) {
				for (int index = 0; index < size; index++) {
					Object data = iterator.get(index);
					if (data == null) {
						continue;
					}
					Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
					KeyValue<String, Boolean> keyValue = term.getValue();
					if (keyValue.getValue()) {
						QualityAttribute attribute = qualityAttributes.get(keyValue.getKey());
						data = ConversionUtility.convert(data, attribute.getType());
						int feature = attribute.convertValue((Comparable) data);
						qualityFeatures.put(module.getQualityInner(keyValue.getKey()) + index - term.getKey(), feature);
					} else {
						QuantityAttribute attribute = continuousAttributes.get(keyValue.getKey());
						data = ConversionUtility.convert(data, attribute.getType());
						float feature = attribute.convertValue((Number) data);
						continuousFeatures.put(module.getContinuousInner(keyValue.getKey()) + index - term.getKey(), feature);
					}
				}
				module.associateInstance(qualityFeatures, continuousFeatures);
				qualityFeatures.clear();
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