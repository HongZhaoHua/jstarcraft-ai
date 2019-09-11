package com.jstarcraft.ai.model.neuralnetwork.layer;

import java.util.Map;

import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.Model;
import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 层
 * 
 * @author Birdy
 *
 */
public interface Layer extends Model {

    /**
     * 根据指定的样本分配缓存(每次epoch调用)
     * 
     * @param factory
     * @param samples
     */
    void doCache(MathCache factory, KeyValue<MathMatrix, MathMatrix> samples);

    /**
     * 获取输入数据与梯度
     * 
     * @return
     */
    KeyValue<MathMatrix, MathMatrix> getInputKeyValue();

    /**
     * 获取中间数据与梯度
     * 
     * @return
     */
    KeyValue<MathMatrix, MathMatrix> getMiddleKeyValue();

    /**
     * 获取输出数据与梯度
     * 
     * @return
     */
    KeyValue<MathMatrix, MathMatrix> getOutputKeyValue();

    /**
     * 计算L1范数
     * 
     * @return
     */
    float calculateL1Norm();

    /**
     * 计算L2范数
     * 
     * @return
     */
    float calculateL2Norm();

    /**
     * 正则化
     */
    void regularize();

    /**
     * 获取所有参数
     * 
     * @return
     */
    Map<String, MathMatrix> getParameters();

    /**
     * 获取所有梯度
     * 
     * @return
     */
    Map<String, MathMatrix> getGradients();

    /**
     * 获取激活函数
     * 
     * @return
     */
    ActivationFunction getFunction();

}
