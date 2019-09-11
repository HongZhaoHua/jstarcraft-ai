package com.jstarcraft.ai.math.algorithm.text;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.text.LogarithmTermFrequency;
import com.jstarcraft.ai.math.algorithm.text.TermFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;

public class LogarithmTermFrequencyTestCase extends TermFrequencyTestCase {

    @Override
    protected TermFrequency getTermFrequency(List<Integer> document) {
        return new LogarithmTermFrequency(new Int2FloatAVLTreeMap(), document);
    }

    @Override
    protected Int2FloatMap calculateTermFrequency(Int2FloatMap keyValues) {
        for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
            term.setValue((float) FastMath.log(1F + term.getFloatValue()));
        }
        return keyValues;
    }

}
