package com.jstarcraft.ai.environment;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentThread;

public class EnvironmentManagerTestCase {

	private EnvironmentContext[] managers = new EnvironmentContext[] { EnvironmentContext.CPU, EnvironmentContext.GPU };

	@Test
	public void testGetInstance() throws Exception {
		try {
			EnvironmentThread.currentThread();
			Assert.fail();
		} catch (ClassCastException exception) {
		}

		for (EnvironmentContext manager : managers) {
			Future<?> task = manager.doTask(() -> {
				Assert.assertEquals(manager, EnvironmentThread.currentThread().getContext());

				manager.doAlgorithmByEvery(() -> {
					Assert.assertEquals(manager, EnvironmentThread.currentThread().getContext());
				});

				manager.doStructureByEvery(() -> {
					Assert.assertEquals(manager, EnvironmentThread.currentThread().getContext());
				});
			});
			task.get();
		}

		try {
			EnvironmentThread.currentThread();
			Assert.fail();
		} catch (ClassCastException exception) {
		}
	}

}
