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
	private IntegerArray quantityPoints;

	private IntegerArray quantityIndexes;

	private FloatArray quantityValues;

	public SparseModule(String name, List<KeyValue<KeyValue<String, Boolean>, Integer>> definition, int capacity) {
		super(name, definition, capacity);
		this.qualityPoints = new IntegerArray(1000, capacity + 1);
		this.qualityIndexes = new IntegerArray(1000, capacity * qualityOrder);
		this.qualityValues = new IntegerArray(1000, capacity * qualityOrder);
		this.quantityPoints = new IntegerArray(1000, capacity + 1);
		this.quantityIndexes = new IntegerArray(1000, capacity * quantityOrder);
		this.quantityValues = new FloatArray(1000, capacity * quantityOrder);
		this.qualityPoints.associateData(0);
		this.quantityPoints.associateData(0);
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

	IntegerArray getquantityPoints() {
		return quantityPoints;
	}

	IntegerArray getquantityIndexes() {
		return quantityIndexes;
	}

	FloatArray getquantityValues() {
		return quantityValues;
	}

	@Override
	public void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap quantityFeatures, int qualityMark, float quantityMark, float weight) {
		if (capacity == size) {
			throw new DataCapacityException();
		}
		if (!qualityFeatures.isEmpty() && (qualityFeatures.firstIntKey() < 0 || qualityFeatures.lastIntKey() >= qualityOrder)) {
			throw new DataException();
		}
		if (!quantityFeatures.isEmpty() && (quantityFeatures.firstIntKey() < 0 || quantityFeatures.lastIntKey() >= quantityOrder)) {
			throw new DataException();
		}
		qualityPoints.associateData(qualityPoints.getData(qualityPoints.getSize() - 1) + qualityFeatures.size());
		quantityPoints.associateData(quantityPoints.getData(quantityPoints.getSize() - 1) + quantityFeatures.size());
		for (Int2IntMap.Entry term : qualityFeatures.int2IntEntrySet()) {
			qualityIndexes.associateData(term.getIntKey());
			qualityValues.associateData(term.getIntValue());
		}
		for (Int2FloatMap.Entry term : quantityFeatures.int2FloatEntrySet()) {
			quantityIndexes.associateData(term.getIntKey());
			quantityValues.associateData(term.getFloatValue());
		}
		qualityMarks.associateData(qualityMark);
		quantityMarks.associateData(quantityMark);
		weights.associateData(weight);
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
