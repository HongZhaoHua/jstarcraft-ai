package com.jstarcraft.ai.math.algorithm.calculus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculusFunctionTestCase {

    @Test
    public void test() {
        /* x^2 + y^2 = 5^2 */
        /* y = √(5^2 - x^2) */
        CalculusFunction left = new ConstantCalculusFunction(-1F);
        CalculusFunction right = new PowerCalculusFunction(2F);
        right = new MultiplicationCalculusFunction(left, right);
        left = new ConstantCalculusFunction(25F);
        right = new AdditionCalculusFunction(left, right);
        left = new PowerCalculusFunction(0.5F);
        right = new ChainCalculusFunction(left, right);
        assertEquals(4F, right.primitive(3F), 0F);
        assertEquals(-0.75F, right.derivative(3F), 0F);
    }

}
