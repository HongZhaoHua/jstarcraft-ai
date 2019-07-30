package com.jstarcraft.ai.environment;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.core.utility.HashUtility;

/**
 * Java环境上下文
 * 
 * @author Birdy
 *
 */
class JavaEnvironmentContext extends EnvironmentContext {

	static final JavaEnvironmentContext INSTANCE;

	static {
		INSTANCE = new JavaEnvironmentContext();
		INSTANCE.numberOfThreads = Runtime.getRuntime().availableProcessors();
		{
			int numberOfTasks = 1;
			JavaEnvironmentThreadFactory factory = new JavaEnvironmentThreadFactory(INSTANCE);
			INSTANCE.taskExecutor = Executors.newFixedThreadPool(numberOfTasks, factory);
		}
		{
			JavaEnvironmentThreadFactory factory = new JavaEnvironmentThreadFactory(INSTANCE);
			INSTANCE.algorithmExecutors = new ExecutorService[INSTANCE.numberOfThreads];
			for (int threadIndex = 0; threadIndex < INSTANCE.numberOfThreads; threadIndex++) {
				INSTANCE.algorithmExecutors[threadIndex] = Executors.newSingleThreadExecutor(factory);
			}
		}
		{
			JavaEnvironmentThreadFactory factory = new JavaEnvironmentThreadFactory(INSTANCE);
			INSTANCE.structureExecutors = new ExecutorService[INSTANCE.numberOfThreads];
			for (int threadIndex = 0; threadIndex < INSTANCE.numberOfThreads; threadIndex++) {
				INSTANCE.structureExecutors[threadIndex] = Executors.newSingleThreadExecutor(factory);
			}
		}
	}

	private int numberOfThreads;

	private ExecutorService taskExecutor;

	private ExecutorService[] algorithmExecutors;

	private ExecutorService[] structureExecutors;

	private JavaEnvironmentContext() {
	}

	@Override
	public Future<?> doTask(Runnable command) {
		Future<?> task = taskExecutor.submit(() -> {
		    command.run();
			// 必须触发垃圾回收.
			System.gc();
		});
		return task;
	}

	@Override
	public void doAlgorithmByAny(int code, Runnable command) {
		int threadIndex = FastMath.abs(HashUtility.twNumberHash32(code)) % numberOfThreads;
		algorithmExecutors[threadIndex].execute(command);
	}

	@Override
	public synchronized void doAlgorithmByEvery(Runnable command) {
		try {
			for (int threadIndex = 0; threadIndex < numberOfThreads; threadIndex++) {
				algorithmExecutors[threadIndex].submit(() -> {
					command.run();
				}).get();
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void doStructureByAny(int code, Runnable command) {
		int threadIndex = FastMath.abs(HashUtility.twNumberHash32(code)) % numberOfThreads;
		structureExecutors[threadIndex].execute(command);
	}

	@Override
	public synchronized void doStructureByEvery(Runnable command) {
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		for (int threadIndex = 0; threadIndex < numberOfThreads; threadIndex++) {
			structureExecutors[threadIndex].execute(() -> {
				command.run();
				latch.countDown();
			});
		}
		try {
			latch.await();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

}
