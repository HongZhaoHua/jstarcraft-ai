package com.jstarcraft.ai.text;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.math3.util.FastMath;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Inverse Document Frequency
 * 
 * <pre>
 * smooth
 * </pre>
 * 
 * @author Birdy
 *
 */
public class SmoothInverseDocumentFrequency implements InverseDocumentFrequency {

	private Int2FloatMap keyValues;

	public SmoothInverseDocumentFrequency(Int2FloatMap keyValues, TermFrequency... documents) {
		int size = documents.length;
		for (TermFrequency document : documents) {
			IntIterator iterator = document.getKeys().iterator();
			while (iterator.hasNext()) {
				int term = iterator.nextInt();
				keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
			}
		}
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			int key = term.getIntKey();
			float value = term.getFloatValue();
			keyValues.put(key, (float) FastMath.log(1F + size / value));
		}
		this.keyValues = keyValues;
	}

	public SmoothInverseDocumentFrequency(Int2FloatMap keyValues, Collection<TermFrequency> documents) {
		int size = documents.size();
		for (TermFrequency document : documents) {
			IntIterator iterator = document.getKeys().iterator();
			while (iterator.hasNext()) {
				int term = iterator.nextInt();
				keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
			}
		}
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			int key = term.getIntKey();
			float value = term.getFloatValue();
			keyValues.put(key, (float) FastMath.log(1F + size / value));
		}
		this.keyValues = keyValues;
	}

	public SmoothInverseDocumentFrequency(Int2FloatMap keyValues, Iterator<TermFrequency> documents) {
		int size = 0;
		while (documents.hasNext()) {
			size++;
			TermFrequency document = documents.next();
			IntIterator iterator = document.getKeys().iterator();
			while (iterator.hasNext()) {
				int term = iterator.nextInt();
				keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
			}
		}
		for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
			int key = term.getIntKey();
			float value = term.getFloatValue();
			keyValues.put(key, (float) FastMath.log(1F + size / value));
		}
		this.keyValues = keyValues;
	}

	@Override
	public IntSet getKeys() {
		return keyValues.keySet();
	}

	@Override
	public float getValue(int key) {
		return keyValues.get(key);
	}

}
