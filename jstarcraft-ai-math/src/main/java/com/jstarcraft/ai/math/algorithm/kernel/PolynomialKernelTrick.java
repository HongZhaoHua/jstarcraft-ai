package com.jstarcraft.ai.math.algorithm.kernel;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 多项式核(Polynomial Kernel)
 * 
 * @author Birdy
 *
 */
public class PolynomialKernelTrick implements KernelTrick {

    private float a;

    private float c;

    private float d;

    public PolynomialKernelTrick(float a, float c, float d) {
        this.a = a;
        this.c = c;
        this.d = d;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        MathScalar scalar = DefaultScalar.getInstance();
        return (float) FastMath.pow(a * scalar.dotProduct(leftVector, rightVector).getValue() + c, d);
    }

}
