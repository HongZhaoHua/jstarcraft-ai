package com.jstarcraft.ai.data.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.core.utility.StringUtility;

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
		while (true) {
			if (mark) {
				count += super.parseData(module, buffer);
				break;
			} else {
				String line = buffer.readLine();
				if (StringUtility.isBlank(line) || line.startsWith("%")) {
					continue;
				}
				String[] datas = line.trim().split("[ \t]");
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
		return count;
	}

}