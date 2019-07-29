package com.jstarcraft.ai.environment;

/**
 * 环境线程
 * 
 * @author Birdy
 *
 */
public class EnvironmentThread extends Thread {

    private EnvironmentContext context;

    EnvironmentThread(EnvironmentContext context, ThreadGroup group, Runnable runnable, String name, long size) {
        super(group, runnable, name, size);
        this.context = context;
    }

    public EnvironmentContext getContext() {
        return context;
    }

    public static EnvironmentThread currentThread() {
        return EnvironmentThread.class.cast(Thread.currentThread());
    }

}
