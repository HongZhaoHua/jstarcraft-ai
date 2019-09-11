package com.jstarcraft.ai.math.algorithm.text;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.text.CountTermFrequency;
import com.jstarcraft.ai.math.algorithm.text.TermFrequency;

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
