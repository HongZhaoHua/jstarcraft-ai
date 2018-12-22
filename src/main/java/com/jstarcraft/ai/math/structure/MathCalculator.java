package com.jstarcraft.ai.math.structure;

import java.util.concurrent.Semaphore;

/**
 * 数学计算器
 * 
 * @author Birdy
 *
 */
public enum MathCalculator {

	/** 串行 */
	SERIAL,

	/** 并行 */
	PARALLEL;

	public final static ThreadLocal<Semaphore> STORAGES = new ThreadLocal<>();

	/**
	 * 获取信号量
	 * 
	 * @return
	 */
	public static Semaphore getSemaphore() {
		Semaphore semaphore = STORAGES.get();
		if (semaphore == null) {
			semaphore = new Semaphore(0);
			STORAGES.set(semaphore);
		}
		return semaphore;
	}

}
