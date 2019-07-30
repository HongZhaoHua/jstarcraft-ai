package com.jstarcraft.ai.environment;

/**
 * 环境线程
 * 
 * @author Birdy
 *
 */
public class JavaEnvironmentThread extends EnvironmentThread {

    private EnvironmentContext context;

    JavaEnvironmentThread(EnvironmentContext context, ThreadGroup group, Runnable runnable, String name, long size) {
        super(group, runnable, name, size);
        this.context = context;
    }

    @Override
    public EnvironmentContext getContext() {
        return context;
    }

}
