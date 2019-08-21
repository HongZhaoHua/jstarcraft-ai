package com.jstarcraft.ai.environment;

import org.nd4j.linalg.api.concurrency.AffinityManager;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.core.utility.NameThreadFactory;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 环境工厂
 * 
 * @author Birdy
 *
 */
class Nd4jEnvironmentThreadFactory extends NameThreadFactory {

	private static final AffinityManager manager = Nd4j.getAffinityManager();

	private EnvironmentContext context;

	public Nd4jEnvironmentThreadFactory(EnvironmentContext context) {
		super(context.getClass().getName());
		this.context = context;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		int index = number.getAndIncrement();
		String name = group.getName() + StringUtility.COLON + index;
		Thread thread = new EnvironmentThread(context, group, runnable, name, 0);
		manager.attachThreadToDevice(thread, index % manager.getNumberOfDevices());
		return thread;
	}

}