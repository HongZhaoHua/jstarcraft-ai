package com.jstarcraft.ai.data.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.ContinuousAttribute;
import com.jstarcraft.ai.data.attribute.DiscreteAttribute;
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

	public JsonConverter(Collection<DiscreteAttribute> discreteAttributes, Collection<ContinuousAttribute> continuousAttributes) {
		super(discreteAttributes, continuousAttributes);
	}

	@Override
	protected int parseData(DataModule module, BufferedReader buffer) throws IOException {
		int count = 0;
		Int2IntSortedMap discreteFeatures = new Int2IntRBTreeMap();
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
						DiscreteAttribute attribute = discreteAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						int feature = attribute.convertValue((Comparable) value);
						discreteFeatures.put(module.getDiscreteInner(keyValue.getKey()) + index - term.getKey(), feature);
					} else {
						ContinuousAttribute attribute = continuousAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						float feature = attribute.convertValue((Number) value);
						continuousFeatures.put(module.getContinuousInner(keyValue.getKey()) + index - term.getKey(), feature);
					}
				}
				module.associateInstance(discreteFeatures, continuousFeatures);
				discreteFeatures.clear();
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
						DiscreteAttribute attribute = discreteAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						int feature = attribute.convertValue((Comparable) value);
						discreteFeatures.put(module.getDiscreteInner(keyValue.getKey()) + index - term.getKey(), feature);
					} else {
						ContinuousAttribute attribute = continuousAttributes.get(keyValue.getKey());
						value = ConversionUtility.convert(value, attribute.getType());
						float feature = attribute.convertValue((Number) value);
						continuousFeatures.put(module.getContinuousInner(keyValue.getKey()) + index - term.getKey(), feature);
					}
				}
				module.associateInstance(discreteFeatures, continuousFeatures);
				discreteFeatures.clear();
				continuousFeatures.clear();
				count++;
			}
		}
		return count;
	}

}
