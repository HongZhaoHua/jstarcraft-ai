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
 * Ada Grad学习器
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * http://xcorr.net/2014/01/23/adagrad-eliminating-learning-rates-in-stochastic-gradient-descent/
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "epsilon", "learnSchedule" })
public class AdaGradLearner implements Learner, ModemCycle {

	public static final float DEFAULT_ADAGRAD_LEARN_RATE = 1E-1F;
	public static final float DEFAULT_ADAGRAD_EPSILON = 1E-6F;

	private float epsilon;

	private Schedule learnSchedule;

	private Map<String, DenseMatrix> historicalGradients;

	public AdaGradLearner() {
		this(DEFAULT_ADAGRAD_EPSILON, new ConstantSchedule(DEFAULT_ADAGRAD_LEARN_RATE));
	}

	public AdaGradLearner(float epsilon, Schedule learnSchedule) {
		this.epsilon = epsilon;
		this.learnSchedule = learnSchedule;
		this.historicalGradients = new HashMap<>();
	}

	@Override
	public void doCache(Map<String, MathMatrix> gradients) {
		historicalGradients.clear();
		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			DenseMatrix historicalGradient = DenseMatrix.valueOf(gradient.getRowSize(), gradient.getColumnSize());
			historicalGradient.setValues(epsilon);
			historicalGradients.put(term.getKey(), historicalGradient);
		}
	}

	@Override
	public void learn(Map<String, MathMatrix> gradients, int iteration, int epoch) {
		if (historicalGradients.isEmpty()) {
			throw new IllegalStateException("Updater has not been initialized with view state");
		}
		for (Entry<String, MathMatrix> term : gradients.entrySet()) {
			MathMatrix gradient = term.getValue();
			DenseMatrix historicalGradient = historicalGradients.get(term.getKey());
			double learnRatio = learnSchedule.valueAt(iteration, epoch);
			historicalGradient.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				float delta = gradient.getValue(row, column);
				value = value + delta * delta;
				scalar.setValue(value);
			});

			// lr * gradient / (sqrt(sumSquaredGradients) + epsilon)
			gradient.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				value = (float) (value * (learnRatio / (FastMath.sqrt(historicalGradient.getValue(row, column)) + epsilon)));
				scalar.setValue(value);
			});
		}
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterLoad() {
		historicalGradients = new HashMap<>();
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
			AdaGradLearner that = (AdaGradLearner) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.epsilon, that.epsilon);
			equal.append(this.learnSchedule, that.learnSchedule);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(epsilon);
		hash.append(learnSchedule);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "AdaGradLearner(epsilon=" + epsilon + ", learnSchedule=" + learnSchedule + ")";
	}

}
