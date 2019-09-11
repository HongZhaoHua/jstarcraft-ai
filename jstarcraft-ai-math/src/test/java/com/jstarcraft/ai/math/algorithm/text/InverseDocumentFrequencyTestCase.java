package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.jstarcraft.ai.math.algorithm.text.InverseDocumentFrequency;
import com.jstarcraft.ai.math.algorithm.text.NaturalTermFrequency;
import com.jstarcraft.ai.math.algorithm.text.TermFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;

public abstract class InverseDocumentFrequencyTestCase {

    protected abstract InverseDocumentFrequency getInverseDocumentFrequency(Collection<TermFrequency> documents);

    protected abstract Int2FloatMap calculateInverseDocumentFrequency(Int2FloatMap keyValues);

    @Test
    public void testInverseDocumentFrequency() {
        Collection<TermFrequency> documents = new LinkedList<>();
        {
            List<Integer> document = ImmutableList.of(0, 1, 2, 3, 0, 1);
            TermFrequency termFrequency = new NaturalTermFrequency(new Int2FloatAVLTreeMap(), document);
            documents.add(termFrequency);
        }
        {
            List<Integer> document = ImmutableList.of(2, 0, 4);
            TermFrequency termFrequency = new NaturalTermFrequency(new Int2FloatAVLTreeMap(), document);
            documents.add(termFrequency);
        }

        InverseDocumentFrequency inverseDocumentFrequency = getInverseDocumentFrequency(documents);
        Int2FloatMap keyValues = new Int2FloatOpenHashMap();
        keyValues.put(0, 2F);
        keyValues.put(1, 1F);
        keyValues.put(2, 2F);
        keyValues.put(3, 1F);
        keyValues.put(4, 1F);
        keyValues = calculateInverseDocumentFrequency(keyValues);
        for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
            Assert.assertEquals(term.getFloatValue(), inverseDocumentFrequency.getValue(term.getIntKey()), 0F);
        }
    }

}
