package com.jstarcraft.ai.environment;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.core.utility.HashUtility;

/**
 * CPU环境上下文
 * 
 * @author Birdy
 *
 */
class CpuEnvironmentContext extends EnvironmentContext {

    static final CpuEnvironmentContext INSTANCE;

    static {
        INSTANCE = new CpuEnvironmentContext();
        INSTANCE.numberOfThreads = Runtime.getRuntime().availableProcessors();
        {
            int numberOfTasks = 1;
            Nd4jEnvironmentThreadFactory factory = new Nd4jEnvironmentThreadFactory(INSTANCE);
            INSTANCE.taskExecutor = Executors.newFixedThreadPool(numberOfTasks, factory);
        }
        {
            Nd4jEnvironmentThreadFactory factory = new Nd4jEnvironmentThreadFactory(INSTANCE);
            INSTANCE.algorithmExecutors = new ExecutorService[INSTANCE.numberOfThreads];
            for (int threadIndex = 0; threadIndex < INSTANCE.numberOfThreads; threadIndex++) {
                INSTANCE.algorithmExecutors[threadIndex] = Executors.newSingleThreadExecutor(factory);
            }
        }
        {
            Nd4jEnvironmentThreadFactory factory = new Nd4jEnvironmentThreadFactory(INSTANCE);
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

    private CpuEnvironmentContext() {
    }

    @Override
    public <T> Future<T> doTask(Callable<T> command) {
        Future<T> task = taskExecutor.submit(() -> {
            int size = 1024 * 1024 * 10;
            {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.constructCache(size);
            }
            doAlgorithmByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.constructCache(size);
            });
            doStructureByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.constructCache(size);
            });
            T value = command.call();
            doStructureByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.destroyCache();
            });
            doAlgorithmByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.destroyCache();
            });
            {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.destroyCache();
            }
            // 必须触发垃圾回收.
            System.gc();
            return value;
        });
        return task;
    }

    @Override
    public Future<?> doTask(Runnable command) {
        Future<?> task = taskExecutor.submit(() -> {
            int size = 1024 * 1024 * 10;
            {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.constructCache(size);
            }
            doAlgorithmByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.constructCache(size);
            });
            doStructureByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.constructCache(size);
            });
            command.run();
            doStructureByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.destroyCache();
            });
            doAlgorithmByEvery(() -> {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.destroyCache();
            });
            {
                Nd4jEnvironmentThread thread = EnvironmentThread.getThread(Nd4jEnvironmentThread.class);
                thread.destroyCache();
            }
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
