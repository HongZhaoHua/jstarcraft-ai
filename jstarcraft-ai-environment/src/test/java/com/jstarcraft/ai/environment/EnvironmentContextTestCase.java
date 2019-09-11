package com.jstarcraft.ai.environment;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

public class EnvironmentContextTestCase {

    private EnvironmentContext[] managers = new EnvironmentContext[] { EnvironmentFactory.CPU, EnvironmentFactory.GPU, EnvironmentFactory.JAVA };

    @Test
    public void testGetInstance() throws Exception {
        try {
            EnvironmentThread.getThread(EnvironmentThread.class);
            Assert.fail();
        } catch (ClassCastException exception) {
        }

        for (EnvironmentContext manager : managers) {
            Future<?> task = manager.doTask(() -> {
                Assert.assertEquals(manager, EnvironmentThread.getThread(EnvironmentThread.class).getContext());

                manager.doAlgorithmByEvery(() -> {
                    Assert.assertEquals(manager, EnvironmentThread.getThread(EnvironmentThread.class).getContext());
                });

                manager.doStructureByEvery(() -> {
                    Assert.assertEquals(manager, EnvironmentThread.getThread(EnvironmentThread.class).getContext());
                });
            });
            task.get();
        }

        try {
            EnvironmentThread.getThread(EnvironmentThread.class);
            Assert.fail();
        } catch (ClassCastException exception) {
        }
    }

}
