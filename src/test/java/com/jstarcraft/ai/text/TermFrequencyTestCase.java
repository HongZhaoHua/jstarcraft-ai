package com.jstarcraft.ai.text;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.jstarcraft.ai.text.TermFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;

public abstract class TermFrequencyTestCase {

	protected abstract TermFrequency getTermFrequency(List<Integer> document);

	protected abstract Int2FloatMap calculateTermFrequency(Int2FloatMap keyValues);

	@Test
	public void testTermFrequency() {
		{
			List<Integer> document = ImmutableList.of(0, 1, 2, 3, 0, 1);
			TermFrequency termFrequency = getTermFrequency(document);
			Int2FloatMap keyValues = new Int2FloatOpenHashMap();
			keyValues.put(0, 2F);
			keyValues.put(1, 2F);
			keyValues.put(2, 1F);
			keyValues.put(3, 1F);
			keyValues = calculateTermFrequency(keyValues);
			for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
				Assert.assertEquals(term.getFloatValue(), termFrequency.getValue(term.getIntKey()), 0F);
			}
		}

		{
			List<Integer> document = ImmutableList.of(2, 0, 4);
			TermFrequency termFrequency = getTermFrequency(document);
			Int2FloatMap keyValues = new Int2FloatOpenHashMap();
			keyValues.put(0, 1F);
			keyValues.put(2, 1F);
			keyValues.put(4, 1F);
			keyValues = calculateTermFrequency(keyValues);
			for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
				Assert.assertEquals(term.getFloatValue(), termFrequency.getValue(term.getIntKey()), 0F);
			}
		}
	}

}
