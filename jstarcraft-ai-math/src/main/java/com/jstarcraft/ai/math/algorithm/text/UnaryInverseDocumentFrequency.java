package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;
import java.util.Iterator;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntIterator;

/**
 * Inverse Document Frequency
 * 
 * <pre>
 * 1
 * </pre>
 * 
 * @author Birdy
 *
 */
public class UnaryInverseDocumentFrequency extends AbstractInverseDocumentFrequency {

	public UnaryInverseDocumentFrequency(Int2FloatMap keyValues, TermFrequency... documents) {
		super(keyValues);
		for (TermFrequency document : documents) {
			IntIterator iterator = document.getKeys().iterator();
			while (iterator.hasNext()) {
				int term = iterator.nextInt();
				keyValues.put(term, 1F);
			}
		}
	}

	public UnaryInverseDocumentFrequency(Int2FloatMap keyValues, Collection<TermFrequency> documents) {
		super(keyValues);
		for (TermFrequency document : documents) {
			IntIterator iterator = document.getKeys().iterator();
			while (iterator.hasNext()) {
				int term = iterator.nextInt();
				keyValues.put(term, 1F);
			}
		}
	}

	public UnaryInverseDocumentFrequency(Int2FloatMap keyValues, Iterator<TermFrequency> documents) {
		super(keyValues);
		while (documents.hasNext()) {
			TermFrequency document = documents.next();
			IntIterator iterator = document.getKeys().iterator();
			while (iterator.hasNext()) {
				int term = iterator.nextInt();
				keyValues.put(term, 1F);
			}
		}
	}

}
