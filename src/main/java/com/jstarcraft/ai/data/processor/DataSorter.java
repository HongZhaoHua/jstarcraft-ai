package com.jstarcraft.ai.data.processor;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.module.ReferenceModule;
import com.jstarcraft.ai.utility.IntegerArray;

/**
 * 数据排序器
 * 
 * @author Birdy
 *
 */
public interface DataSorter {

	/**
	 * 排序
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	int sort(DataInstance left, DataInstance right);

	/**
	 * 排序
	 * 
	 * @param module
	 * @return
	 */
	default DataModule sort(DataModule module) {
		if (module.getSize() <= 1) {
			return module;
		}
		int size = module.getSize();
		IntegerArray references = new IntegerArray(size, size);
		for (int index = 0; index < size; index++) {
			references.associateData(index);
		}
		int cursor = 0;
		DataInstance leftInstance = module.getInstance(cursor);
		DataInstance rightInstance = module.getInstance(cursor);
		for (int left = 0; left < size - 1; left++) {
			leftInstance.setCursor(left);
			for (int right = left + 1; right < size; right++) {
				// TODO 注意:此处存在0的情况.
				rightInstance.setCursor(right);
				if (sort(leftInstance, rightInstance) > 0) {
					references.setData(left, references.getData(left) ^ references.getData(right));
					references.setData(right, references.getData(right) ^ references.getData(left));
					references.setData(left, references.getData(left) ^ references.getData(right));
				}
			}
		}
		return new ReferenceModule(references, module);
	}

}
