package com.jstarcraft.ai.math.structure.table;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCell;
import com.jstarcraft.ai.math.structure.MathTable;

public class DenseTable<T> implements MathTable<T> {

	/** 方向(true为按行,false为按列) */
	private boolean orientation;

	/** 大小 */
	private int rowSize, columnSize;

	/** 数据 */
	private T[] cells;

	public DenseTable(boolean orientation, int rowSize, int columnSize, T[] cells) {
		this.orientation = orientation;
		this.rowSize = rowSize;
		this.columnSize = columnSize;
		this.cells = cells;
	}

	@Override
	public DenseTable<T> setValues(T value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getElementSize() {
		return rowSize * columnSize;
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
	public DenseTable<T> iterateElement(MathCalculator mode, MathAccessor<MathCell<T>>... accessors) {
		switch (mode) {
		case SERIAL: {
			DenseTableCell cell = new DenseTableCell();
			if (orientation) {
				for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
					for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
						int cursor = rowIndex * columnSize + columnIndex;
						cell.update(cursor);
						for (MathAccessor<MathCell<T>> accessor : accessors) {
							accessor.accessElement(cell);
						}
					}
				}
				return this;
			} else {
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
						int cursor = columnIndex * rowSize + rowIndex;
						cell.update(cursor);
						for (MathAccessor<MathCell<T>> accessor : accessors) {
							accessor.accessElement(cell);
						}
					}
				}
				return this;
			}
		}
		default: {
			if (orientation) {
				int size = rowSize;
				EnvironmentContext context = EnvironmentContext.getContext();
				Semaphore semaphore = MathCalculator.getSemaphore();
				for (int index = 0; index < size; index++) {
					int rowIndex = index;
					context.doStructureByAny(index, () -> {
						DenseTableCell cell = new DenseTableCell();
						for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
							int cursor = rowIndex * columnSize + columnIndex;
							cell.update(cursor);
							for (MathAccessor<MathCell<T>> accessor : accessors) {
								accessor.accessElement(cell);
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
				int size = columnSize;
				EnvironmentContext context = EnvironmentContext.getContext();
				Semaphore semaphore = MathCalculator.getSemaphore();
				for (int index = 0; index < size; index++) {
					int columnIndex = index;
					context.doStructureByAny(index, () -> {
						DenseTableCell cell = new DenseTableCell();
						for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
							int cursor = rowIndex * columnSize + columnIndex;
							cell.update(cursor);
							for (MathAccessor<MathCell<T>> accessor : accessors) {
								accessor.accessElement(cell);
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
	public int getRowSize() {
		return rowSize;
	}

	@Override
	public int getColumnSize() {
		return columnSize;
	}

	@Override
	public T getValue(int rowIndex, int columnIndex) {
		if (orientation) {
			return cells[rowIndex * columnSize + columnIndex];
		} else {
			return cells[columnIndex * rowSize + rowIndex];
		}
	}

	@Override
	public void setValue(int rowIndex, int columnIndex, T value) {
		if (orientation) {
			cells[rowIndex * columnSize + columnIndex] = value;
		} else {
			cells[columnIndex * rowSize + rowIndex] = value;
		}
	}

	@Override
	public boolean getOrientation() {
		return orientation;
	}

	@Override
	public Iterator<MathCell<T>> iterator() {
		return new DenseTableIterator();
	}

	private class DenseTableIterator implements Iterator<MathCell<T>> {

		private int size = rowSize * columnSize;

		private int cursor;

		private DenseTableCell term = new DenseTableCell();

		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public MathCell<T> next() {
			term.update(cursor++);
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class DenseTableCell implements MathCell<T> {

		private int index;

		private void update(int index) {
			this.index = index;
		}

		@Override
		public int getRow() {
			return orientation ? index / columnSize : index % rowSize;
		}

		@Override
		public int getColumn() {
			return orientation ? index % columnSize : index / rowSize;
		}

		@Override
		public T getValue() {
			return cells[index];
		}

		@Override
		public void setValue(T value) {
			cells[index] = value;
		}

	}

}
