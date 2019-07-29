package com.jstarcraft.ai.math.structure.table;

import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.jstarcraft.ai.math.structure.MathTable;

public class DenseTableTestCase extends TableTestCase {

	@Override
	protected MathTable<Integer> getTable(boolean orientation, int dimension, Table<Integer, Integer, Integer> data) {
		DenseTable<Integer> table = new DenseTable<>(orientation, dimension, dimension, new Integer[dimension * dimension]);
		for (Cell<Integer, Integer, Integer> cell : data.cellSet()) {
			table.setValue(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
		}
		return table;
	}

}
