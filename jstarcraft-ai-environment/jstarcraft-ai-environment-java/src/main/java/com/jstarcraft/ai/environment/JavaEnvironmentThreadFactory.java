package com.jstarcraft.ai.environment;

import com.jstarcraft.core.utility.NameThreadFactory;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 环境工厂
 * 
 * @author Birdy
 *
 */
class JavaEnvironmentThreadFactory extends NameThreadFactory {

    private EnvironmentContext context;

    public JavaEnvironmentThreadFactory(EnvironmentContext context) {
        super(context.getClass().getName());
        this.context = context;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        int index = number.getAndIncrement();
        String name = group.getName() + StringUtility.COLON + index;
        Thread thread = new EnvironmentThread(context, group, runnable, name, 0);
        return thread;
    }

}