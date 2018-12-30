package com.jstarcraft.ai.model;

/**
 * 模式周期
 * 
 * @author Birdy
 *
 */
public interface ModelCycle {

	/**
	 * 在模型被保存之前执行
	 */
	void beforeSave();

	/**
	 * 在模型被加载之前执行
	 */
	void afterLoad();

}
