package com.jstarcraft.ai.data.processor;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.module.ReferenceModule;
import com.jstarcraft.ai.utility.IntegerArray;

/**
 * 数据选择器
 * 
 * @author Birdy
 *
 */
public interface DataSelector {

	/**
	 * 选择
	 * 
	 * @param instance
	 * @return
	 */
	boolean select(DataInstance instance);

	/**
	 * 选择
	 * 
	 * @param module
	 * @return
	 */
	default DataModule select(DataModule module) {
		if (module.getSize() == 0) {
			return module;
		}
		int size = module.getSize();
		int maximum = size;
		int minimum = 1000;
		if (maximum < minimum) {
			maximum = minimum;
		}
		IntegerArray reference = new IntegerArray(minimum, maximum);
		int cursor = 0;
		DataInstance instance = module.getInstance(cursor);
		while (cursor < size) {
			instance.setCursor(cursor);
			if (select(instance)) {
				reference.associateData(cursor);
			}
			cursor++;
		}
		return new ReferenceModule(reference, module);
	}

}
