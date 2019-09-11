package com.jstarcraft.ai.math.algorithm.text;

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
public class MaximumInverseDocumentFrequency implements InverseDocumentFrequency {

    private Int2FloatMap keyValues;

    public MaximumInverseDocumentFrequency(Int2FloatMap keyValues, TermFrequency... documents) {
        float numerator = 0F;
        for (TermFrequency document : documents) {
            IntIterator iterator = document.getKeys().iterator();
            while (iterator.hasNext()) {
                int term = iterator.nextInt();
                float denominator = keyValues.getOrDefault(term, 0F) + 1F;
                keyValues.put(term, denominator);
                if (denominator > numerator) {
                    numerator = denominator;
                }
            }
        }
        for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
            int key = term.getIntKey();
            float value = term.getFloatValue();
            keyValues.put(key, (float) FastMath.log(numerator / (1 + value)));
        }
        this.keyValues = keyValues;
    }

    public MaximumInverseDocumentFrequency(Int2FloatMap keyValues, Collection<TermFrequency> documents) {
        float numerator = 0F;
        for (TermFrequency document : documents) {
            IntIterator iterator = document.getKeys().iterator();
            while (iterator.hasNext()) {
                int term = iterator.nextInt();
                float denominator = keyValues.getOrDefault(term, 0F) + 1F;
                keyValues.put(term, denominator);
                if (denominator > numerator) {
                    numerator = denominator;
                }
            }
        }
        for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
            int key = term.getIntKey();
            float value = term.getFloatValue();
            keyValues.put(key, (float) FastMath.log(numerator / (1 + value)));
        }
        this.keyValues = keyValues;
    }

    public MaximumInverseDocumentFrequency(Int2FloatMap keyValues, Iterator<TermFrequency> documents) {
        float numerator = 0F;
        while (documents.hasNext()) {
            TermFrequency document = documents.next();
            IntIterator iterator = document.getKeys().iterator();
            while (iterator.hasNext()) {
                int term = iterator.nextInt();
                float denominator = keyValues.getOrDefault(term, 0F) + 1F;
                keyValues.put(term, denominator);
                if (denominator > numerator) {
                    numerator = denominator;
                }
            }
        }
        for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
            int key = term.getIntKey();
            float value = term.getFloatValue();
            keyValues.put(key, (float) FastMath.log(numerator / (1 + value)));
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
