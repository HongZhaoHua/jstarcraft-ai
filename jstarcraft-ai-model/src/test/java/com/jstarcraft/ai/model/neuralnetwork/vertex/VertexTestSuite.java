package com.jstarcraft.ai.model.neuralnetwork.vertex;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        // 节点测试集
        EuclideanVertexTestCase.class,

        HorizontalStackVertexTestCase.class,

        HorizontalUnstackVertexTestCase.class,

        MinusVertexTestCase.class,

        MultiplyVertexTestCase.class,

        PlusVertexTestCase.class,

        ShareVertexTestCase.class,

        VerticalStackVertexTestCase.class,

        VerticalUnstackVertexTestCase.class, })
public class VertexTestSuite {

}
