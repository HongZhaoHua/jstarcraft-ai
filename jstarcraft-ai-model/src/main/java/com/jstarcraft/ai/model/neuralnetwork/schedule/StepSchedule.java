package com.jstarcraft.ai.model.neuralnetwork.schedule;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class StepSchedule implements Schedule {

    private ScheduleType scheduleType;
    private float initialValue;
    private float decayRate;
    private float step;

    StepSchedule() {
    }

    public StepSchedule(ScheduleType scheduleType, float initialValue, float decayRate, float step) {
        this.scheduleType = scheduleType;
        this.initialValue = initialValue;
        this.decayRate = decayRate;
        this.step = step;
    }

    @Override
    public float valueAt(int iteration, int epoch) {
        int i = (scheduleType == ScheduleType.ITERATION ? iteration : epoch);
        return (float) (initialValue * Math.pow(decayRate, Math.floor(i / step)));
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
            StepSchedule that = (StepSchedule) object;
            EqualsBuilder equal = new EqualsBuilder();
            equal.append(this.scheduleType, that.scheduleType);
            equal.append(this.initialValue, that.initialValue);
            equal.append(this.decayRate, that.decayRate);
            equal.append(this.step, that.step);
            return equal.isEquals();
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(scheduleType);
        hash.append(initialValue);
        hash.append(decayRate);
        hash.append(step);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        return "StepSchedule(scheduleType=" + scheduleType + ", initialValue=" + initialValue + ", decayRate=" + decayRate + ", step=" + step + ")";
    }

}
