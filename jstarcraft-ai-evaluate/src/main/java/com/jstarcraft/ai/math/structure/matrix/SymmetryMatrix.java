package com.jstarcraft.ai.math.structure.matrix;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.ScalarIterator;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.SymmetryVector;

/**
 * 对称矩阵
 * 
 * @author Birdy
 *
 */
public class SymmetryMatrix implements MathMatrix {

	private int dimension;

	private float[] data;

	private int[] point;

	@Override
	public int getElementSize() {
		return data.length;
	}

	@Override
	public int getKnownSize() {
		return dimension * dimension;
	}

	@Override
	public int getUnknownSize() {
		return 0;
	}

	@Override
	public ScalarIterator<MatrixScalar> iterateElement(MathCalculator mode, MathAccessor<MatrixScalar>... accessors) {
		switch (mode) {
		case SERIAL: {
			SymmetryMatrixScalar scalar = new SymmetryMatrixScalar();
			int rowIndex = 0, columnIndex = 0, cursor = 0, size = data.length;
			while (cursor < size) {
				scalar.update(rowIndex, columnIndex, cursor);
				for (MathAccessor<MatrixScalar> accessor : accessors) {
					accessor.accessElement(scalar);
				}
				if (columnIndex < rowIndex) {
					columnIndex++;
				} else {
					rowIndex++;
					columnIndex = 0;
				}
				cursor++;
			}
			return this;
		}
		default: {
			if (this.getColumnSize() <= this.getRowSize()) {
				int size = dimension;
				EnvironmentContext context = EnvironmentContext.getContext();
				Semaphore semaphore = MathCalculator.getSemaphore();
				for (int index = 0; index < size; index++) {
					int columnIndex = index;
					context.doStructureByAny(index, () -> {
						SymmetryMatrixScalar scalar = new SymmetryMatrixScalar();
						for (int rowIndex = dimension - 1; rowIndex >= columnIndex; rowIndex--) {
							int cursor = point[rowIndex] + columnIndex;
							scalar.update(rowIndex, columnIndex, cursor);
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
				return this;
			} else {
				int size = dimension;
				EnvironmentContext context = EnvironmentContext.getContext();
				Semaphore semaphore = MathCalculator.getSemaphore();
				for (int index = 0; index < size; index++) {
					int rowIndex = index;
					context.doStructureByAny(index, () -> {
						SymmetryMatrixScalar scalar = new SymmetryMatrixScalar();
						for (int columnIndex = 0; columnIndex <= rowIndex; columnIndex++) {
							int cursor = point[rowIndex] + columnIndex;
							scalar.update(rowIndex, columnIndex, cursor);
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
				return this;
			}
		}
		}
	}

	@Override
	public SymmetryMatrix setValues(float value) {
		int rowIndex = 0, columnIndex = 0, cursor = 0, size = data.length;
		while (cursor < size) {
			data[cursor] = value;
			if (columnIndex < rowIndex) {
				columnIndex++;
			} else {
				rowIndex++;
				columnIndex = 0;
			}
			cursor++;
		}
		return this;
	}

	@Override
	public SymmetryMatrix scaleValues(float value) {
		int rowIndex = 0, columnIndex = 0, cursor = 0, size = data.length;
		while (cursor < size) {
			data[cursor] *= value;
			if (columnIndex < rowIndex) {
				columnIndex++;
			} else {
				rowIndex++;
				columnIndex = 0;
			}
			cursor++;
		}
		return this;
	}

	@Override
	public SymmetryMatrix shiftValues(float value) {
		int rowIndex = 0, columnIndex = 0, cursor = 0, size = data.length;
		while (cursor < size) {
			data[cursor] += value;
			if (columnIndex < rowIndex) {
				columnIndex++;
			} else {
				rowIndex++;
				columnIndex = 0;
			}
			cursor++;
		}
		return this;
	}

	@Override
	public float getSum(boolean absolute) {
		float sum = 0F;
		int rowIndex = 0, columnIndex = 0, cursor = 0, size = data.length;
		if (absolute) {
			while (cursor < size) {
				sum += FastMath.abs(data[cursor]);
				if (columnIndex < rowIndex) {
					columnIndex++;
				} else {
					rowIndex++;
					columnIndex = 0;
				}
				cursor++;
			}
		} else {
			while (cursor < size) {
				sum += data[cursor];
				if (columnIndex < rowIndex) {
					columnIndex++;
				} else {
					rowIndex++;
					columnIndex = 0;
				}
				cursor++;
			}
		}
		return sum;
	}

	@Override
	public int getRowSize() {
		return dimension;
	}

	@Override
	public int getColumnSize() {
		return dimension;
	}

	@Override
	public SymmetryVector getRowVector(int rowIndex) {
		return new SymmetryVector(dimension, rowIndex, point, data);
	}

	@Override
	public SymmetryVector getColumnVector(int columnIndex) {
		return new SymmetryVector(dimension, columnIndex, point, data);
	}

	@Override
	public boolean isIndexed() {
		return true;
	}

	private int getCursor(int rowIndex, int columnIndex) {
		int x, y;
		if (rowIndex >= columnIndex) {
			x = rowIndex;
			y = columnIndex;
		} else {
			x = columnIndex;
			y = rowIndex;
		}
		return point[x] + y;
	}

	@Override
	public float getValue(int rowIndex, int columnIndex) {
		int cursor = getCursor(rowIndex, columnIndex);
		return data[cursor];
	}

	@Override
	public void setValue(int rowIndex, int columnIndex, float value) {
		int cursor = getCursor(rowIndex, columnIndex);
		data[cursor] = value;
	}

	@Override
	public void scaleValue(int rowIndex, int columnIndex, float value) {
		int cursor = getCursor(rowIndex, columnIndex);
		data[cursor] *= value;
	}

	@Override
	public void shiftValue(int rowIndex, int columnIndex, float value) {
		int cursor = getCursor(rowIndex, columnIndex);
		data[cursor] += value;
	}

	@Override
	public MathMatrix addMatrix(MathMatrix matrix, boolean transpose) {
		assert matrix instanceof SymmetryMatrix;
		SymmetryMatrix that = SymmetryMatrix.class.cast(matrix);
		assert this.dimension == that.dimension;
		for (int index = 0, size = data.length; index < size; index++) {
			this.data[index] += that.data[index];
		}
		return this;
	}

	@Override
	public MathMatrix subtractMatrix(MathMatrix matrix, boolean transpose) {
		assert matrix instanceof SymmetryMatrix;
		SymmetryMatrix that = SymmetryMatrix.class.cast(matrix);
		assert this.dimension == that.dimension;
		for (int index = 0, size = data.length; index < size; index++) {
			this.data[index] -= that.data[index];
		}
		return this;
	}

	@Override
	public MathMatrix multiplyMatrix(MathMatrix matrix, boolean transpose) {
		assert matrix instanceof SymmetryMatrix;
		SymmetryMatrix that = SymmetryMatrix.class.cast(matrix);
		assert this.dimension == that.dimension;
		for (int index = 0, size = data.length; index < size; index++) {
			this.data[index] *= that.data[index];
		}
		return this;
	}

	@Override
	public MathMatrix divideMatrix(MathMatrix matrix, boolean transpose) {
		assert matrix instanceof SymmetryMatrix;
		SymmetryMatrix that = SymmetryMatrix.class.cast(matrix);
		assert this.dimension == that.dimension;
		for (int index = 0, size = data.length; index < size; index++) {
			this.data[index] /= that.data[index];
		}
		return this;
	}

	@Override
	public MathMatrix copyMatrix(MathMatrix matrix, boolean transpose) {
		assert matrix instanceof SymmetryMatrix;
		SymmetryMatrix that = SymmetryMatrix.class.cast(matrix);
		assert this.dimension == that.dimension;
		for (int index = 0, size = data.length; index < size; index++) {
			this.data[index] = that.data[index];
		}
		return this;
	}

	@Override
	public MathMatrix dotProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
		boolean isSymmetry = (leftMatrix == rightMatrix && leftTranspose != rightTranspose);
		assert isSymmetry;

		return MathMatrix.super.dotProduct(leftMatrix, leftTranspose, rightMatrix, rightTranspose, mode);
	}

	@Override
	public MathMatrix dotProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
		boolean isSymmetry = (rowVector == columnVector);
		assert isSymmetry;

		return MathMatrix.super.dotProduct(rowVector, columnVector, mode);
	}

	@Override
	public MathMatrix accumulateProduct(MathMatrix leftMatrix, boolean leftTranspose, MathMatrix rightMatrix, boolean rightTranspose, MathCalculator mode) {
		boolean isSymmetry = (leftMatrix == rightMatrix && leftTranspose != rightTranspose);
		assert isSymmetry;

		return MathMatrix.super.accumulateProduct(leftMatrix, leftTranspose, rightMatrix, rightTranspose, mode);
	}

	@Override
	public MathMatrix accumulateProduct(MathVector rowVector, MathVector columnVector, MathCalculator mode) {
		boolean isSymmetry = (rowVector == columnVector);
		assert isSymmetry;

		return MathMatrix.super.accumulateProduct(rowVector, columnVector, mode);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		SymmetryMatrix that = (SymmetryMatrix) object;
		EqualsBuilder equal = new EqualsBuilder();
		equal.append(this.dimension, that.dimension);
		equal.append(this.data, that.data);
		return equal.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(dimension);
		hash.append(data);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder string = new ToStringBuilder(this);
		string.append(dimension);
		string.append(data);
		return string.toString();
	}

	@Override
	public Iterator<MatrixScalar> iterator() {
		return new SymmetryMatrixIterator();
	}

	private class SymmetryMatrixIterator implements Iterator<MatrixScalar> {

		private int row, column, cursor, size = data.length - 1;

		private SymmetryMatrixScalar term = new SymmetryMatrixScalar();

		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public MatrixScalar next() {
			cursor = point[row] + column;
			term.update(row, column, cursor);
			if (column < row) {
				column++;
			} else {
				row++;
				column = 0;
			}
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class SymmetryMatrixScalar implements MatrixScalar {

		private int row, column, cursor;

		private void update(int row, int column, int cursor) {
			this.row = row;
			this.column = column;
			this.cursor = cursor;
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

	SymmetryMatrix() {
	}

	public SymmetryMatrix(int dimension) {
		this.dimension = dimension;
		this.point = new int[dimension];
		// 等差数列求和
		int size = 0;
		for (int index = 0; index < dimension; index++, size += index) {
			this.point[index] = size;
		}
		this.data = new float[size];
	}

}
