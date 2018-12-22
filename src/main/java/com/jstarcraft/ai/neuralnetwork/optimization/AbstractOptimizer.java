package com.jstarcraft.ai.neuralnetwork.optimization;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.condition.Condition;
import com.jstarcraft.ai.neuralnetwork.step.StepFunction;

@ModelDefinition(value = { "stepFunction", "conditions" })
public abstract class AbstractOptimizer implements Optimizer {

	protected static final Logger log = LoggerFactory.getLogger(AbstractOptimizer.class);

	protected StepFunction stepFunction;

	protected Condition[] conditions;

	protected double step;

	protected double oldScore, newScore;

	protected AbstractOptimizer() {
	}

	protected AbstractOptimizer(StepFunction stepFunction, Condition... conditions) {
		this.stepFunction = stepFunction;
		this.conditions = conditions;
		for (Condition condition : conditions) {
			condition.start();
		}
	}

	@Override
	public StepFunction getFunction() {
		return stepFunction;
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
			AbstractOptimizer that = (AbstractOptimizer) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.stepFunction, that.stepFunction);
			equal.append(this.conditions, that.conditions);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(stepFunction);
		hash.append(conditions);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(stepFunction=" + stepFunction + ", terminationConditions=" + conditions + ")";
	}

}
