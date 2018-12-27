package com.jstarcraft.ai.math.structure.matrix;

import java.util.WeakHashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathIterator;
import com.jstarcraft.ai.math.structure.MathMonitor;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.RandomVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.ai.model.ModelDefinition;

/**
 * 随机矩阵
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "vectors", "rowSize", "columnSize", "elementSize", "knownSize", "unknownSize" })
public abstract class RandomMatrix implements MathMatrix, MathMonitor<VectorScalar> {

	/** 向量 */
	protected RandomVector[] vectors;

	protected int rowSize, columnSize;

	protected int elementSize, knownSize, unknownSize;

	private transient WeakHashMap<MathMonitor<MatrixScalar>, Object> monitors = new WeakHashMap<>();

	RandomMatrix() {
	}

	@Override
	public int getElementSize() {
		return elementSize;
	}

	@Override
	public int getKnownSize() {
		return knownSize;
	}

	@Override
	public int getUnknownSize() {
		return unknownSize;
	}

	@Override
	public RandomMatrix setValues(float value) {
		for (RandomVector vector : vectors) {
			vector.setValues(value);
		}
		return this;
	}

	@Override
	public RandomMatrix scaleValues(float value) {
		for (RandomVector vector : vectors) {
			vector.scaleValues(value);
		}
		return this;
	}

	@Override
	public RandomMatrix shiftValues(float value) {
		for (RandomVector vector : vectors) {
			vector.shiftValues(value);
		}
		return this;
	}

	@Override
	public float getSum(boolean absolute) {
		float sum = 0F;
		for (RandomVector vector : vectors) {
			sum += vector.getSum(absolute);
		}
		return sum;
	}

	@Override
	public void attachMonitor(MathMonitor<MatrixScalar> monitor) {
		monitors.put(monitor, null);
	}

	@Override
	public void detachMonitor(MathMonitor<MatrixScalar> monitor) {
		monitors.remove(monitor);
	}

	@Override
	public int getRowSize() {
		return rowSize;
	}

	@Override
	public int getColumnSize() {
		return columnSize;
	}

	@Override
	public abstract RandomVector getRowVector(int rowIndex);

	@Override
	public abstract RandomVector getColumnVector(int columnIndex);

	@Override
	public boolean isIndexed() {
		return false;
	}

	@Override
	public float getValue(int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(int rowIndex, int columnIndex, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void scaleValue(int rowIndex, int columnIndex, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shiftValue(int rowIndex, int columnIndex, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void notifySizeChanged(MathIterator<VectorScalar> iterator, int oldElementSize, int newElementSize, int oldKnownSize, int newKnownSize, int oldUnknownSize, int newUnknownSize) {
		{
			int changedElementSize = newElementSize - oldElementSize;
			oldElementSize = elementSize;
			elementSize += changedElementSize;
			newElementSize = elementSize;
		}
		{
			int changedKnownSize = newKnownSize - oldKnownSize;
			oldKnownSize = knownSize;
			knownSize += changedKnownSize;
			newKnownSize = knownSize;
		}
		{
			int changedUnknownSize = newUnknownSize - oldUnknownSize;
			oldUnknownSize = unknownSize;
			unknownSize += changedUnknownSize;
			newUnknownSize = unknownSize;
		}

		for (MathMonitor<MatrixScalar> monitor : monitors.keySet()) {
			monitor.notifySizeChanged(this, oldElementSize, newElementSize, oldKnownSize, newKnownSize, oldUnknownSize, newUnknownSize);
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		RandomMatrix that = (RandomMatrix) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.vectors, that.vectors);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vectors);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (MathVector vector : vectors) {
			buffer.append(vector.toString());
		}
		return buffer.toString();
	}

	protected class RandomMatrixScalar implements MatrixScalar {

		private VectorScalar term;

		private int row, column;

		protected void update(VectorScalar term, int row, int column) {
			this.term = term;
			this.row = row;
			this.column = column;
		}

		@Override
		public int getRow() {
			return row;
		}

		@Override
		public int getColumn() {
			return column;
		}

		@Override
		public float getValue() {
			return term.getValue();
		}

		@Override
		public void scaleValue(float value) {
			term.scaleValue(value);
		}

		@Override
		public void setValue(float value) {
			term.setValue(value);
		}

		@Override
		public void shiftValue(float value) {
			term.shiftValue(value);
		}

	}

}
