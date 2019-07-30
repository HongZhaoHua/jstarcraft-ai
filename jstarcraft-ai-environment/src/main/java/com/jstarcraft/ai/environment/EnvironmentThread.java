package com.jstarcraft.ai.environment;

/**
 * 环境线程
 * 
 * @author Birdy
 *
 */
public abstract class EnvironmentThread extends Thread {

    EnvironmentThread(ThreadGroup group, Runnable runnable, String name, long size) {
        super(group, runnable, name, size);
    }

    abstract public EnvironmentContext getContext();

    public static <T extends EnvironmentThread> T getThread(Class<T> clazz) {
        return clazz.cast(Thread.currentThread());
    }

}
