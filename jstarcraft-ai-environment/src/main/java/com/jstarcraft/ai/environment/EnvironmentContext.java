package com.jstarcraft.ai.environment;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 环境上下文
 * 
 * @author Birdy
 *
 */
public abstract class EnvironmentContext {

    EnvironmentContext() {
    }

    /**
     * 执行任务指令
     * 
     * @param command
     * @return
     */
    abstract public <T> Future<T> doTask(Callable<T> command);

    /**
     * 执行任务指令
     * 
     * @param command
     * @return
     */
    abstract public Future<?> doTask(Runnable command);

    /**
     * 执行算法指令(任一线程)
     * 
     * @param code
     * @param command
     */
    abstract public void doAlgorithmByAny(int code, Runnable command);

    /**
     * 执行算法指令(所有线程)
     * 
     * @param command
     */
    abstract public void doAlgorithmByEvery(Runnable command);

    /**
     * 执行数据结构指令(任一线程)
     * 
     * @param code
     * @param command
     */
    abstract public void doStructureByAny(int code, Runnable command);

    /**
     * 执行数据结构指令(所有线程)
     * 
     * @param command
     */
    abstract public void doStructureByEvery(Runnable command);

    public static EnvironmentContext getContext() {
        EnvironmentThread thread = EnvironmentThread.getThread(EnvironmentThread.class);
        return thread.getContext();
    }

}
