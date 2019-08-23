package com.jstarcraft.ai.math.structure.tensor;

import com.jstarcraft.ai.math.structure.MathScalar;

public interface TensorScalar extends MathScalar {

    /**
     * 获取标量所在维度的索引
     * 
     * @return
     */
    int getIndex(int dimension);

}
