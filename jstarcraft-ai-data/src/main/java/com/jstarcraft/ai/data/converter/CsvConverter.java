package com.jstarcraft.ai.data.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.core.common.conversion.csv.ConversionUtility;
import com.jstarcraft.core.utility.KeyValue;

import it.unimi.dsi.fastutil.ints.Int2FloatRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * Comma-Separated Values转换器
 * 
 * @author Birdy
 *
 */
public class CsvConverter extends StreamConverter {

    /** 分隔符 */
    protected char delimiter;

    public CsvConverter(char delimiter, Collection<QualityAttribute> qualityAttributes, Collection<QuantityAttribute> quantityAttributes) {
        super(qualityAttributes, quantityAttributes);
        this.delimiter = delimiter;
    }

    @Override
    protected int parseData(DataModule module, BufferedReader buffer, Integer qualityMarkDimension, Integer quantityMarkDimension, Integer weightDimension) throws IOException {
        int count = 0;
        Int2IntSortedMap qualityFeatures = new Int2IntRBTreeMap();
        Int2FloatSortedMap quantityFeatures = new Int2FloatRBTreeMap();
        int size = module.getQualityOrder() + module.getQuantityOrder();
        try (CSVParser parser = new CSVParser(buffer, CSVFormat.newFormat(delimiter))) {
            Iterator<CSVRecord> iterator = parser.iterator();
            while (iterator.hasNext()) {
                CSVRecord datas = iterator.next();
                for (int index = 0; index < size; index++) {
                    Object data = datas.get(index);
                    Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
                    KeyValue<String, Boolean> keyValue = term.getValue();
                    if (keyValue.getValue()) {
                        QualityAttribute attribute = qualityAttributes.get(keyValue.getKey());
                        data = ConversionUtility.convert(data, attribute.getType());
                        int feature = attribute.convertData((Comparable) data);
                        qualityFeatures.put(module.getQualityInner(keyValue.getKey()) + index - term.getKey(), feature);
                    } else {
                        QuantityAttribute attribute = quantityAttributes.get(keyValue.getKey());
                        data = ConversionUtility.convert(data, attribute.getType());
                        float feature = attribute.convertData((Number) data);
                        quantityFeatures.put(module.getQuantityInner(keyValue.getKey()) + index - term.getKey(), feature);
                    }
                }
                int qualityMark = qualityMarkDimension != null ? ConversionUtility.convert(datas.get(qualityMarkDimension), int.class) : DataInstance.defaultInteger;
                float quantityMark = quantityMarkDimension != null ? quantityMark = ConversionUtility.convert(datas.get(quantityMarkDimension), float.class) : DataInstance.defaultFloat;
                float weight = weightDimension != null ? ConversionUtility.convert(datas.get(weightDimension), float.class) : DataInstance.defaultWeight;
                module.associateInstance(qualityFeatures, quantityFeatures, qualityMark, quantityMark, weight);
                qualityFeatures.clear();
                quantityFeatures.clear();
                count++;
            }
            return count;
        } catch (Exception exception) {
            // TODO 处理日志.
            throw new RuntimeException(exception);
        }
    }

}
