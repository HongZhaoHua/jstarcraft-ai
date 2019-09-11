package com.jstarcraft.ai.math.structure.table;

import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.jstarcraft.ai.math.structure.MathTable;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;

public class SparseTableTestCase extends TableTestCase {

    @Override
    protected MathTable<Integer> getTable(boolean orientation, int dimension, Table<Integer, Integer, Integer> data) {
        SparseTable<Integer> table = new SparseTable<>(orientation, dimension, dimension, new Int2ObjectAVLTreeMap<>());
        for (Cell<Integer, Integer, Integer> cell : data.cellSet()) {
            table.setValue(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
        return table;
    }

}
