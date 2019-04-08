package com.jstarcraft.ai.math.structure.table;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jstarcraft.ai.math.structure.MathCell;
import com.jstarcraft.ai.math.structure.MathTable;
import com.jstarcraft.core.utility.RandomUtility;

public abstract class TableTestCase {

	protected abstract MathTable<Integer> getTable(boolean orientation, int dimension, Table<Integer, Integer, Integer> data);

	@Test
	public void testTable() {
		int dimension = 10;
		Table<Integer, Integer, Integer> data = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				data.put(rowIndex, columnIndex, RandomUtility.randomInteger(dimension));
			}
		}
		{
			MathTable<Integer> table = getTable(true, dimension, data);
			for (MathCell<Integer> cell : table) {
				Assert.assertThat(cell.getValue(), CoreMatchers.equalTo(data.get(cell.getRow(), cell.getColumn())));
			}
			table.setValues(0);
			for (MathCell<Integer> cell : table) {
				Assert.assertThat(cell.getValue(), CoreMatchers.equalTo(0));
			}
		}
		{
			MathTable<Integer> table = getTable(false, dimension, data);
			for (MathCell<Integer> cell : table) {
				Assert.assertThat(cell.getValue(), CoreMatchers.equalTo(data.get(cell.getRow(), cell.getColumn())));
			}
			table.setValues(0);
			for (MathCell<Integer> cell : table) {
				Assert.assertThat(cell.getValue(), CoreMatchers.equalTo(0));
			}
		}
	}

}
