package com.jstarcraft.ai.data.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.data.module.DenseModule;
import com.jstarcraft.ai.data.module.SparseModule;
import com.jstarcraft.core.utility.ConversionUtility;
import com.jstarcraft.core.utility.JsonUtility;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.core.utility.TypeUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * JavaScript Object Notation转换器
 * 
 * @author Birdy
 *
 */
public class JsonConverter extends StreamConverter {

	private final static Type denseType = TypeUtility.parameterize(ArrayList.class, Object.class);

	private final static Type sparseType = TypeUtility.parameterize(Int2ObjectOpenHashMap.class, Object.class);

	public JsonConverter(Collection<QualityAttribute> qualityAttributes, Collection<QuantityAttribute> continuousAttributes) {
		super(qualityAttributes, continuousAttributes);
	}

	@Override
	protected int parseData(DataModule module, BufferedReader buffer) throws IOException {
		int count = 0;
		Int2IntSortedMap qualityFeatures = new Int2IntRBTreeMap();
		Int2FloatSortedMap continuousFeatures = new Int2FloatRBTreeMap();
		if (module instanceof DenseModule) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				if (StringUtility.isBlank(line)) {
					// TODO 考虑改为异常或者日志.
					continue;
				}
				ArrayList<Object> datas = JsonUtility.string2Object(line, denseType);
				for (int index = 0, size = datas.size(); index < size; index++) {
					Object value = datas.get(index);
					Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
					KeyValue<String, Boolean> keyValue = term.getValue();
					if (keyValue.getValue()) {
						QualityAttribute attribute = qualityAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						int feature = attribute.convertValue((Comparable) value);
						qualityFeatures.put(module.getQualityInner(keyValue.getKey()) + index - term.getKey(), feature);
					} else {
						QuantityAttribute attribute = continuousAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						float feature = attribute.convertValue((Number) value);
						continuousFeatures.put(module.getContinuousInner(keyValue.getKey()) + index - term.getKey(), feature);
					}
				}
				module.associateInstance(qualityFeatures, continuousFeatures);
				qualityFeatures.clear();
				continuousFeatures.clear();
				count++;
			}
		} else if (module instanceof SparseModule) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				if (StringUtility.isBlank(line)) {
					// TODO 考虑改为异常或者日志.
					continue;
				}
				Int2ObjectOpenHashMap<Object> datas = JsonUtility.string2Object(line, sparseType);
				for (Int2ObjectMap.Entry<Object> element : datas.int2ObjectEntrySet()) {
					int index = element.getIntKey();
					Object value = element.getValue();
					Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
					KeyValue<String, Boolean> keyValue = term.getValue();
					if (keyValue.getValue()) {
						QualityAttribute attribute = qualityAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						int feature = attribute.convertValue((Comparable) value);
						qualityFeatures.put(module.getQualityInner(keyValue.getKey()) + index - term.getKey(), feature);
					} else {
						QuantityAttribute attribute = continuousAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						float feature = attribute.convertValue((Number) value);
						continuousFeatures.put(module.getContinuousInner(keyValue.getKey()) + index - term.getKey(), feature);
					}
				}
				module.associateInstance(qualityFeatures, continuousFeatures);
				qualityFeatures.clear();
				continuousFeatures.clear();
				count++;
			}
		}
		return count;
	}

}
