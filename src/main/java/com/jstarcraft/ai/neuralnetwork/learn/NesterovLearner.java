package com.jstarcraft.ai.neuralnetwork.learn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.ModelCycle;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.schedule.ConstantSchedule;
import com.jstarcraft.ai.neuralnetwork.schedule.Schedule;

/**
 * Nesterov学习器
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "learnSchedule", "momentumSchedule" })
public class NesterovLearner implements Learner, ModelCycle {

	public static final float DEFAULT_NESTEROV_LEARN_RATE = 0.1F;
	public static final float DEFAULT_NESTEROV_MOMENTUM = 0.9F;

	private Schedule learnSchedule;
	private Schedule momentumSchedule;

	private Map<String, DenseMatrix> vs;
	private Map<String, DenseMatrix> cs;

	public NesterovLearner() {
		this(new ConstantSchedule(DEFAULT_NESTEROV_LEARN_RATE), new ConstantSchedule(DEFAULT_NESTEROV_MOMENTUM));
	}

	public NesterovLearner(Schedule learnSchedule) {
		this(learnSchedule, new ConstantSchedule(DEFAULT_NESTEROV_MOMENTUM));
	}

	public NesterovLearner(Schedule learnSchedule, Schedule momentumSchedule) {
		this.learnSchedule = learnSchedule;
		this.momentumSchedule = momentumSchedule;
		this.vs = new HashMap<>();
		this.cs = new HashMap<>();
	}

	@Override
	public void doCache(Map<String, MathMatrix> gradients) {
		vs.clear();
		cs.clear();
		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			vs.put(term.getKey(), DenseMatrix.valueOf(gradient.getRowSize(), gradient.getColumnSize()));
			cs.put(term.getKey(), DenseMatrix.valueOf(gradient.getRowSize(), gradient.getColumnSize()));
		}
	}

	@Override
	public void learn(Map<String, MathMatrix> gradients, int iteration, int epoch) {
		if (vs.isEmpty()) {
			throw new IllegalStateException("Updater has not been initialized with view state");
		}
		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			DenseMatrix v = vs.get(term.getKey());
			DenseMatrix c = cs.get(term.getKey());
			float momentum = momentumSchedule.valueAt(iteration, epoch);
			float learnRatio = learnSchedule.valueAt(iteration, epoch);

			// reference https://cs231n.github.io/neural-networks-3/#sgd 2nd
			// equation
			// DL4J default is negative step function thus we flipped the signs:
			// x += mu * v_prev + (-1 - mu) * v
			// i.e., we do params -= updatedGradient, not params +=
			// updatedGradient

			// v = mu * v - lr * gradient
			c.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				scalar.setValue(v.getValue(row, column));
			});

			v.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				float delta = gradient.getValue(row, column);
				value = value * momentum - delta * learnRatio;
				scalar.setValue(value);
			});

			gradient.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float left = c.getValue(row, column) * momentum;
				float right = v.getValue(row, column) * (-momentum - 1F);
				scalar.setValue(left + right);
			});
		}
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterLoad() {
		vs = new HashMap<>();
		cs = new HashMap<>();
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
			NesterovLearner that = (NesterovLearner) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.learnSchedule, that.learnSchedule);
			equal.append(this.momentumSchedule, that.momentumSchedule);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(learnSchedule);
		hash.append(momentumSchedule);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "NesterovLearner(learnSchedule=" + learnSchedule + ", momentumSchedule=" + momentumSchedule + ")";
	}

}
