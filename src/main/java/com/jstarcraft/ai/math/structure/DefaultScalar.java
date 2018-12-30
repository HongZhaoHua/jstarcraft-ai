package com.jstarcraft.ai.math.structure;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 默认标量
 * 
 * @author Birdy
 *
 */
public class DefaultScalar implements MathScalar {

	private final static ThreadLocal<DefaultScalar> instances = new ThreadLocal<>();

	private DefaultScalar() {
	};

	private float value;

	@Override
	public float getValue() {
		return value;
	}

	@Override
	public void scaleValue(float value) {
		this.value *= value;
	}

	@Override
	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public void shiftValue(float value) {
		this.value += value;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		DefaultScalar that = (DefaultScalar) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.value, that.value);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(value);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		string.append(value);
		return string.toString();
	}

	public static DefaultScalar getInstance() {
		DefaultScalar instance = instances.get();
		if (instance == null) {
			instance = new DefaultScalar();
			instances.set(instance);
		}
		return instance;
	}

}
