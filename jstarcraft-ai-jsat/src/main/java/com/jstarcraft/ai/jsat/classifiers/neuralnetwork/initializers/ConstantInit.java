package com.jstarcraft.ai.jsat.classifiers.neuralnetwork.initializers;

import java.util.Random;

import com.jstarcraft.ai.jsat.linear.ConstantVector;
import com.jstarcraft.ai.jsat.linear.Vec;

/**
 * This initializes all bias values to a single constant value
 * 
 * @author Edward Raff
 */
public class ConstantInit implements BiastInitializer {

    private static final long serialVersionUID = 2638413936718283757L;
    private double c;

    /**
     * 
     * @param c the constant to set all biases to
     */
    public ConstantInit(double c) {
        this.c = c;
    }

    /**
     * 
     * @param c the constant value to use
     */
    public void setConstant(double c) {
        if (Double.isNaN(c) || Double.isInfinite(c))
            throw new IllegalArgumentException("Constant must be a real value, not " + c);
        this.c = c;
    }

    /**
     * 
     * @return the constant value that will be used for initialization
     */
    public double getConstant() {
        return c;
    }

    @Override
    public void init(Vec b, int fanIn, Random rand) {
        new ConstantVector(c, b.length()).copyTo(b);
    }

    @Override
    public ConstantInit clone() {
        return new ConstantInit(c);
    }

}
