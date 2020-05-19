package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import org.apache.commons.math3.util.FastMath;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;

public class ProbabilisticInverseDocumentFrequencyTestCase extends InverseDocumentFrequencyTestCase {

    @Override
    protected InverseDocumentFrequency getInverseDocumentFrequency(Collection<TermFrequency> documents) {
        return new ProbabilisticInverseDocumentFrequency(new Int2FloatAVLTreeMap(), documents);
    }

    @Override
    protected Int2FloatMap calculateInverseDocumentFrequency(Int2FloatMap keyValues) {
        for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
            term.setValue((float) FastMath.log((2F - term.getFloatValue()) / term.getFloatValue()));
        }
        return keyValues;
    }

}
