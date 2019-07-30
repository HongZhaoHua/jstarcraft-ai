package com.jstarcraft.ai.evaluate;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public abstract class AbstractRankingEvaluatorTestCase extends AbstractEvaluatorTestCase<IntSet, IntList> {

    @Override
    protected IntSet getLeft(MathVector vector) {
        IntSet itemSet = new IntOpenHashSet();
        for (VectorScalar scalar : vector) {
            if (RandomUtility.randomFloat(1F) < 0.5F) {
                itemSet.add(scalar.getIndex());
            }
        }
        return itemSet;
    }

    @Override
    protected IntList getRight(MathVector vector) {
        IntList recommendList = new IntArrayList(vector.getElementSize());
        for (VectorScalar scalar : vector) {
            if (RandomUtility.randomFloat(1F) < 0.5F) {
                recommendList.add(scalar.getIndex());
            }
        }
        return recommendList;
    }

}
