package com.jstarcraft.ai.neuralnetwork.schedule;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class InverseSchedule implements Schedule {

	private ScheduleType scheduleType;
	private float initialValue;
	private float gamma;
	private float power;

	public InverseSchedule(ScheduleType scheduleType, float initialValue, float gamma, float power) {
		this.scheduleType = scheduleType;
		this.initialValue = initialValue;
		this.gamma = gamma;
		this.power = power;
	}

	@Override
	public float valueAt(int iteration, int epoch) {
		int i = (scheduleType == ScheduleType.ITERATION ? iteration : epoch);
		return (float) (initialValue / Math.pow(1 + gamma * i, power));
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
			InverseSchedule that = (InverseSchedule) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.scheduleType, that.scheduleType);
			equal.append(this.initialValue, that.initialValue);
			equal.append(this.gamma, that.gamma);
			equal.append(this.power, that.power);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(scheduleType);
		hash.append(initialValue);
		hash.append(gamma);
		hash.append(power);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "InverseSchedule(scheduleType=" + scheduleType + ", initialValue=" + initialValue + ", gamma=" + gamma + ", power=" + power + ")";
	}

}
