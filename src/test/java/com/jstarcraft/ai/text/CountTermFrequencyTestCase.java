package com.jstarcraft.ai.text;

import java.util.List;

import com.jstarcraft.ai.text.CountTermFrequency;
import com.jstarcraft.ai.text.TermFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;

public class CountTermFrequencyTestCase extends TermFrequencyTestCase {

	@Override
	protected TermFrequency getTermFrequency(List<Integer> document) {
		return new CountTermFrequency(new Int2FloatAVLTreeMap(), document);
	}

	@Override
	protected Int2FloatMap calculateTermFrequency(Int2FloatMap keyValues) {
		return keyValues;
	}
}
