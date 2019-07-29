package com.jstarcraft.ai.math.structure.table;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathAccessor;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCell;
import com.jstarcraft.ai.math.structure.MathTable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;

/**
 * 稀疏表单
 * 
 * @author Birdy
 *
 * @param <T>
 */
public class SparseTable<T> implements MathTable<T> {

	/** 大小 */
	private int rowSize, columnSize;

	/** 方向(true为按行,false为按列) */
	private boolean orientation;

	private transient Int2ObjectSortedMap<T> cells;

	public SparseTable(boolean orientation, int rowSize, int columnSize, Int2ObjectSortedMap<T> cells) {
		this.orientation = orientation;
		this.rowSize = rowSize;
		this.columnSize = columnSize;
		this.cells = cells;
	}

	@Override
	public SparseTable<T> setValues(T value) {
		for (Int2ObjectMap.Entry<T> term : cells.int2ObjectEntrySet()) {
			term.setValue(value);
		}
		return this;
	}

	@Override
	public int getElementSize() {
		return cells.size();
	}

	@Override
	public int getKnownSize() {
		return getElementSize();
	}

	@Override
	public int getUnknownSize() {
		return rowSize * columnSize - getElementSize();
	}

	@Override
	public SparseTable<T> iterateElement(MathCalculator mode, MathAccessor<MathCell<T>>... accessors) {
		switch (mode) {
		case SERIAL: {
			SparseTableCell cell = new SparseTableCell();
			if (orientation) {
				for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
					int from = rowIndex * columnSize;
					int to = rowIndex * columnSize + columnSize;
					for (Entry<T> element : cells.subMap(from, to).int2ObjectEntrySet()) {
						cell.update(element);
						for (MathAccessor<MathCell<T>> accessor : accessors) {
							accessor.accessElement(cell);
						}
					}
				}
				return this;
			} else {
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					int from = columnIndex * rowSize;
					int to = columnIndex * rowSize + rowSize;
					for (Entry<T> element : cells.subMap(from, to).int2ObjectEntrySet()) {
						cell.update(element);
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
						SparseTableCell cell = new SparseTableCell();
						int from = rowIndex * columnSize;
						int to = rowIndex * columnSize + columnSize;
						for (Entry<T> element : cells.subMap(from, to).int2ObjectEntrySet()) {
							cell.update(element);
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
						SparseTableCell cell = new SparseTableCell();
						int from = columnIndex * rowSize;
						int to = columnIndex * rowSize + rowSize;
						for (Entry<T> element : cells.subMap(from, to).int2ObjectEntrySet()) {
							cell.update(element);
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
			return cells.get(rowIndex * columnSize + columnIndex);
		} else {
			return cells.get(columnIndex * rowSize + rowIndex);
		}
	}

	@Override
	public void setValue(int rowIndex, int columnIndex, T value) {
		if (orientation) {
			cells.put(rowIndex * columnSize + columnIndex, value);
		} else {
			cells.put(columnIndex * rowSize + rowIndex, value);
		}
	}

	@Override
	public boolean getOrientation() {
		return orientation;
	}

	@Override
	public Iterator<MathCell<T>> iterator() {
		return new SparseTableIterator();
	}

	private class SparseTableIterator implements Iterator<MathCell<T>> {

		private Iterator<Entry<T>> iterator = cells.int2ObjectEntrySet().iterator();

		private final SparseTableCell term = new SparseTableCell();

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public MathCell<T> next() {
			term.update(iterator.next());
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class SparseTableCell implements MathCell<T> {

		private Entry<T> element;

		private void update(Entry<T> element) {
			this.element = element;
		}

		@Override
		public int getRow() {
			return orientation ? element.getIntKey() / columnSize : element.getIntKey() % rowSize;
		}

		@Override
		public int getColumn() {
			return orientation ? element.getIntKey() % columnSize : element.getIntKey() / rowSize;
		}

		@Override
		public T getValue() {
			return element.getValue();
		}

		@Override
		public void setValue(T value) {
			element.setValue(value);
		}

	}

}
