package com.jstarcraft.ai.math.algorithm.decomposition;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.utility.MathUtility;

/**
 * 奇异值分解
 * 
 * <pre>
 * 参考(https://math.nist.gov/javanumerics/jama/)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class SingularValueDecomposition {

	private DenseMatrix uMatrix, vMatrix;

	private DenseVector singularVector;

	private int rowSize, columnSize;

	private static float hypot(float x, float y) {
		float value;
		if (Math.abs(x) > Math.abs(y)) {
			value = y / x;
			value = (float) (Math.abs(x) * Math.sqrt(1 + value * value));
		} else if (!MathUtility.equal(y, 0F)) {
			value = x / y;
			value = (float) (Math.abs(y) * Math.sqrt(1 + value * value));
		} else {
			value = 0F;
		}
		return value;
	}

	public SingularValueDecomposition(MathMatrix matrix) {
		assert matrix.getRowSize() >= matrix.getColumnSize();
		// Derived from LINPACK code.

		rowSize = matrix.getRowSize();
		columnSize = matrix.getColumnSize();
		float[][] data = new float[rowSize][columnSize];
		for (int row = 0; row < rowSize; row++) {
			for (int column = 0; column < columnSize; column++) {
				data[row][column] = matrix.getValue(row, column);
			}
		}

		float[][] uValues = new float[rowSize][columnSize];
		float[][] vValues = new float[columnSize][columnSize];
		float[] singularValues = new float[Math.min(rowSize + 1, columnSize)];
		float[] eigenValues = new float[columnSize];
		float[] work = new float[rowSize];

		// Reduce data to bidiagonal form, storing the diagonal elements
		// in s and the super-diagonal elements in eigenvalue.
		int bidiagonalColumn = Math.min(rowSize - 1, columnSize);
		int bidiagonalRow = Math.max(0, Math.min(columnSize - 2, rowSize));
		for (int index = 0; index < Math.max(bidiagonalColumn, bidiagonalRow); index++) {
			if (index < bidiagonalColumn) {
				// Compute the transformation for the index-th column and
				// place the index-th diagonal in singularVector[index].
				// Compute 2-norm of index-th column without under/overflow.
				singularValues[index] = 0F;
				for (int row = index; row < rowSize; row++) {
					singularValues[index] = hypot(singularValues[index], data[row][index]);
				}
				if (singularValues[index] != 0F) {
					if (data[index][index] < 0F) {
						singularValues[index] = -singularValues[index];
					}
					for (int row = index; row < rowSize; row++) {
						data[row][index] /= singularValues[index];
					}
					data[index][index] += 1F;
				}
				singularValues[index] = -singularValues[index];
			}
			for (int column = index + 1; column < columnSize; column++) {
				if ((index < bidiagonalColumn) & (singularValues[index] != 0D)) {
					// Apply the transformation.
					double transformation = 0D;
					for (int row = index; row < rowSize; row++) {
						transformation += data[row][index] * data[row][column];
					}
					transformation = -transformation / data[index][index];
					for (int row = index; row < rowSize; row++) {
						data[row][column] += transformation * data[row][index];
					}
				}
				// Place the index-th row of data into e for the
				// subsequent calculation of the row transformation.
				eigenValues[column] = data[index][column];
			}
			if (index < bidiagonalColumn) {
				// Place the transformation in uMatrix for subsequent back
				// multiplication.
				for (int row = index; row < rowSize; row++) {
					uValues[row][index] = data[row][index];
				}
			}
			if (index < bidiagonalRow) {
				// Compute the index-th row transformation and place the
				// index-th super-diagonal in e[index].
				// Compute 2-norm without under/overflow.
				eigenValues[index] = 0F;
				for (int column = index + 1; column < columnSize; column++) {
					eigenValues[index] = hypot(eigenValues[index], eigenValues[column]);
				}
				if (eigenValues[index] != 0F) {
					if (eigenValues[index + 1] < 0F) {
						eigenValues[index] = -eigenValues[index];
					}
					for (int column = index + 1; column < columnSize; column++) {
						eigenValues[column] /= eigenValues[index];
					}
					eigenValues[index + 1] += 1F;
				}
				eigenValues[index] = -eigenValues[index];
				if ((index + 1 < rowSize) & (eigenValues[index] != 0F)) {
					// Apply the transformation.
					for (int row = index + 1; row < rowSize; row++) {
						work[row] = 0F;
					}
					for (int column = index + 1; column < columnSize; column++) {
						for (int row = index + 1; row < rowSize; row++) {
							work[row] += eigenValues[column] * data[row][column];
						}
					}
					for (int column = index + 1; column < columnSize; column++) {
						double transformation = -eigenValues[column] / eigenValues[index + 1];
						for (int row = index + 1; row < rowSize; row++) {
							data[row][column] += transformation * work[row];
						}
					}
				}
				// Place the transformation in vMatrix for subsequent
				// back multiplication.
				for (int column = index + 1; column < columnSize; column++) {
					vValues[column][index] = eigenValues[column];
				}
			}
		}

		// Set up the final bidiagonal matrix or order p.

		int outerPosition = Math.min(columnSize, rowSize + 1);
		if (bidiagonalColumn < columnSize) {
			singularValues[bidiagonalColumn] = data[bidiagonalColumn][bidiagonalColumn];
		}
		if (rowSize < outerPosition) {
			singularValues[outerPosition - 1] = 0F;
		}
		if (bidiagonalRow + 1 < outerPosition) {
			eigenValues[bidiagonalRow] = data[bidiagonalRow][outerPosition - 1];
		}
		eigenValues[outerPosition - 1] = 0F;

		// Generate U Matrix
		for (int column = bidiagonalColumn; column < columnSize; column++) {
			for (int row = 0; row < rowSize; row++) {
				uValues[row][column] = 0F;
			}
			uValues[column][column] = 1F;
		}
		for (int index = bidiagonalColumn - 1; index >= 0; index--) {
			if (singularValues[index] != 0F) {
				for (int column = index + 1; column < columnSize; column++) {
					double transformation = 0;
					for (int row = index; row < rowSize; row++) {
						transformation += uValues[row][index] * uValues[row][column];
					}
					transformation = -transformation / uValues[index][index];
					for (int row = index; row < rowSize; row++) {
						uValues[row][column] += transformation * uValues[row][index];
					}
				}
				for (int row = index; row < rowSize; row++) {
					uValues[row][index] = -uValues[row][index];
				}
				uValues[index][index] = 1F + uValues[index][index];
				for (int row = 0; row < index - 1; row++) {
					uValues[row][index] = 0F;
				}
			} else {
				for (int row = 0; row < rowSize; row++) {
					uValues[row][index] = 0F;
				}
				uValues[index][index] = 1F;
			}
		}

		// Generate V Matrix
		// 注意此处是转置矩阵
		for (int index = columnSize - 1; index >= 0; index--) {
			if ((index < bidiagonalRow) & (eigenValues[index] != 0D)) {
				for (int row = index + 1; row < columnSize; row++) {
					double transformation = 0D;
					for (int column = index + 1; column < columnSize; column++) {
						transformation += vValues[column][index] * vValues[column][row];
					}
					transformation = -transformation / vValues[index + 1][index];
					for (int column = index + 1; column < columnSize; column++) {
						vValues[column][row] += transformation * vValues[column][index];
					}
				}
			}
			for (int column = 0; column < columnSize; column++) {
				vValues[column][index] = 0F;
			}
			vValues[index][index] = 1F;
		}

		// Main iteration loop for the singular values.
		int innerPosition = outerPosition - 1;
		int iteration = 0;
		float epsilon = (float) Math.pow(2F, -52F);
		float tiny = (float) Math.pow(2F, -966F);
		while (outerPosition > 0) {
			int outerIndex, kase;

			// Here is where a test for too many iterations would go.

			// This section of the program inspects for
			// negligible elements in the s and e arrays. On
			// completion the variables kase and k are set as follows.

			// kase = 1 if s(p) and e[k-1] are negligible and k<p
			// kase = 2 if s(k) is negligible and k<p
			// kase = 3 if e[k-1] is negligible, k<p, and
			// s(k), ..., s(p) are not negligible (qr step).
			// kase = 4 if e(p-1) is negligible (convergence).

			for (outerIndex = outerPosition - 2; outerIndex >= -1; outerIndex--) {
				if (outerIndex == -1) {
					break;
				}
				if (Math.abs(eigenValues[outerIndex]) <= tiny + epsilon * (Math.abs(singularValues[outerIndex]) + Math.abs(singularValues[outerIndex + 1]))) {
					eigenValues[outerIndex] = 0F;
					break;
				}
			}
			if (outerIndex == outerPosition - 2) {
				kase = 4;
			} else {
				int innerIndex;
				for (innerIndex = outerPosition - 1; innerIndex >= outerIndex; innerIndex--) {
					if (innerIndex == outerIndex) {
						break;
					}
					double transformation = (innerIndex != outerPosition ? Math.abs(eigenValues[innerIndex]) : 0.) + (innerIndex != outerIndex + 1 ? Math.abs(eigenValues[innerIndex - 1]) : 0.);
					if (Math.abs(singularValues[innerIndex]) <= tiny + epsilon * transformation) {
						singularValues[innerIndex] = 0F;
						break;
					}
				}
				if (innerIndex == outerIndex) {
					kase = 3;
				} else if (innerIndex == outerPosition - 1) {
					kase = 1;
				} else {
					kase = 2;
					outerIndex = innerIndex;
				}
			}
			outerIndex++;

			// Perform the task indicated by kase.

			switch (kase) {

			// Deflate negligible s(p).

			case 1: {
				float eigenValue = eigenValues[outerPosition - 2];
				eigenValues[outerPosition - 2] = 0F;
				for (int row = outerPosition - 2; row >= outerIndex; row--) {
					float transformation = hypot(singularValues[row], eigenValue);
					float singularTransformation = singularValues[row] / transformation;
					float eigenTransformation = eigenValue / transformation;
					singularValues[row] = transformation;
					if (row != outerIndex) {
						eigenValue = -eigenTransformation * eigenValues[row - 1];
						eigenValues[row - 1] = singularTransformation * eigenValues[row - 1];
					}
					for (int column = 0; column < columnSize; column++) {
						transformation = singularTransformation * vValues[column][row] + eigenTransformation * vValues[column][outerPosition - 1];
						vValues[column][outerPosition - 1] = -eigenTransformation * vValues[column][row] + singularTransformation * vValues[column][outerPosition - 1];
						vValues[column][row] = transformation;
					}
				}
			}
				break;

			// Split at negligible s(k).

			case 2: {
				float eigenValue = eigenValues[outerIndex - 1];
				eigenValues[outerIndex - 1] = 0F;
				for (int column = outerIndex; column < outerPosition; column++) {
					float transformation = hypot(singularValues[column], eigenValue);
					float singularTransformation = singularValues[column] / transformation;
					float eigenTransformation = eigenValue / transformation;
					singularValues[column] = transformation;
					eigenValue = -eigenTransformation * eigenValues[column];
					eigenValues[column] = singularTransformation * eigenValues[column];
					for (int row = 0; row < rowSize; row++) {
						transformation = singularTransformation * uValues[row][column] + eigenTransformation * uValues[row][outerIndex - 1];
						uValues[row][outerIndex - 1] = -eigenTransformation * uValues[row][column] + singularTransformation * uValues[row][outerIndex - 1];
						uValues[row][column] = transformation;
					}
				}
			}
				break;

			// Perform one qr step.

			case 3: {

				// Calculate the shift.

				float scale = Math.max(Math.max(Math.max(Math.max(Math.abs(singularValues[outerPosition - 1]), Math.abs(singularValues[outerPosition - 2])), Math.abs(eigenValues[outerPosition - 2])), Math.abs(singularValues[outerIndex])), Math.abs(eigenValues[outerIndex]));
				float leftSingularValue = singularValues[outerPosition - 1] / scale;
				float rightSingularValue = singularValues[outerPosition - 2] / scale;
				float eigenValue = eigenValues[outerPosition - 2] / scale;

				float singularValue = ((rightSingularValue + leftSingularValue) * (rightSingularValue - leftSingularValue) + eigenValue * eigenValue) / 2F;
				eigenValue = (leftSingularValue * eigenValue) * (leftSingularValue * eigenValue);
				float shift = 0F;
				if ((singularValue != 0F) | (eigenValue != 0F)) {
					shift = (float) Math.sqrt(singularValue * singularValue + eigenValue);
					if (singularValue < 0F) {
						shift = -shift;
					}
					shift = eigenValue / (singularValue + shift);
				}

				singularValue = singularValues[outerIndex] / scale;
				eigenValue = eigenValues[outerIndex] / scale;
				eigenValue = singularValue * eigenValue;
				singularValue = (singularValue + leftSingularValue) * (singularValue - leftSingularValue) + shift;

				// Chase zeros.

				for (int column = outerIndex; column < outerPosition - 1; column++) {
					float transformation = hypot(singularValue, eigenValue);
					float singularTransformation = singularValue / transformation;
					float eigenTransformation = eigenValue / transformation;
					if (column != outerIndex) {
						eigenValues[column - 1] = transformation;
					}
					singularValue = singularTransformation * singularValues[column] + eigenTransformation * eigenValues[column];
					eigenValues[column] = singularTransformation * eigenValues[column] - eigenTransformation * singularValues[column];
					eigenValue = eigenTransformation * singularValues[column + 1];
					singularValues[column + 1] = singularTransformation * singularValues[column + 1];
					for (int row = 0; row < columnSize; row++) {
						transformation = singularTransformation * vValues[row][column] + eigenTransformation * vValues[row][column + 1];
						vValues[row][column + 1] = -eigenTransformation * vValues[row][column] + singularTransformation * vValues[row][column + 1];
						vValues[row][column] = transformation;
					}
					transformation = hypot(singularValue, eigenValue);
					singularTransformation = singularValue / transformation;
					eigenTransformation = eigenValue / transformation;
					singularValues[column] = transformation;
					singularValue = singularTransformation * eigenValues[column] + eigenTransformation * singularValues[column + 1];
					singularValues[column + 1] = -eigenTransformation * eigenValues[column] + singularTransformation * singularValues[column + 1];
					eigenValue = eigenTransformation * eigenValues[column + 1];
					eigenValues[column + 1] = singularTransformation * eigenValues[column + 1];
					if (column < rowSize - 1) {
						for (int row = 0; row < rowSize; row++) {
							transformation = singularTransformation * uValues[row][column] + eigenTransformation * uValues[row][column + 1];
							uValues[row][column + 1] = -eigenTransformation * uValues[row][column] + singularTransformation * uValues[row][column + 1];
							uValues[row][column] = transformation;
						}
					}
				}
				eigenValues[outerPosition - 2] = singularValue;
				iteration = iteration + 1;
			}
				break;

			// Convergence.

			case 4: {

				// Make the singular values positive.

				if (singularValues[outerIndex] <= 0F) {
					singularValues[outerIndex] = (singularValues[outerIndex] < 0F ? -singularValues[outerIndex] : 0F);
					for (int column = 0; column <= innerPosition; column++) {
						vValues[column][outerIndex] = -vValues[column][outerIndex];
					}
				}

				// Order the singular values.

				while (outerIndex < innerPosition) {
					if (singularValues[outerIndex] >= singularValues[outerIndex + 1]) {
						break;
					}
					float transformation = singularValues[outerIndex];
					singularValues[outerIndex] = singularValues[outerIndex + 1];
					singularValues[outerIndex + 1] = transformation;
					if (outerIndex < columnSize - 1) {
						for (int column = 0; column < columnSize; column++) {
							transformation = vValues[column][outerIndex + 1];
							vValues[column][outerIndex + 1] = vValues[column][outerIndex];
							vValues[column][outerIndex] = transformation;
						}
					}
					if (outerIndex < rowSize - 1) {
						for (int row = 0; row < rowSize; row++) {
							transformation = uValues[row][outerIndex + 1];
							uValues[row][outerIndex + 1] = uValues[row][outerIndex];
							uValues[row][outerIndex] = transformation;
						}
					}
					outerIndex++;
				}
				iteration = 0;
				outerPosition--;
			}
				break;
			}
		}

		singularVector = DenseVector.valueOf(singularValues.length, singularValues);
		uMatrix = DenseMatrix.valueOf(rowSize, columnSize);
		uMatrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(uValues[scalar.getRow()][scalar.getColumn()]);
		});
		vMatrix = DenseMatrix.valueOf(columnSize, columnSize);
		vMatrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(vValues[scalar.getRow()][scalar.getColumn()]);
		});
	}

	public DenseMatrix getU() {
		return uMatrix;
	}

	public DenseMatrix getV() {
		return vMatrix;
	}

	public DenseVector getS() {
		return singularVector;
	}

	/**
	 * 获取矩阵的范数
	 * 
	 * @return
	 */
	public float getNorm() {
		return singularVector.getValue(0);
	}

	/**
	 * 获取矩阵的条件数
	 * 
	 * @return
	 */
	public float getCondition() {
		return singularVector.getValue(0) / singularVector.getValue(Math.min(rowSize, columnSize) - 1);
	}

	/**
	 * 获取矩阵的秩
	 * 
	 * @return
	 */
	public int getOrder() {
		double epsilon = Math.pow(2D, -52D);
		epsilon = Math.max(rowSize, columnSize) * singularVector.getValue(0) * epsilon;
		int order = 0;
		for (int index = 0; index < singularVector.getElementSize(); index++) {
			if (singularVector.getValue(index) > epsilon) {
				order++;
			}
		}
		return order;
	}

}
