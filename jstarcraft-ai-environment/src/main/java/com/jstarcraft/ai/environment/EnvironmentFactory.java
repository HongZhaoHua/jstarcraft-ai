package com.jstarcraft.ai.environment;

/**
 * 环境工厂
 * 
 * @author Birdy
 *
 */
public class EnvironmentFactory {

    public final static String AFFINITY_MANAGER = "org.nd4j.linalg.api.concurrency.AffinityManager";

    public final static String CPU_AFFINITY_MANAGER = "org.nd4j.linalg.cpu.nativecpu.CpuAffinityManager";

    public final static String GPU_AFFINITY_MANAGER = "org.nd4j.jita.concurrency.CudaAffinityManager";

    public static final EnvironmentContext CPU = CpuEnvironmentContext.INSTANCE;

    public static final EnvironmentContext GPU = GpuEnvironmentContext.INSTANCE;

    public static final EnvironmentContext JAVA = JavaEnvironmentContext.INSTANCE;

    public final static EnvironmentContext getContext() {
        try {
            Class.forName(AFFINITY_MANAGER);
            try {
                Class.forName(CPU_AFFINITY_MANAGER);
                return CPU;
            } catch (Exception exception) {
            }
            try {
                Class.forName(GPU_AFFINITY_MANAGER);
                return GPU;
            } catch (Exception exception) {
            }
        } catch (Exception exception) {
        }

        return JAVA;
    }

}
