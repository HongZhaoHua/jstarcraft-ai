package com.jstarcraft.ai.neuralnetwork.layer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.parameter.IgnoreParameterFactory;
import com.jstarcraft.ai.neuralnetwork.parameter.ParameterFactory;

/**
 * 参数配置器用于初始化与正则化
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "l1Regularization", "l2Regularization", "factory" })
public class ParameterConfigurator {

	private float l1Regularization;

	private float l2Regularization;

	private ParameterFactory factory;

	ParameterConfigurator() {
	}

	public ParameterConfigurator(float l1Regularization, float l2Regularization) {
		this(l1Regularization, l2Regularization, new IgnoreParameterFactory());
	}

	public ParameterConfigurator(float l1Regularization, float l2Regularization, ParameterFactory factory) {
		this.l1Regularization = l1Regularization;
		this.l2Regularization = l2Regularization;
		this.factory = factory;
	}

	public float getL1Regularization() {
		return l1Regularization;
	}

	public float getL2Regularization() {
		return l2Regularization;
	}

	public ParameterFactory getFactory() {
		return factory;
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
			ParameterConfigurator that = (ParameterConfigurator) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.l1Regularization, that.l1Regularization);
			equal.append(this.l2Regularization, that.l2Regularization);
			equal.append(this.factory, that.factory);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(l1Regularization);
		hash.append(l2Regularization);
		hash.append(factory);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(l1Regularization=" + l1Regularization + ", l2Regularization=" + l2Regularization + ", factory=" + factory + ")";
	}

}
