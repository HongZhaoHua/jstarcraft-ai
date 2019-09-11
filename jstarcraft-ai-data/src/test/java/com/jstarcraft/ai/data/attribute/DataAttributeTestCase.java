package com.jstarcraft.ai.data.attribute;

import org.junit.Test;

public abstract class DataAttributeTestCase {

    @Test
    abstract public void testConvertValue();

    @Test(timeout = 1000)
    abstract public void testConcurrent() throws Exception;

}
