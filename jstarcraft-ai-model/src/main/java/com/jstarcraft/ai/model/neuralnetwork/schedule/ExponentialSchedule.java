package com.jstarcraft.ai.model.neuralnetwork.schedule;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ExponentialSchedule implements Schedule {

    private ScheduleType scheduleType;
    private float initialValue;
    private float gamma;

    ExponentialSchedule() {
    }

    public ExponentialSchedule(ScheduleType scheduleType, float initialValue, float gamma) {
        this.scheduleType = scheduleType;
        this.initialValue = initialValue;
        this.gamma = gamma;
    }

    @Override
    public float valueAt(int iteration, int epoch) {
        int i = (scheduleType == ScheduleType.ITERATION ? iteration : epoch);
        return (float) (initialValue * Math.pow(gamma, i));
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
            ExponentialSchedule that = (ExponentialSchedule) object;
            EqualsBuilder equal = new EqualsBuilder();
            equal.append(this.scheduleType, that.scheduleType);
            equal.append(this.initialValue, that.initialValue);
            equal.append(this.gamma, that.gamma);
            return equal.isEquals();
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(scheduleType);
        hash.append(initialValue);
        hash.append(gamma);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        return "ExponentialSchedule(scheduleType=" + scheduleType + ", initialValue=" + initialValue + ", gamma=" + gamma + ")";
    }

}
