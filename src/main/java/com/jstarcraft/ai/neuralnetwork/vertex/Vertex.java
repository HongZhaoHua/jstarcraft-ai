package com.jstarcraft.ai.neuralnetwork.vertex;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.Model;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 节点
 * 
 * @author Birdy
 *
 */
public interface Vertex extends Model {

	/**
	 * 根据指定的样本分配缓存(每次epoch调用)
	 * 
	 * @param samples
	 */
	void doCache(KeyValue<MathMatrix, MathMatrix>... samples);

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	String getVertexName();

	/**
	 * 获取输入数据与梯度
	 * 
	 * @param position
	 * @return
	 */
	KeyValue<MathMatrix, MathMatrix> getInputKeyValue(int position);

	/**
	 * 获取输出数据与梯度
	 * 
	 * @return
	 */
	KeyValue<MathMatrix, MathMatrix> getOutputKeyValue();

}
