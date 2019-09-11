package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.text.InverseDocumentFrequency;
import com.jstarcraft.ai.math.algorithm.text.SmoothInverseDocumentFrequency;
import com.jstarcraft.ai.math.algorithm.text.TermFrequency;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;

public class SmoothInverseDocumentFrequencyTestCase extends InverseDocumentFrequencyTestCase {

    @Override
    protected InverseDocumentFrequency getInverseDocumentFrequency(Collection<TermFrequency> documents) {
        return new SmoothInverseDocumentFrequency(new Int2FloatAVLTreeMap(), documents);
    }

    @Override
    protected Int2FloatMap calculateInverseDocumentFrequency(Int2FloatMap keyValues) {
        for (Int2FloatMap.Entry term : keyValues.int2FloatEntrySet()) {
            term.setValue((float) FastMath.log(1F + 2F / term.getFloatValue()));
        }
        return keyValues;
    }

}
