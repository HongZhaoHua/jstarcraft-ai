package com.jstarcraft.ai.model.neuralnetwork.condition;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 时间限制条件
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class DurationLimitCondition implements Condition {

	private long timeDuration;

	private TimeUnit timeUnit;

	private transient long timeStamp;

	public DurationLimitCondition(long timeDuration, TimeUnit timeUnit) {
		assert timeDuration > 0 && timeUnit != null;
		this.timeDuration = timeDuration;
		this.timeUnit = timeUnit;
	}

	@Override
	public void start() {
		this.timeStamp = TimeUnit.MILLISECONDS.convert(timeDuration, timeUnit);
	}

	@Override
	public boolean stop(double newScore, double oldScore, Map<String, MathMatrix> gradients) {
		return System.currentTimeMillis() >= timeStamp;
	}

}
