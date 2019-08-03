package com.jstarcraft.ai.data.processor;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.IntegerArray;
import com.jstarcraft.ai.data.module.ReferenceModule;

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
    default ReferenceModule sort(DataModule module) {
        int size = module.getSize();
        if (size <= 1) {
            return new ReferenceModule(module);
        }
        IntegerArray reference = new IntegerArray(size, size);
        for (int index = 0; index < size; index++) {
            reference.associateData(index);
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
                    reference.setData(left, reference.getData(left) ^ reference.getData(right));
                    reference.setData(right, reference.getData(right) ^ reference.getData(left));
                    reference.setData(left, reference.getData(left) ^ reference.getData(right));
                }
            }
        }
        return new ReferenceModule(reference, module);
    }

}
