package com.jstarcraft.ai.text;

import java.util.List;

import com.jstarcraft.ai.text.NormalizationTermFrequency;
import com.jstarcraft.ai.text.TermFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;

public class NormalizationTermFrequencyTestCase extends TermFrequencyTestCase {

	@Override
	protected TermFrequency getTermFrequency(List<Integer> document) {
		return new NormalizationTermFrequency(new Int2FloatAVLTreeMap(), 0.5F, document);
	}

	@Override
	protected Int2FloatMap calculateTermFrequency(Int2FloatMap keyValues) {
		float denominator = 0F;
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			if (term.getFloatValue() > denominator) {
				denominator = term.getFloatValue();
			}
		}
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			term.setValue(0.5F + 0.5F * term.getFloatValue() / denominator);
		}
		return keyValues;
	}

}
