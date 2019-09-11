package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;
import java.util.Iterator;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;

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
public class UnaryInverseDocumentFrequency implements InverseDocumentFrequency {

    private Int2FloatMap keyValues;

    public UnaryInverseDocumentFrequency(Int2FloatMap keyValues, TermFrequency... documents) {
        for (TermFrequency document : documents) {
            IntIterator iterator = document.getKeys().iterator();
            while (iterator.hasNext()) {
                int term = iterator.nextInt();
                keyValues.put(term, 1F);
            }
        }
        this.keyValues = keyValues;
    }

    public UnaryInverseDocumentFrequency(Int2FloatMap keyValues, Collection<TermFrequency> documents) {
        for (TermFrequency document : documents) {
            IntIterator iterator = document.getKeys().iterator();
            while (iterator.hasNext()) {
                int term = iterator.nextInt();
                keyValues.put(term, 1F);
            }
        }
        this.keyValues = keyValues;
    }

    public UnaryInverseDocumentFrequency(Int2FloatMap keyValues, Iterator<TermFrequency> documents) {
        while (documents.hasNext()) {
            TermFrequency document = documents.next();
            IntIterator iterator = document.getKeys().iterator();
            while (iterator.hasNext()) {
                int term = iterator.nextInt();
                keyValues.put(term, 1F);
            }
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
