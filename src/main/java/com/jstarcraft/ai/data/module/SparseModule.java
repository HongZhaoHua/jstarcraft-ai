package com.jstarcraft.ai.data.module;

import java.util.Iterator;
import java.util.List;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.exception.DataCapacityException;
import com.jstarcraft.ai.data.exception.DataCursorException;
import com.jstarcraft.ai.data.exception.DataException;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;
import com.jstarcraft.core.utility.KeyValue;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * 稀疏模块
 * 
 * @author Birdy
 *
 */
public class SparseModule extends AbstractModule {

	/** 离散特征 */
	private IntegerArray qualityPoints;

	private IntegerArray qualityIndexes;

	private IntegerArray qualityValues;

	/** 连续特征 */
	private IntegerArray continuousPoints;

	private IntegerArray continuousIndexes;

	private FloatArray continuousValues;

	public SparseModule(String name, List<KeyValue<KeyValue<String, Boolean>, Integer>> definition, int capacity) {
		super(name, definition, capacity);
		this.qualityPoints = new IntegerArray(1000, capacity + 1);
		this.qualityIndexes = new IntegerArray(1000, capacity * qualityOrder);
		this.qualityValues = new IntegerArray(1000, capacity * qualityOrder);
		this.continuousPoints = new IntegerArray(1000, capacity + 1);
		this.continuousIndexes = new IntegerArray(1000, capacity * continuousOrder);
		this.continuousValues = new FloatArray(1000, capacity * continuousOrder);
		this.qualityPoints.associateData(0);
		this.continuousPoints.associateData(0);
	}

	IntegerArray getQualityPoints() {
		return qualityPoints;
	}

	IntegerArray getQualityIndexes() {
		return qualityIndexes;
	}

	IntegerArray getQualityValues() {
		return qualityValues;
	}

	IntegerArray getContinuousPoints() {
		return continuousPoints;
	}

	IntegerArray getContinuousIndexes() {
		return continuousIndexes;
	}

	FloatArray getContinuousValues() {
		return continuousValues;
	}

	@Override
	public void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap continuousFeatures, int qualityMark, float continuousMark) {
		if (capacity == size) {
			throw new DataCapacityException();
		}
		if (!qualityFeatures.isEmpty() && (qualityFeatures.firstIntKey() < 0 || qualityFeatures.lastIntKey() >= qualityOrder)) {
			throw new DataException();
		}
		if (!continuousFeatures.isEmpty() && (continuousFeatures.firstIntKey() < 0 || continuousFeatures.lastIntKey() >= continuousOrder)) {
			throw new DataException();
		}
		qualityPoints.associateData(qualityPoints.getData(qualityPoints.getSize() - 1) + qualityFeatures.size());
		continuousPoints.associateData(continuousPoints.getData(continuousPoints.getSize() - 1) + continuousFeatures.size());
		for (Int2IntMap.Entry term : qualityFeatures.int2IntEntrySet()) {
			qualityIndexes.associateData(term.getIntKey());
			qualityValues.associateData(term.getIntValue());
		}
		for (Int2FloatMap.Entry term : continuousFeatures.int2FloatEntrySet()) {
			continuousIndexes.associateData(term.getIntKey());
			continuousValues.associateData(term.getFloatValue());
		}
		qualityMarks.associateData(qualityMark);
		continuousMarks.associateData(continuousMark);
		size++;
	}

	@Override
	public DataInstance getInstance(int cursor) {
		if (cursor < 0 && cursor >= size) {
			throw new DataCursorException();
		}
		return new SparseInstance(cursor, this);
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public Iterator<DataInstance> iterator() {
		return new SparseModuleIterator();
	}

	private class SparseModuleIterator implements Iterator<DataInstance> {

		private int cursor;

		private DataInstance term = new SparseInstance(cursor, SparseModule.this);

		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public DataInstance next() {
			term.setCursor(cursor++);
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
