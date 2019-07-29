package com.jstarcraft.ai.model.neuralnetwork.schedule;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MapSchedule implements Schedule {

	private ScheduleType scheduleType;
	private Map<Integer, Float> values;
	private transient int[] allKeysSorted;

	MapSchedule() {
	}

	public MapSchedule(ScheduleType scheduleType, Map<Integer, Float> values) {
		if (!values.containsKey(0)) {
			throw new IllegalArgumentException("Invalid set of values: must contain initial value (position 0)");
		}
		this.scheduleType = scheduleType;
		this.values = values;

		this.allKeysSorted = new int[values.size()];
		int pos = 0;
		for (Integer i : values.keySet()) {
			allKeysSorted[pos++] = i;
		}
		Arrays.sort(allKeysSorted);
	}

	@Override
	public float valueAt(int iteration, int epoch) {
		int i = (scheduleType == ScheduleType.ITERATION ? iteration : epoch);

		if (values.containsKey(i)) {
			return values.get(i);
		} else {
			// Key doesn't exist - find nearest key...
			if (i >= allKeysSorted[allKeysSorted.length - 1]) {
				return values.get(allKeysSorted[allKeysSorted.length - 1]);
			} else {
				/*
				 * Returned: index of the search key, if it is contained in the array;
				 * otherwise, (-(insertion point) - 1). The insertion point is defined as the
				 * point at which the key would be inserted into the array: the index of the
				 * first element greater than the key
				 */
				int pt = Arrays.binarySearch(allKeysSorted, i);
				int iPt = -(pt + 1);
				float d = values.get(allKeysSorted[iPt - 1]);
				return d;
			}
		}
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
			MapSchedule that = (MapSchedule) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.scheduleType, that.scheduleType);
			equal.append(this.values, that.values);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(scheduleType);
		hash.append(values);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "MapSchedule(scheduleType=" + scheduleType + ", values=" + values + ")";
	}

}
