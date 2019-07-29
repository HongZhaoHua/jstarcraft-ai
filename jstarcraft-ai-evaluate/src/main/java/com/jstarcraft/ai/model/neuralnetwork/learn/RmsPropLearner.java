package com.jstarcraft.ai.model.neuralnetwork.learn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.schedule.ConstantSchedule;
import com.jstarcraft.ai.model.neuralnetwork.schedule.Schedule;
import com.jstarcraft.ai.modem.ModemCycle;
import com.jstarcraft.ai.modem.ModemDefinition;

/**
 * RMS Prop学习器
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * http://www.cs.toronto.edu/~tijmen/csc321/slides/lecture_slides_lec6.pdf
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "rmsDecay", "epsilon", "learnSchedule" })
public class RmsPropLearner implements Learner, ModemCycle {

	public static final float DEFAULT_RMSPROP_LEARN_RATE = 1E-1F;
	public static final float DEFAULT_RMSPROP_EPSILON = 1E-8F;
	public static final float DEFAULT_RMSPROP_RMSDECAY = 0.95F;

	private float rmsDecay;
	private float epsilon;

	private Schedule learnSchedule;

	private Map<String, DenseMatrix> lastGradients;

	public RmsPropLearner() {
		this(DEFAULT_RMSPROP_RMSDECAY, DEFAULT_RMSPROP_EPSILON, new ConstantSchedule(DEFAULT_RMSPROP_LEARN_RATE));
	}

	public RmsPropLearner(float rmsDecay, float epsilon, Schedule learnSchedule) {
		this.rmsDecay = rmsDecay;
		this.epsilon = epsilon;
		this.learnSchedule = learnSchedule;
		this.lastGradients = new HashMap<>();
	}

	@Override
	public void doCache(Map<String, MathMatrix> gradients) {
		lastGradients.clear();
		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			DenseMatrix lastGradient = DenseMatrix.valueOf(gradient.getRowSize(), gradient.getColumnSize());
			lastGradient.setValues(epsilon);
			lastGradients.put(term.getKey(), lastGradient);
		}
	}

	@Override
	public void learn(Map<String, MathMatrix> gradients, int iteration, int epoch) {
		if (lastGradients.isEmpty()) {
			throw new IllegalStateException("Updater has not been initialized with view state");
		}
		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			DenseMatrix lastGradient = lastGradients.get(term.getKey());

			double learnRatio = learnSchedule.valueAt(iteration, epoch);

			lastGradient.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				float delta = gradient.getValue(row, column);
				value = value * rmsDecay + delta * delta * (1F - rmsDecay);
				scalar.setValue(value);
			});

			// lr * gradient / (sqrt(cache) + 1e-8)
			gradient.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				value = (float) (value * (learnRatio / (FastMath.sqrt(lastGradient.getValue(row, column)) + epsilon)));
				scalar.setValue(value);
			});
		}
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterLoad() {
		lastGradients = new HashMap<>();
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
			RmsPropLearner that = (RmsPropLearner) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.rmsDecay, that.rmsDecay);
			equal.append(this.epsilon, that.epsilon);
			equal.append(this.learnSchedule, that.learnSchedule);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(rmsDecay);
		hash.append(epsilon);
		hash.append(learnSchedule);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "RmsPropLearner(rmsDecay=" + rmsDecay + ", epsilon=" + epsilon + ", learnSchedule=" + learnSchedule + ")";
	}

}
