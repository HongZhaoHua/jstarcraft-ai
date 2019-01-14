package com.jstarcraft.ai.data.module;

import java.util.Iterator;
import java.util.Map.Entry;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.utility.IntegerArray;
import com.jstarcraft.core.utility.KeyValue;

import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * 引用模块
 * 
 * @author Birdy
 *
 */
public class ReferenceModule implements DataModule {

	/** 引用 */
	private IntegerArray references;

	/** 模型 */
	private DataModule module;

	public ReferenceModule(IntegerArray references, DataModule module) {
		this.references = references;
		this.module = module;
	}

	@Override
	public int getDiscreteOrder() {
		return module.getDiscreteOrder();
	}

	@Override
	public int getContinuousOrder() {
		return module.getContinuousOrder();
	}

	@Override
	public void associateInstance(Int2IntSortedMap discreteFeatures, Int2FloatSortedMap continuousFeatures) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DataInstance getInstance(int cursor) {
		return new ReferenceInstance(cursor, references, module);
	}

	@Override
	public int getSize() {
		return references.getSize();
	}

	@Override
	public Entry<Integer, KeyValue<String, Boolean>> getOuterKeyValue(int index) {
		return module.getOuterKeyValue(index);
	}

	@Override
	public int getDiscreteInner(String name) {
		return module.getDiscreteInner(name);
	}

	@Override
	public int getContinuousInner(String name) {
		return module.getContinuousInner(name);
	}

	@Override
	public Iterator<DataInstance> iterator() {
		return new ReferenceModuleIterator();
	}

	private class ReferenceModuleIterator implements Iterator<DataInstance> {

		private int cursor, size = ReferenceModule.this.getSize();

		private DataInstance term = new ReferenceInstance(cursor, ReferenceModule.this.references, ReferenceModule.this.module);

		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public DataInstance next() {
			term.setCursor(cursor++);
			return term;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
