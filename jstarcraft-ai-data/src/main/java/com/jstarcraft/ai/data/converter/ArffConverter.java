package com.jstarcraft.ai.data.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.module.DenseModule;
import com.jstarcraft.ai.data.module.SparseModule;
import com.jstarcraft.core.common.conversion.csv.ConversionUtility;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.StringUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * Attribute-Relation File Format转换器
 * 
 * <pre>
 * ARFF定义(http://www.cs.waikato.ac.nz/ml/weka/arff.html)
 * </pre>
 * 
 * @author Birdy
 *
 */
// TODO 准备支持稀疏数据 https://www.bbsmax.com/A/x9J2RnqeJ6/
public class ArffConverter extends CsvConverter {

    public ArffConverter(Collection<QualityAttribute> qualityAttributes, Collection<QuantityAttribute> quantityAttributes) {
        super(CSVFormat.DEFAULT.getDelimiter(), qualityAttributes, quantityAttributes);
    }

    @Override
    protected int parseData(DataModule module, BufferedReader buffer) throws IOException {
        int count = 0;
        boolean mark = false;
        if (module instanceof DenseModule) {
            while (true) {
                if (mark) {
                    count += super.parseData(module, buffer);
                    break;
                } else {
                    String line = buffer.readLine();
                    if (StringUtility.isBlank(line) || line.startsWith(StringUtility.PERCENT)) {
                        continue;
                    }
                    String[] datas = line.trim().split("\\s+");
                    switch (datas[0].toUpperCase()) {
                    case "@RELATION": {
                        break;
                    }
                    case "@ATTRIBUTE": {
                        break;
                    }
                    case "@DATA": {
                        mark = true;
                        break;
                    }
                    }
                }
            }
        } else if (module instanceof SparseModule) {
            while (true) {
                if (mark) {
                    Int2IntSortedMap qualityFeatures = new Int2IntRBTreeMap();
                    Int2FloatSortedMap quantityFeatures = new Int2FloatRBTreeMap();
                    String line = null;
                    while ((line = buffer.readLine()) != null) {
                        if (StringUtility.isBlank(line)) {
                            // TODO 考虑改为异常或者日志.
                            continue;
                        }
                        line = line.substring(1, line.length() - 1);
                        String[] elements = line.split(",\\s*");
                        for (String element : elements) {
                            String[] data = element.split(StringUtility.SPACE);
                            int index = Integer.parseInt(data[0]) - 1;
                            Object value = data[1];
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
                    break;
                } else {
                    String line = buffer.readLine();
                    if (StringUtility.isBlank(line) || line.startsWith(StringUtility.PERCENT)) {
                        continue;
                    }
                    String[] datas = line.trim().split("\\s+");
                    switch (datas[0].toUpperCase()) {
                    case "@RELATION": {
                        break;
                    }
                    case "@ATTRIBUTE": {
                        break;
                    }
                    case "@DATA": {
                        mark = true;
                        break;
                    }
                    }
                }
            }
        }
        return count;
    }

}