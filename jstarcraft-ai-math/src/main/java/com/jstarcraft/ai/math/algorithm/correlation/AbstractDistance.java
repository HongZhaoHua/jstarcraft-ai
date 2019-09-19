package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * 抽象距离
 * 
 * @author Birdy
 *
 */
public abstract class AbstractDistance extends AbstractCorrelation {

    @Override
    public float getIdentical() {
        return 0F;
    }

}
