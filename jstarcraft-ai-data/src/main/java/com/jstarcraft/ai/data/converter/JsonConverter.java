package com.jstarcraft.ai.data.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.module.DenseModule;
import com.jstarcraft.ai.data.module.SparseModule;
import com.jstarcraft.core.common.conversion.ConversionUtility;
import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.reflection.TypeUtility;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.StringUtility;

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

    public JsonConverter(Collection<QualityAttribute> qualityAttributes, Collection<QuantityAttribute> quantityAttributes) {
        super(qualityAttributes, quantityAttributes);
    }

    @Override
    protected int parseData(DataModule module, BufferedReader buffer) throws IOException {
        int count = 0;
        Int2IntSortedMap qualityFeatures = new Int2IntRBTreeMap();
        Int2FloatSortedMap quantityFeatures = new Int2FloatRBTreeMap();
        int size = module.getQualityOrder() + module.getQuantityOrder();
        if (module instanceof DenseModule) {
            String line = null;
            while ((line = buffer.readLine()) != null) {
                if (StringUtility.isBlank(line)) {
                    // TODO 考虑改为异常或者日志.
                    continue;
                }
                ArrayList<Object> datas = JsonUtility.string2Object(line, denseType);
                for (int index = 0; index < size; index++) {
                    Object value = datas.get(index);
                    Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
                    KeyValue<String, Boolean> keyValue = term.getValue();
                    if (keyValue.getValue()) {
                        QualityAttribute attribute = qualityAttributes.get(keyValue.getKey());
                        value = ConversionUtility.convert(value, attribute.getType());
                        int feature = attribute.convertData((Comparable) value);
                        qualityFeatures.put(module.getQualityInner(keyValue.getKey()) + index - term.getKey(), feature);
                    } else {
                        QuantityAttribute attribute = quantityAttributes.get(keyValue.getKey());
                        value = ConversionUtility.convert(value, attribute.getType());
                        float feature = attribute.convertData((Number) value);
                        quantityFeatures.put(module.getQuantityInner(keyValue.getKey()) + index - term.getKey(), feature);
                    }
                }
                module.associateInstance(qualityFeatures, quantityFeatures);
                qualityFeatures.clear();
                quantityFeatures.clear();
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
                        int feature = attribute.convertData((Comparable) value);
                        qualityFeatures.put(module.getQualityInner(keyValue.getKey()) + index - term.getKey(), feature);
                    } else {
                        QuantityAttribute attribute = quantityAttributes.get(keyValue.getKey());
                        value = ConversionUtility.convert(value, attribute.getType());
                        float feature = attribute.convertData((Number) value);
                        quantityFeatures.put(module.getQuantityInner(keyValue.getKey()) + index - term.getKey(), feature);
                    }
                }
                module.associateInstance(qualityFeatures, quantityFeatures);
                qualityFeatures.clear();
                quantityFeatures.clear();
                count++;
            }
        }
        return count;
    }

}
