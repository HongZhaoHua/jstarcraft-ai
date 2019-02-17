package com.jstarcraft.ai.data.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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
 * Comma-Separated Values转换器
 * 
 * @author Birdy
 *
 */
public class CsvConverter extends StreamConverter {

	/** 分隔符 */
	protected char delimiter;

	public CsvConverter(char delimiter, Collection<DiscreteAttribute> discreteAttributes, Collection<ContinuousAttribute> continuousAttributes) {
		super(discreteAttributes, continuousAttributes);
		this.delimiter = delimiter;
	}

	@Override
	protected int parseData(DataModule module, BufferedReader buffer) throws IOException {
		int count = 0;
		Int2IntSortedMap discreteFeatures = new Int2IntRBTreeMap();
		Int2FloatSortedMap continuousFeatures = new Int2FloatRBTreeMap();
		try (CSVParser parser = new CSVParser(buffer, CSVFormat.newFormat(delimiter))) {
			Iterator<CSVRecord> iterator = parser.iterator();
			while (iterator.hasNext()) {
				CSVRecord datas = iterator.next();
				for (int index = 0, size = datas.size(); index < size; index++) {
					Object data = datas.get(index);
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
			return count;
		} catch (Exception exception) {
			// TODO 处理日志.
			throw new RuntimeException(exception);
		}
	}

}
