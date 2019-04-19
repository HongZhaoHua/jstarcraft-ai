package com.jstarcraft.ai.neuralnetwork.learn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.modem.ModemCycle;
import com.jstarcraft.ai.modem.ModemDefinition;
import com.jstarcraft.ai.neuralnetwork.schedule.ConstantSchedule;
import com.jstarcraft.ai.neuralnetwork.schedule.Schedule;

/**
 * Ada Max学习器
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * http://arxiv.org/abs/1412.6980
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "beta1", "beta2", "epsilon", "learnSchedule" })
public class AdaMaxLearner implements Learner, ModemCycle {

	public static final float DEFAULT_ADAMAX_LEARN_RATE = 1E-3F;
	public static final float DEFAULT_ADAMAX_EPSILON = 1E-8F;
	public static final float DEFAULT_ADAMAX_BETA1_MEAN_DECAY = 0.9F;
	public static final float DEFAULT_ADAMAX_BETA2_VAR_DECAY = 0.999F;

	// gradient moving avg decay rate
	private float beta1;
	// gradient sqrd decay rate
	private float beta2;
	private float epsilon;

	private Schedule learnSchedule;

	// moving avg & exponentially weighted infinity norm
	private Map<String, DenseMatrix> ms, us;

	public AdaMaxLearner() {
		this(DEFAULT_ADAMAX_BETA1_MEAN_DECAY, DEFAULT_ADAMAX_BETA2_VAR_DECAY, DEFAULT_ADAMAX_EPSILON, new ConstantSchedule(DEFAULT_ADAMAX_LEARN_RATE));
	}

	public AdaMaxLearner(float beta1, float beta2, float epsilon, Schedule learnSchedule) {
		this.beta1 = beta1;
		this.beta2 = beta2;
		this.epsilon = epsilon;
		this.learnSchedule = learnSchedule;
		this.ms = new HashMap<>();
		this.us = new HashMap<>();
	}

	@Override
	public void doCache(Map<String, MathMatrix> gradients) {
		ms.clear();
		us.clear();
		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			ms.put(term.getKey(), DenseMatrix.valueOf(gradient.getRowSize(), gradient.getColumnSize()));
			us.put(term.getKey(), DenseMatrix.valueOf(gradient.getRowSize(), gradient.getColumnSize()));
		}
	}

	@Override
	public void learn(Map<String, MathMatrix> gradients, int iteration, int epoch) {
		if (ms.isEmpty() || us.isEmpty()) {
			throw new IllegalStateException("Updater has not been initialized with view state");
		}

		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			DenseMatrix m = ms.get(term.getKey());
			DenseMatrix u = us.get(term.getKey());

			// m = B_1 * m + (1-B_1)*grad
			m.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				float delta = gradient.getValue(row, column);
				value = value * beta1 + delta * (1F - beta1);
				scalar.setValue(value);
			});

			// u = max(B_2 * u, |grad|)
			u.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				float delta = gradient.getValue(row, column);
				value = FastMath.max(value * beta2, FastMath.abs(delta));
				// prevent NaNs in params
				scalar.setValue(value + 1E-32F);
			});

			float beta1t = (float) FastMath.pow(beta1, iteration + 1);
			float learnRatio = learnSchedule.valueAt(iteration, epoch);
			float alphat = learnRatio / (1F - beta1t);
			if (Double.isNaN(alphat) || Double.isInfinite(alphat) || alphat == 0F) {
				alphat = epsilon;
			}

			float alpha = alphat;
			gradient.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = m.getValue(row, column) * alpha / u.getValue(row, column);
				scalar.setValue(value);
			});
		}
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterLoad() {
		ms = new HashMap<>();
		us = new HashMap<>();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		} else {
			AdaMaxLearner that = (AdaMaxLearner) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.beta1, that.beta1);
			equal.append(this.beta2, that.beta2);
			equal.append(this.epsilon, that.epsilon);
			equal.append(this.learnSchedule, that.learnSchedule);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(beta1);
		hash.append(beta2);
		hash.append(epsilon);
		hash.append(learnSchedule);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "AdaMaxLearner(beta1=" + beta1 + ", beta2=" + beta2 + ", epsilon=" + epsilon + ", learnSchedule=" + learnSchedule + ")";
	}

}
