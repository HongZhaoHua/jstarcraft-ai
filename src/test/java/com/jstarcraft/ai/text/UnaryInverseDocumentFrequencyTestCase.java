package com.jstarcraft.ai.text;

import java.util.Collection;

import com.jstarcraft.ai.text.InverseDocumentFrequency;
import com.jstarcraft.ai.text.TermFrequency;
import com.jstarcraft.ai.text.UnaryInverseDocumentFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;

public class UnaryInverseDocumentFrequencyTestCase extends InverseDocumentFrequencyTestCase {

	@Override
	protected InverseDocumentFrequency getInverseDocumentFrequency(Collection<TermFrequency> documents) {
		return new UnaryInverseDocumentFrequency(new Int2FloatAVLTreeMap(), documents);
	}

	@Override
	protected Int2FloatMap calculateInverseDocumentFrequency(Int2FloatMap keyValues) {
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			term.setValue(1F);
		}
		return keyValues;
	}

}
