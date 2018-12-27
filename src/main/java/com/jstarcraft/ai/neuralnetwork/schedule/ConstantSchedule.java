package com.jstarcraft.ai.neuralnetwork.schedule;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ConstantSchedule implements Schedule {

	private float constant;

	ConstantSchedule() {
	}

	public ConstantSchedule(float constant) {
		this.constant = constant;
	}

	@Override
	public float valueAt(int iteration, int epoch) {
		return constant;
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
			ConstantSchedule that = (ConstantSchedule) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.constant, that.constant);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(constant);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "ConstantSchedule(constant=" + constant + ")";
	}

}
