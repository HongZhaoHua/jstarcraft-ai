package com.jstarcraft.ai.math.structure.vector;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathIterator;

/**
 * 稠密向量
 * 
 * @author Birdy
 *
 */
public class DenseVector implements MathVector {

	/** 游标 */
	private int cursor;
	/** 偏移量 */
	private int delta;
	/** 大小 */
	private int size;
	/** 数据 */
	private float[] values;

	public DenseVector(float[] data, int cursor, int delta, int size) {
		this.values = data;
		this.cursor = cursor;
		this.delta = delta;
		this.size = size;
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
	public MathIterator<VectorScalar> iterateElement(MathCalculator mode, MathAccessor<VectorScalar>... accessors) {
		switch (mode) {
		case SERIAL: {
			DenseVectorScalar scalar = new DenseVectorScalar();
			for (int index = 0; index < size; index++) {
				int position = cursor + index * delta;
				scalar.update(position, index);
				for (MathAccessor<VectorScalar> accessor : accessors) {
					accessor.accessScalar(scalar);
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
						accessor.accessScalar(scalar);
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
	public DenseVector setValues(float value) {
		for (int index = 0; index < size; index++) {
			int position = cursor + index * delta;
			values[position] = value;
		}
		return this;
	}

	@Override
	public DenseVector scaleValues(float value) {
		for (int index = 0; index < size; index++) {
			int position = cursor + index * delta;
			values[position] *= value;
		}
		return this;
	}

	@Override
	public DenseVector shiftValues(float value) {
		for (int index = 0; index < size; index++) {
			int position = cursor + index * delta;
			values[position] += value;
		}
		return this;
	}

	@Override
	public float getSum(boolean absolute) {
		float sum = 0F;
		if (absolute) {
			for (int index = 0; index < size; index++) {
				sum += FastMath.abs(values[cursor + index * delta]);
			}
		} else {
			for (int index = 0; index < size; index++) {
				sum += values[cursor + index * delta];
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
		return values[cursor + position * delta];
	}

	@Override
	public void setValue(int position, float value) {
		values[cursor + position * delta] = value;
	}

	@Override
	public void scaleValue(int position, float value) {
		values[cursor + position * delta] *= value;
	}

	@Override
	public void shiftValue(int position, float value) {
		values[cursor + position * delta] += value;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		DenseVector that = (DenseVector) object;
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
			buffer.append(getValue(index)).append(", ");
		}
		buffer.append("\n");
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
			return values[cursor];
		}

		@Override
		public void scaleValue(float value) {
			values[cursor] *= value;
		}

		@Override
		public void setValue(float value) {
			values[cursor] = value;
		}

		@Override
		public void shiftValue(float value) {
			values[cursor] += value;
		}

	}

	public static DenseVector copyOf(DenseVector vector) {
		float[] values = new float[vector.size];
		for (int index = 0, size = vector.size; index < size; index++) {
			values[index] = vector.getValue(index);
		}
		DenseVector instance = new DenseVector(values, 0, 1, vector.size);
		return instance;
	}

	public static DenseVector valueOf(int size) {
		DenseVector instance = new DenseVector(new float[size], 0, 1, size);
		return instance;
	}

	public static DenseVector valueOf(int size, float[] data) {
		DenseVector instance = new DenseVector(data, 0, 1, size);
		return instance;
	}

}
