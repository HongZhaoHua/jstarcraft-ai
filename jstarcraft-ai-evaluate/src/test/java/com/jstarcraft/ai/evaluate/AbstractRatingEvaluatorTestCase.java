package com.jstarcraft.ai.evaluate;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;

public abstract class AbstractRatingEvaluatorTestCase extends AbstractEvaluatorTestCase<FloatList, FloatList> {

    @Override
    protected FloatList getLeft(MathVector vector) {
        FloatList scoreList = new FloatArrayList(vector.getElementSize());
        for (VectorScalar scalar : vector) {
            scoreList.add(scalar.getValue());
        }
        return scoreList;
    }

    @Override
    protected FloatList getRight(MathVector vector) {
        FloatList recommendList = new FloatArrayList(vector.getElementSize());
        for (VectorScalar scalar : vector) {
            if (RandomUtility.randomFloat(1F) < 0.5F) {
                recommendList.add(scalar.getValue());
            } else {
                recommendList.add(scalar.getValue() * 0.5F);
            }
        }
        return recommendList;
    }

}
