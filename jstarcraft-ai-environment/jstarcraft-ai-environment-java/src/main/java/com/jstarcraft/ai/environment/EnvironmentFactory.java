package com.jstarcraft.ai.environment;

/**
 * 环境工厂
 * 
 * @author Birdy
 *
 */
public class EnvironmentFactory {

    public static final EnvironmentContext JAVA = JavaEnvironmentContext.INSTANCE;

    public final static EnvironmentContext getContext() {
        return JAVA;
    }

}
