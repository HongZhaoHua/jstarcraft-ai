package com.jstarcraft.ai.math.structure.vector;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathIterator;

/**
 * 对称向量
 * 
 * @author Birdy
 *
 */
public class SymmetryVector implements MathVector {

	private int index;

	private int dimension;

	private float[] data;

	private int[] point;

	@Override
	public int getElementSize() {
		return dimension;
	}

	@Override
	public int getKnownSize() {
		return dimension;
	}

	@Override
	public int getUnknownSize() {
		return 0;
	}

	@Override
	public MathIterator<VectorScalar> iterateElement(MathCalculator mode, MathAccessor<VectorScalar>... accessors) {
		switch (mode) {
		case SERIAL: {
			SymmetryVectorScalar scalar = new SymmetryVectorScalar();
			for (int position = 0; position < dimension; position++) {
				int x, y;
				if (index >= position) {
					x = index;
					y = position;
				} else {
					x = position;
					y = index;
				}
				int cursor = position;
				scalar.update(cursor, x, y);
				for (MathAccessor<VectorScalar> accessor : accessors) {
					accessor.accessScalar(scalar);
				}
			}
			return this;
		}
		default: {
			int size = dimension;
			EnvironmentContext context = EnvironmentContext.getContext();
			Semaphore semaphore = MathCalculator.getSemaphore();
			for (int position = 0; position < dimension; position++) {
				int x, y;
				if (index >= position) {
					x = index;
					y = position;
				} else {
					x = position;
					y = index;
				}
				int cursor = position;
				context.doStructureByAny(position, () -> {
					SymmetryVectorScalar scalar = new SymmetryVectorScalar();
					scalar.update(cursor, x, y);
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
	public SymmetryVector setValues(float value) {
		for (int position = 0; position < dimension; position++) {
			int x, y;
			if (index >= position) {
				x = index;
				y = position;
			} else {
				x = position;
				y = index;
			}
			int index = point[x] + y;
			data[index] = value;
		}
		return this;
	}

	@Override
	public SymmetryVector scaleValues(float value) {
		for (int position = 0; position < dimension; position++) {
			int x, y;
			if (index >= position) {
				x = index;
				y = position;
			} else {
				x = position;
				y = index;
			}
			int index = point[x] + y;
			data[index] *= value;
		}
		return this;
	}

	@Override
	public SymmetryVector shiftValues(float value) {
		for (int position = 0; position < dimension; position++) {
			int x, y;
			if (index >= position) {
				x = index;
				y = position;
			} else {
				x = position;
				y = index;
			}
			int index = point[x] + y;
			data[index] += value;
		}
		return this;
	}

	@Override
	public float getSum(boolean absolute) {
		float sum = 0F;
		if (absolute) {
			for (int position = 0; position < dimension; position++) {
				int x, y;
				if (index >= position) {
					x = index;
					y = position;
				} else {
					x = position;
					y = index;
				}
				int index = point[x] + y;
				sum += FastMath.abs(data[index]);
			}
		} else {
			for (int position = 0; position < dimension; position++) {
				int x, y;
				if (index >= position) {
					x = index;
					y = position;
				} else {
					x = position;
					y = index;
				}
				int index = point[x] + y;
				sum += data[index];
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

	private int getCursor(int position) {
		int x, y;
		if (index >= position) {
			x = index;
			y = position;
		} else {
			x = position;
			y = index;
		}
		return point[x] + y;
	}

	@Override
	public float getValue(int position) {
		int cursor = getCursor(position);
		return data[cursor];
	}

	@Override
	public void setValue(int position, float value) {
		int cursor = getCursor(position);
		data[cursor] = value;
	}

	@Override
	public void scaleValue(int position, float value) {
		int cursor = getCursor(position);
		data[cursor] *= value;
	}

	@Override
	public void shiftValue(int position, float value) {
		int cursor = getCursor(position);
		data[cursor] += value;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		SymmetryVector that = (SymmetryVector) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.dimension, that.dimension);
		equal.append(this.index, that.index);
		equal.append(this.data, that.data);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(dimension);
		hash.append(index);
		hash.append(data);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		string.append(dimension);
		string.append(index);
		string.append(data);
		return string.toString();
	}

	@Override
	public Iterator<VectorScalar> iterator() {
		return new SymmetryVectorIterator();
	}

	private class SymmetryVectorIterator implements Iterator<VectorScalar> {

		private int cursor = 0;

		private final SymmetryVectorScalar term = new SymmetryVectorScalar();

		@Override
		public boolean hasNext() {
			return cursor < dimension;
		}

		@Override
		public VectorScalar next() {
			int x, y;
			if (index >= cursor) {
				x = index;
				y = cursor;
			} else {
				x = cursor;
				y = index;
			}
			term.update(cursor++, x, y);
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class SymmetryVectorScalar implements VectorScalar {

		private int cursor, x, y;

		private void update(int cursor, int x, int y) {
			this.cursor = cursor;
			this.x = x;
			this.y = y;
		}

		@Override
		public int getIndex() {
			return cursor;
		}

		@Override
		public float getValue() {
			return data[point[x] + y];
		}

		@Override
		public void scaleValue(float value) {
			data[point[x] + y] *= value;
		}

		@Override
		public void setValue(float value) {
			data[point[x] + y] = value;
		}

		@Override
		public void shiftValue(float value) {
			data[point[x] + y] += value;
		}

	}

	SymmetryVector() {
	}

	public SymmetryVector(int capacity, int index, int[] point, float[] data) {
		assert capacity > index;
		this.dimension = capacity;
		this.index = index;
		this.point = point;
		this.data = data;
	}

}
