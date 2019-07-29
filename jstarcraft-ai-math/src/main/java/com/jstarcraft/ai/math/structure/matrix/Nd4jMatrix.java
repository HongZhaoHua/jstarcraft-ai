package com.jstarcraft.ai.math.structure.matrix;

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
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.Nd4jVector;
import com.jstarcraft.ai.modem.ModemCycle;
import com.jstarcraft.ai.modem.ModemDefinition;

/**
 * ND4J矩阵
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "rowSize", "columnSize", "order", "data" })
public class Nd4jMatrix implements MathMatrix, ModemCycle {

	private static final AffinityManager manager = Nd4j.getAffinityManager();

	private int rowSize;

	private int columnSize;

	private int size;

	private char order;

	private float[] data;

	private transient INDArray matrix;

	Nd4jMatrix() {
	}

	public Nd4jMatrix(INDArray matrix) {
		if (matrix.rank() != 2) {
			new IllegalArgumentException();
		}
		this.rowSize = matrix.rows();
		this.columnSize = matrix.columns();
		this.size = rowSize * columnSize;
		this.order = matrix.ordering();
		this.matrix = matrix;
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

	private class Nd4jArrayScalar implements MatrixScalar {

		private int cursor, section = matrix.columns();

		private float[] data;

		private Nd4jArrayScalar(float[] data) {
			this.data = data;
		}

		private void update(int cursor) {
			this.cursor = cursor;
		}

		@Override
		public int getRow() {
			return cursor / section;
		}

		@Override
		public int getColumn() {
			return cursor % section;
		}

		@Override
		public float getValue() {
			return data[cursor];
		}

		@Override
		public void scaleValue(float value) {
			data[cursor] *= value;
		}

		@Override
		public void setValue(float value) {
			data[cursor] = value;
		}

		@Override
		public void shiftValue(float value) {
			data[cursor] += value;
		}

	}

	@Override
	public ScalarIterator<MatrixScalar> iterateElement(MathCalculator mode, MathAccessor<MatrixScalar>... accessors) {
		// 保证内存与显存同步
		manager.ensureLocation(matrix, Location.HOST);
		manager.tagLocation(matrix, Location.HOST);
		EnvironmentThread thread = EnvironmentThread.currentThread();
		data = thread.getArray();
		FloatPointer pointer = (FloatPointer) matrix.data().pointer();
		pointer.get(data, 0, rowSize * columnSize);
		int rowSize = matrix.rows();
		int columnSize = matrix.columns();
		switch (mode) {
		case SERIAL: {
			Nd4jArrayScalar scalar = new Nd4jArrayScalar(data);
			for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					int cursor = order == 'c' ? rowIndex * columnSize + columnIndex : columnIndex * rowSize + rowIndex;
					scalar.update(cursor);
					for (MathAccessor<MatrixScalar> accessor : accessors) {
						accessor.accessElement(scalar);
					}
				}
			}
			pointer.put(data, 0, rowSize * columnSize);
			return this;
		}
		default: {
			if (this.getColumnSize() <= this.getRowSize()) {
				int size = columnSize;
				EnvironmentContext context = thread.getContext();
				Semaphore semaphore = MathCalculator.getSemaphore();
				for (int index = 0; index < size; index++) {
					int columnIndex = index;
					context.doStructureByAny(index, () -> {
						Nd4jArrayScalar scalar = new Nd4jArrayScalar(data);
						for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
							int cursor = order == 'c' ? rowIndex * columnSize + columnIndex : columnIndex * rowSize + rowIndex;
							scalar.update(cursor);
							for (MathAccessor<MatrixScalar> accessor : accessors) {
								accessor.accessElement(scalar);
							}
						}
						semaphore.release();
					});
				}
				try {
					semaphore.acquire(size);
				} catch (Exception exception) {
					throw new RuntimeException(exception);
				}
				pointer.put(data, 0, rowSize * columnSize);
				return this;
			} else {
				int size = rowSize;
				EnvironmentContext context = thread.getContext();
				Semaphore semaphore = MathCalculator.getSemaphore();
				for (int index = 0; index < size; index++) {
					int rowIndex = index;
					context.doStructureByAny(index, () -> {
						Nd4jArrayScalar scalar = new Nd4jArrayScalar(data);
						for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
							int cursor = order == 'c' ? rowIndex * columnSize + columnIndex : columnIndex * rowSize + rowIndex;
							scalar.update(cursor);
							for (MathAccessor<MatrixScalar> accessor : accessors) {
								accessor.accessElement(scalar);
							}
						}
						semaphore.release();
					});
				}
				try {
					semaphore.acquire(size);
				} catch (Exception exception) {
					throw new RuntimeException(exception);
				}
				pointer.put(data, 0, rowSize * columnSize);
				return this;
			}
		}
		}
	}

	@Override
	public Nd4jMatrix setValues(float value) {
		matrix.assign(value);
		return this;
	}

	@Override
	public Nd4jMatrix scaleValues(float value) {
		matrix.muli(value);
		return this;
	}

	@Override
	public Nd4jMatrix shiftValues(float value) {
		matrix.addi(value);
		return this;
	}

	@Override
	public float getSum(boolean absolute) {
		if (absolute) {
			return matrix.ameanNumber().floatValue() * matrix.length();
		} else {
			return matrix.sumNumber().floatValue();
		}
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
	public MathVector getRowVector(int rowIndex) {
		return new Nd4jVector(matrix.getRow(rowIndex));
	}

	@Override
	public MathVector getColumnVector(int columnIndex) {
		return new Nd4jVector(matrix.getColumn(columnIndex));
	}

	@Override
	public boolean isIndexed() {
		return true;
	}

	@Override
	public float getValue(int rowIndex, int columnIndex) {
		return matrix.getFloat(rowIndex, columnIndex);
	}

	@Override
	public void setValue(int rowIndex, int columnIndex, float value) {
		matrix.putScalar(rowIndex, columnIndex, value);
	}

	@Override
	public void scaleValue(int rowIndex, int columnIndex, float value) {
		matrix.putScalar(rowIndex, columnIndex, matrix.getFloat(rowIndex, columnIndex) * value);
	}

	@Override
	public void shiftValue(int rowIndex, int columnIndex, float value) {
		matrix.putScalar(rowIndex, columnIndex, matrix.getFloat(rowIndex, columnIndex) + value);
	}

	@Override
	public MathMatrix addMatrix(MathMatrix matrix, boolean transpose) {
		if (matrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jMatrix.class.cast(matrix).getArray();
				thisArray.addi(transpose ? thatArray.transposei() : thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.addMatrix(matrix, transpose);
		}
	}

	@Override
	public MathMatrix subtractMatrix(MathMatrix matrix, boolean transpose) {
		if (matrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jMatrix.class.cast(matrix).getArray();
				thisArray.subi(transpose ? thatArray.transposei() : thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.subtractMatrix(matrix, transpose);
		}
	}

	@Override
	public MathMatrix multiplyMatrix(MathMatrix matrix, boolean transpose) {
		if (matrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jMatrix.class.cast(matrix).getArray();
				thisArray.muli(transpose ? thatArray.transposei() : thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.multiplyMatrix(matrix, transpose);
		}
	}

	@Override
	public MathMatrix divideMatrix(MathMatrix matrix, boolean transpose) {
		if (matrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jMatrix.class.cast(matrix).getArray();
				thisArray.divi(transpose ? thatArray.transposei() : thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.divideMatrix(matrix, transpose);
		}
	}

	@Override
	public MathMatrix copyMatrix(MathMatrix matrix, boolean transpose) {
		if (matrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jMatrix.class.cast(matrix).getArray();
				thisArray.assign(transpose ? thatArray.transposei() : thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.copyMatrix(matrix, transpose);
		}
	}

	@Override
	public MathMatrix addRowVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.addiRowVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.addRowVector(vector);
		}
	}

	@Override
	public MathMatrix subtractRowVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.subiRowVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.subtractRowVector(vector);
		}
	}

	@Override
	public MathMatrix multiplyRowVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.muliRowVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.multiplyRowVector(vector);
		}
	}

	@Override
	public MathMatrix divideRowVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.diviRowVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.divideRowVector(vector);
		}
	}

	@Override
	public MathMatrix copyRowVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.putiRowVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.copyRowVector(vector);
		}
	}

	@Override
	public MathMatrix addColumnVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.addiColumnVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.addColumnVector(vector);
		}
	}

	@Override
	public MathMatrix subtractColumnVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.subiColumnVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.subtractColumnVector(vector);
		}
	}

	@Override
	public MathMatrix multiplyColumnVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.muliColumnVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.multiplyColumnVector(vector);
		}
	}

	@Override
	public MathMatrix divideColumnVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.diviColumnVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.divideColumnVector(vector);
		}
	}

	@Override
	public MathMatrix copyColumnVector(MathVector vector) {
		if (vector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray thisArray = this.getArray();
				INDArray thatArray = Nd4jVector.class.cast(vector).getArray();
				thisArray.putiColumnVector(thatArray);
				return this;
			}
		} else {
			return MathMatrix.super.copyColumnVector(vector);
		}
	}

	@Override
	public MathMatrix dotProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
		if (leftMatrix instanceof Nd4jMatrix && rightMatrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = leftTranspose ? Nd4jMatrix.class.cast(leftMatrix).getArray().transpose() : Nd4jMatrix.class.cast(leftMatrix).getArray();
				INDArray rightArray = rightTranspose ? Nd4jMatrix.class.cast(rightMatrix).getArray().transpose() : Nd4jMatrix.class.cast(rightMatrix).getArray();
				INDArray dataArray = this.getArray();
				leftArray.mmul(rightArray, dataArray);
				return this;
			}
		} else {
			return MathMatrix.super.dotProduct(leftMatrix, leftTranspose, rightMatrix, rightTranspose, mode);
		}
	}

	@Override
	public MathMatrix dotProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
		if (rowVector instanceof Nd4jVector && columnVector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = Nd4jVector.class.cast(rowVector).getArray();
				// TODO 此处需要想方案优化,可能存在性能问题.
				if (leftArray.isView()) {
					// 此处执行复制是由于gemm不支持视图向量.
					leftArray = leftArray.dup();
				}
				if (leftArray.rows() == 1) {
					leftArray = leftArray.transpose();
				}
				INDArray rightArray = Nd4jVector.class.cast(columnVector).getArray();
				if (rightArray.isView()) {
					// 此处执行复制是由于gemm不支持视图向量.
					rightArray = rightArray.dup();
				}
				if (rightArray.columns() == 1) {
					rightArray = rightArray.transpose();
				}
				INDArray dataArray = this.getArray();
				leftArray.mmul(rightArray, dataArray);
				return this;
			}
		} else {
			return MathMatrix.super.dotProduct(rowVector, columnVector, mode);
		}
	}

	@Override
	public MathMatrix accumulateProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
		if (leftMatrix instanceof Nd4jMatrix && rightMatrix instanceof Nd4jMatrix) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = leftTranspose ? Nd4jMatrix.class.cast(leftMatrix).getArray().transpose() : Nd4jMatrix.class.cast(leftMatrix).getArray();
				INDArray rightArray = rightTranspose ? Nd4jMatrix.class.cast(rightMatrix).getArray().transpose() : Nd4jMatrix.class.cast(rightMatrix).getArray();
				INDArray dataArray = this.getArray();
				INDArray cacheArray = Nd4j.zeros(dataArray.shape(), dataArray.ordering());
				leftArray.mmul(rightArray, cacheArray);
				dataArray.addi(cacheArray);
				return this;
			}
		} else {
			return MathMatrix.super.accumulateProduct(leftMatrix, leftTranspose, rightMatrix, rightTranspose, mode);
		}
	}

	@Override
	public MathMatrix accumulateProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
		if (rowVector instanceof Nd4jVector && columnVector instanceof Nd4jVector) {
			EnvironmentThread thread = EnvironmentThread.currentThread();
			try (MemoryWorkspace workspace = thread.getSpace()) {
				INDArray leftArray = Nd4jVector.class.cast(rowVector).getArray();
				// TODO 此处需要想方案优化,否则存在性能问题.
				if (leftArray.isView()) {
					// 此处执行复制是由于gemm不支持视图向量.
					leftArray = leftArray.dup();
				}
				if (leftArray.rows() == 1) {
					leftArray = leftArray.transpose();
				}
				INDArray rightArray = Nd4jVector.class.cast(columnVector).getArray();
				if (rightArray.isView()) {
					// 此处执行复制是由于gemm不支持视图向量.
					rightArray = rightArray.dup();
				}
				if (rightArray.columns() == 1) {
					rightArray = rightArray.transpose();
				}
				INDArray dataArray = this.getArray();
				INDArray cacheArray = Nd4j.zeros(dataArray.shape(), dataArray.ordering());
				leftArray.mmul(rightArray, cacheArray);
				dataArray.addi(cacheArray);
				return this;
			}
		} else {
			return MathMatrix.super.accumulateProduct(rowVector, columnVector, mode);
		}
	}

	public INDArray getArray() {
		return matrix;
	}

	@Override
	public void beforeSave() {
		data = new float[rowSize * columnSize];
		FloatPointer pointer = (FloatPointer) matrix.data().pointer();
		pointer.get(data, 0, data.length);
	}

	@Override
	public void afterLoad() {
		matrix = Nd4j.zeros(rowSize, columnSize, order);
		manager.ensureLocation(matrix, Location.HOST);
		manager.tagLocation(matrix, Location.HOST);
		FloatPointer pointer = (FloatPointer) matrix.data().pointer();
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
		Nd4jMatrix that = (Nd4jMatrix) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.matrix, that.matrix);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(matrix);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return matrix.toString();
	}

	@Override
	public Iterator<MatrixScalar> iterator() {
		return new Nd4jMatrixIterator();
	}

	private class Nd4jMatrixIterator implements Iterator<MatrixScalar> {

		private int cursor = 0;

		private Nd4jMatrixScalar term = new Nd4jMatrixScalar();

		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public MatrixScalar next() {
			term.update(cursor++);
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class Nd4jMatrixScalar implements MatrixScalar {

		private int row, column, section = matrix.columns();

		private void update(int cursor) {
			this.row = cursor / section;
			this.column = cursor % section;
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
			return matrix.getFloat(row, column);
		}

		@Override
		public void scaleValue(float value) {
			matrix.putScalar(row, column, matrix.getFloat(row, column) * value);
		}

		@Override
		public void setValue(float value) {
			matrix.putScalar(row, column, value);
		}

		@Override
		public void shiftValue(float value) {
			matrix.putScalar(row, column, matrix.getFloat(row, column) + value);
		}

	}

}
