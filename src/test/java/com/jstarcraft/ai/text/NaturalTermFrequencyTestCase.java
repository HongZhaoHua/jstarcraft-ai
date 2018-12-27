package com.jstarcraft.ai.text;

import java.util.List;

import com.jstarcraft.ai.text.NaturalTermFrequency;
import com.jstarcraft.ai.text.TermFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;

public class NaturalTermFrequencyTestCase extends TermFrequencyTestCase {

	@Override
	protected TermFrequency getTermFrequency(List<Integer> document) {
		return new NaturalTermFrequency(new Int2FloatAVLTreeMap(), document);
	}

	@Override
	protected Int2FloatMap calculateTermFrequency(Int2FloatMap keyValues) {
		float size = 0F;
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			size += term.getFloatValue();
		}
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			term.setValue(term.getFloatValue() / size);
		}
		return keyValues;
	}
}
