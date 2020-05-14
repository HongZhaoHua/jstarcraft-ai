package com.jstarcraft.ai.math.structure.vector;

import java.util.BitSet;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;

/**
 * 位向量
 * 
 * @author Birdy
 *
 */
public class BitVector implements MathVector {

	public static final float TRUE = 1F;

	public static final float FALSE = 0F;

	/** 游标 */
	protected int cursor;
	/** 偏移量 */
	protected int delta;
	/** 大小 */
	protected int size;
	/** 数据 */
	protected BitSet values;

	public BitVector(BitSet data, int cursor, int delta, int size) {
		this.values = data;
		this.cursor = cursor;
		this.delta = delta;
		this.size = size;
	}

	@Override
	public int getDimensionSize() {
		return size;
	}

	@Override
	public int getElementSize() {
		return size;
	}

	@Override
	public int getKnownSize() {
		return getElementSize();
	}

	@Override
	public int getUnknownSize() {
		return 0;
	}

	@Override
	public ScalarIterator<VectorScalar> iterateElement(MathCalculator mode, MathAccessor<VectorScalar>... accessors) {
		switch (mode) {
		case SERIAL: {
			DenseVectorScalar scalar = new DenseVectorScalar();
			for (int index = 0; index < size; index++) {
				int position = cursor + index * delta;
				scalar.update(position, index);
				for (MathAccessor<VectorScalar> accessor : accessors) {
					accessor.accessElement(scalar);
				}
			}
			return this;
		}
		default: {
			EnvironmentContext context = EnvironmentContext.getContext();
			Semaphore semaphore = MathCalculator.getSemaphore();
			for (int index = 0; index < size; index++) {
				int elementIndex = index;
				int position = cursor + index * delta;
				context.doStructureByAny(position, () -> {
					DenseVectorScalar scalar = new DenseVectorScalar();
					scalar.update(position, elementIndex);
					for (MathAccessor<VectorScalar> accessor : accessors) {
						accessor.accessElement(scalar);
					}
					semaphore.release();
				});
			}
			try {
				semaphore.acquire(size);
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
			return this;
		}
		}
	}

	@Override
	public BitVector setValues(float value) {
		for (int index = 0; index < size; index++) {
			int position = cursor + index * delta;
			values.set(position, value > FALSE);
		}
		return this;
	}

	@Override
	public BitVector scaleValues(float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BitVector shiftValues(float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getSum(boolean absolute) {
		float sum = 0F;
		for (int index = 0; index < size; index++) {
			int position = cursor + index * delta;
			if (values.get(position)) {
				sum++;
			}
		}
		return sum;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public int getIndex(int position) {
		return position;
	}

	@Override
	public float getValue(int position) {
		return values.get(position) ? TRUE : FALSE;
	}

	@Override
	public void setValue(int position, float value) {
		values.set(position, value > FALSE);
	}

	@Override
	public void scaleValue(int position, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shiftValue(int position, float value) {
		throw new UnsupportedOperationException();
	}

	public boolean getBit(int position) {
		return values.get(position);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		BitVector that = (BitVector) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.values, that.values);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(values);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (int index = 0; index < size; index++) {
			buffer.append(getValue(index));
		}
		return buffer.toString();
	}

	@Override
	public Iterator<VectorScalar> iterator() {
		return new DenseVectorIterator();
	}

	/**
	 * Iterator over a sparse vector
	 */
	private class DenseVectorIterator implements Iterator<VectorScalar> {

		private int index;

		private final DenseVectorScalar term = new DenseVectorScalar();

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public VectorScalar next() {
			term.update(cursor + index * delta, index++);
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class DenseVectorScalar implements VectorScalar {

		private int index;

		private int cursor;

		private void update(int cursor, int index) {
			this.cursor = cursor;
			this.index = index;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public float getValue() {
			return values.get(cursor) ? TRUE : FALSE;
		}

		@Override
		public void scaleValue(float value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue(float value) {
			values.set(cursor, value > FALSE);
		}

		@Override
		public void shiftValue(float value) {
			throw new UnsupportedOperationException();
		}

	}

	public static BitVector copyOf(BitVector vector) {
		BitSet values = new BitSet(vector.size);
		for (int index = 0, size = vector.size; index < size; index++) {
			values.set(index, vector.getValue(index) > FALSE);
		}
		BitVector instance = new BitVector(values, 0, 1, vector.size);
		return instance;
	}

	public static BitVector valueOf(int size) {
		BitVector instance = new BitVector(new BitSet(size), 0, 1, size);
		return instance;
	}

	public static BitVector valueOf(int size, BitSet data) {
		BitVector instance = new BitVector(data, 0, 1, size);
		return instance;
	}

}
