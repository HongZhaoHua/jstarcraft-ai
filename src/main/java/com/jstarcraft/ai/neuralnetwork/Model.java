package com.jstarcraft.ai.neuralnetwork;

/**
 * 网络模型
 * 
 * <pre>
 * 代表神经网络的基本流程:
 * 数据集一般会划分为几个批次的样本集.
 * 每个epoch代表数据集被遍历一次.
 * 每个iteration代表样本集被遍历一次.
 * 
 * 有三种模型:
 * Graph => 多输入多输出
 * Vertex => 多输入单输出
 * Layer => 单输入单输出
 * </pre>
 * 
 * @author Birdy
 *
 */
public interface Model {

	/**
	 * 正向传递(每次iteration调用)
	 */
	void doForward();

	/**
	 * 反向传递(每次iteration调用)
	 */
	void doBackward();

}
