package com.jstarcraft.ai.math.structure.vector;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;

/**
 * 区域向量
 * 
 * @author Birdy
 *
 */
public class SectionVector implements MathVector {

	private int from;

	private int to;

	private MathVector dataVector;

	private int elementSize, knownSize, unknownSize;

	public SectionVector(MathVector dataVector, int from, int to) {
		this.dataVector = dataVector;
		this.from = from;
		this.to = to;

		this.elementSize = to - from;
		this.knownSize = elementSize;
		this.unknownSize = 0;
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
	public ScalarIterator<VectorScalar> iterateElement(MathCalculator mode, MathAccessor<VectorScalar>... accessors) {
		switch (mode) {
		case SERIAL: {
			SectionVectorScalar scalar = new SectionVectorScalar();
			for (int index = 0; index < elementSize; index++) {
				scalar.update(index);
				for (MathAccessor<VectorScalar> accessor : accessors) {
					accessor.accessElement(scalar);
				}
			}
			return this;
		}
		default: {
			EnvironmentContext context = EnvironmentContext.getContext();
			Semaphore semaphore = MathCalculator.getSemaphore();
			for (int index = 0; index < elementSize; index++) {
				int elementIndex = index;
				context.doStructureByAny(index, () -> {
					SectionVectorScalar scalar = new SectionVectorScalar();
					scalar.update(elementIndex);
					for (MathAccessor<VectorScalar> accessor : accessors) {
						accessor.accessElement(scalar);
					}
					semaphore.release();
				});
			}
			try {
				semaphore.acquire(elementSize);
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
			return this;
		}
		}
	}

	@Override
	public SectionVector setValues(float value) {
		for (int index = from; index < to; index++) {
			dataVector.setValue(index, value);
		}
		return this;
	}

	@Override
	public SectionVector scaleValues(float value) {
		for (int index = from; index < to; index++) {
			dataVector.scaleValue(index, value);
		}
		return this;
	}

	@Override
	public SectionVector shiftValues(float value) {
		for (int index = from; index < to; index++) {
			dataVector.shiftValue(index, value);
		}
		return this;
	}

	@Override
	public float getSum(boolean absolute) {
		float sum = 0F;
		if (absolute) {
			for (int index = from; index < to; index++) {
				sum += FastMath.abs(dataVector.getIndex(index));
			}
		} else {
			for (int index = from; index < to; index++) {
				sum += dataVector.getIndex(index);
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
		return dataVector.getValue(position + from);
	}

	@Override
	public void setValue(int position, float value) {
		dataVector.setValue(position + from, value);
	}

	@Override
	public void scaleValue(int position, float value) {
		dataVector.scaleValue(position + from, value);
	}

	@Override
	public void shiftValue(int position, float value) {
		dataVector.shiftValue(position + from, value);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		SectionVector that = (SectionVector) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.dataVector, that.dataVector);
		equal.append(this.from, that.from);
		equal.append(this.to, that.to);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(dataVector);
		hash.append(from);
		hash.append(to);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(dataVector.toString());
		return buffer.toString();
	}

	@Override
	public Iterator<VectorScalar> iterator() {
		return new SectionVectorIterator();
	}

	/**
	 * Iterator over a sparse vector
	 */
	private class SectionVectorIterator implements Iterator<VectorScalar> {

		private int index;

		private int size = elementSize;

		private SectionVectorScalar term = new SectionVectorScalar();

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public VectorScalar next() {
			term.update(index++);
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class SectionVectorScalar implements VectorScalar {

		private int index;

		private void update(int index) {
			this.index = index;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public float getValue() {
			return dataVector.getValue(index + from);
		}

		@Override
		public void scaleValue(float value) {
			dataVector.scaleValue(index + from, value);
		}

		@Override
		public void setValue(float value) {
			dataVector.setValue(index + from, value);
		}

		@Override
		public void shiftValue(float value) {
			dataVector.shiftValue(index + from, value);
		}

	}

}
