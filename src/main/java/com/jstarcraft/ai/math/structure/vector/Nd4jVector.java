package com.jstarcraft.ai.math.structure.vector;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bytedeco.javacpp.FloatPointer;
import org.nd4j.linalg.api.concurrency.AffinityManager;
import org.nd4j.linalg.api.concurrency.AffinityManager.Location;
import org.nd4j.linalg.api.memory.MemoryWorkspace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentThread;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.Nd4jMatrix;
import com.jstarcraft.ai.model.ModelCycle;
import com.jstarcraft.ai.model.ModelDefinition;

/**
 * ND4J向量
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "size", "order", "data" })
public class Nd4jVector implements MathVector, ModelCycle {

	private static final AffinityManager manager = Nd4j.getAffinityManager();

	private static final Double one = 1D;

	private static final double zero = 0D;

	private int size;

	private char order;

	private float[] data;

	private transient INDArray vector;

	Nd4jVector() {
	}

	public Nd4jVector(INDArray vector) {
		if (vector.rank() != 1) {
			new IllegalArgumentException();
		}
		this.size = (int) vector.length();
		this.order = vector.ordering();
		this.vector = vector;
	}

	@Override
	public int getElementSize() {
		return size;
	}

	@Override
	public int getKnownSize() {
		return size;
	}

	@Override
	public int getUnknownSize() {
		return 0;
	}

	@Override
	public ScalarIterator<VectorScalar> iterateElement(MathCalculator mode, MathAccessor<VectorScalar>... accessors) {
		switch (mode) {
		case SERIAL: {
			Nd4jVectorScalar scalar = new Nd4jVectorScalar();
			for (int index = 0; index < size; index++) {
				scalar.update(index);
				for (MathAccessor<VectorScalar> accessor : accessors) {
					accessor.accessElement(scalar);
				}
			}
			return this;
		}
		default: {
			// TODO 参考Nd4jMatrix性能优化
			EnvironmentContext context = EnvironmentContext.getContext();
			Semaphore semaphore = MathCalculator.getSemaphore();
			for (int index = 0; index < size; index++) {
				int elementIndex = index;
				context.doStructureByAny(index, () -> {
					Nd4jVectorScalar scalar = new Nd4jVectorScalar();
					scalar.update(elementIndex);
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
	public ScalarIterator<VectorScalar> setValues(float value) {
		vector.assign(value);
		return this;
	}

	@Override
	public ScalarIterator<VectorScalar> scaleValues(float value) {
		vector.muli(value);
		return this;
	}

	@Override
	public ScalarIterator<VectorScalar> shiftValues(float value) {
		vector.addi(value);
		return this;
	}

	@Override
	public float getSum(boolean absolute) {
		if (absolute) {
			return vector.ameanNumber().floatValue() * vector.length();
		} else {
			return vector.sumNumber().floatValue();
		}
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
		return vector.getFloat(position);
	}

	@Override
	public void setValue(int position, float value) {
		vector.putScalar(position, value);
	}

	@Override
	public void scaleValue(int position, float value) {
		vector.putScalar(position, vector.getFloat(position) * value);
	}

	@Override
	public void shiftValue(int position, float value) {
		vector.putScalar(position, vector.getFloat(position) + value);
	}

	@Override
	public MathVector addVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			INDArray dataArray = this.getArray();
			// TODO 此处可能需要修改方向.
			INDArray vectorArray = Nd4jVector.class.cast(vector).getArray();
			dataArray.addi(vectorArray);
			return this;
		} else {
			return MathVector.super.addVector(vector);
		}
	}

	@Override
	public MathVector subtractVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			INDArray dataArray = this.getArray();
			// TODO 此处可能需要修改方向.
			INDArray vectorArray = Nd4jVector.class.cast(vector).getArray();
			dataArray.subi(vectorArray);
			return this;
		} else {
			return MathVector.super.addVector(vector);
		}
	}

	@Override
	public MathVector multiplyVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			INDArray dataArray = this.getArray();
			// TODO 此处可能需要修改方向.
			INDArray vectorArray = Nd4jVector.class.cast(vector).getArray();
			dataArray.muli(vectorArray);
			return this;
		} else {
			return MathVector.super.addVector(vector);
		}
	}

	@Override
	public MathVector divideVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			INDArray dataArray = this.getArray();
			// TODO 此处可能需要修改方向.
			INDArray vectorArray = Nd4jVector.class.cast(vector).getArray();
			dataArray.divi(vectorArray);
			return this;
		} else {
			return MathVector.super.addVector(vector);
		}
	}

	@Override
	public MathVector copyVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			INDArray dataArray = this.getArray();
			// TODO 此处可能需要修改方向.
			INDArray vectorArray = Nd4jVector.class.cast(vector).getArray();
			dataArray.assign(vectorArray);
			return this;
		} else {
			return MathVector.super.addVector(vector);
		}
	}

	@Override
	public MathVector dotProduct(MathMatrix leftMatrix, boolean transpose, MathVector rightVector, MathCalculator mode) {
		if (leftMatrix instanceof Nd4jMatrix && rightVector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = transpose ? Nd4jMatrix.class.cast(leftMatrix).getArray().transpose() : Nd4jMatrix.class.cast(leftMatrix).getArray();
				INDArray rightArray = Nd4jVector.class.cast(rightVector).getArray();
				INDArray dataArray = this.getArray();
				Nd4j.getBlasWrapper().gemv(one, leftArray, rightArray, zero, dataArray);
				return this;
			}
		} else {
			return MathVector.super.dotProduct(leftMatrix, transpose, rightVector, mode);
		}
	}

	@Override
	public MathVector dotProduct(MathVector leftVector, MathMatrix rightMatrix, boolean transpose, MathCalculator mode) {
		if (leftVector instanceof Nd4jVector && rightMatrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = Nd4jVector.class.cast(leftVector).getArray();
				if (leftArray.isView()) {
					// 此处执行复制是由于gemm不支持视图向量.
					leftArray = leftArray.dup();
				}
				if (leftArray.columns() == 1) {
					leftArray = leftArray.transpose();
				}
				INDArray rightArray = transpose ? Nd4jMatrix.class.cast(rightMatrix).getArray().transpose() : Nd4jMatrix.class.cast(rightMatrix).getArray();
				INDArray dataArray = this.getArray();
				leftArray.mmul(rightArray, dataArray);
				// Nd4j.getBlasWrapper().level3().gemm(leftArray, rightArray, dataArray, false,
				// false, one, zero);
				return this;
			}
		} else {
			return MathVector.super.dotProduct(leftVector, rightMatrix, transpose, mode);
		}
	}

	@Override
	public MathVector accumulateProduct(MathMatrix leftMatrix, boolean transpose, MathVector rightVector, MathCalculator mode) {
		if (leftMatrix instanceof Nd4jMatrix && rightVector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = transpose ? Nd4jMatrix.class.cast(leftMatrix).getArray().transpose() : Nd4jMatrix.class.cast(leftMatrix).getArray();
				INDArray rightArray = Nd4jVector.class.cast(rightVector).getArray();
				INDArray dataArray = this.getArray();
				INDArray cacheArray = Nd4j.zeros(dataArray.shape(), dataArray.ordering());
				Nd4j.getBlasWrapper().gemv(one, leftArray, rightArray, zero, cacheArray);
				dataArray.addi(cacheArray);
				return this;
			}
		} else {
			return MathVector.super.accumulateProduct(leftMatrix, transpose, rightVector, mode);
		}
	}

	@Override
	public MathVector accumulateProduct(MathVector leftVector, MathMatrix rightMatrix, boolean transpose, MathCalculator mode) {
		if (leftVector instanceof Nd4jVector && rightMatrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = Nd4jVector.class.cast(leftVector).getArray();
				if (leftArray.isView()) {
					// 此处执行复制是由于gemm不支持视图向量.
					leftArray = leftArray.dup();
				}
				if (leftArray.columns() == 1) {
					leftArray = leftArray.transpose();
				}
				INDArray rightArray = transpose ? Nd4jMatrix.class.cast(rightMatrix).getArray().transpose() : Nd4jMatrix.class.cast(rightMatrix).getArray();
				INDArray dataArray = this.getArray();
				INDArray cacheArray = Nd4j.zeros(dataArray.shape(), dataArray.ordering());
				leftArray.mmul(rightArray, cacheArray);
				dataArray.addi(cacheArray);
				// Nd4j.getBlasWrapper().level3().gemm(leftArray, rightArray, dataArray, false,
				// false, one, zero);
				return this;
			}
		} else {
			return MathVector.super.accumulateProduct(leftVector, rightMatrix, transpose, mode);
		}
	}

	public INDArray getArray() {
		return vector;
	}

	// @Deprecated
	// // TODO 考虑使用Worksapce代替
	// public INDArray getRowCache() {
	// if (rowCache == null) {
	// rowCache = vector;
	// if (rowCache.isView()) {
	// // 此处执行复制是由于gemm不支持视图向量.
	// rowCache = rowCache.dup();
	// }
	// if (rowCache.columns() == 1) {
	// rowCache = rowCache.transpose();
	// }
	// }
	// return rowCache;
	// }
	//
	// @Deprecated
	// // TODO 考虑使用Worksapce代替
	// public INDArray getColumnCache() {
	// if (columnCache == null) {
	// columnCache = vector;
	// if (columnCache.isView()) {
	// // 此处执行复制是由于gemm不支持视图向量.
	// columnCache = columnCache.dup();
	// }
	// if (columnCache.rows() == 1) {
	// columnCache = columnCache.transpose();
	// }
	// }
	// return columnCache;
	// }

	@Override
	public void beforeSave() {
		data = new float[size];
		FloatPointer pointer = (FloatPointer) vector.data().pointer();
		pointer.get(data, 0, data.length);
	}

	@Override
	public void afterLoad() {
		vector = Nd4j.zeros(size, order);
		manager.ensureLocation(vector, Location.HOST);
		manager.tagLocation(vector, Location.HOST);
		FloatPointer pointer = (FloatPointer) vector.data().pointer();
		pointer.put(data, 0, data.length);
		data = null;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Nd4jVector that = (Nd4jVector) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.vector, that.vector);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vector);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return vector.toString();
	}

	@Override
	public Iterator<VectorScalar> iterator() {
		return new Nd4jVectorIterator();
	}

	private class Nd4jVectorIterator implements Iterator<VectorScalar> {

		private int index = 0;

		private final Nd4jVectorScalar term = new Nd4jVectorScalar();

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

	private class Nd4jVectorScalar implements VectorScalar {

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
			return vector.getFloat(index);
		}

		@Override
		public void scaleValue(float value) {
			vector.putScalar(index, vector.getFloat(index) * value);
		}

		@Override
		public void setValue(float value) {
			vector.putScalar(index, value);
		}

		@Override
		public void shiftValue(float value) {
			vector.putScalar(index, vector.getFloat(index) + value);
		}

	}

}
