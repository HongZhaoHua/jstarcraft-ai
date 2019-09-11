package com.jstarcraft.ai.data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.attribute.QualityAttribute;

public class DataSpaceTestCase {

    @Test
    public void test() {
        Map<String, Class<?>> qualityDifinitions = new HashMap<>();
        Map<String, Class<?>> quantityDifinitions = new HashMap<>();
        qualityDifinitions.put("user", int.class);
        qualityDifinitions.put("item", int.class);
        quantityDifinitions.put("score", float.class);
        DataSpace space = new DataSpace(qualityDifinitions, quantityDifinitions);

        // 获取数据属性
        QualityAttribute userAttribute = space.getQualityAttribute("user");
        Assert.assertNotNull(userAttribute);
        QualityAttribute itemAttribute = space.getQualityAttribute("item");
        Assert.assertNotNull(itemAttribute);
        QuantityAttribute scoreAttribute = space.getQuantityAttribute("score");
        Assert.assertNotNull(scoreAttribute);

        // 制造数据模型
        {
            TreeMap<Integer, String> configuration = new TreeMap<>();
            configuration.put(1, "user");
            configuration.put(2, "item");
            configuration.put(3, "score");
            DataModule sparseModel = space.makeSparseModule("sparse", configuration, 1000);
            Assert.assertEquals(2, sparseModel.getQualityOrder());
            Assert.assertEquals(1, sparseModel.getQuantityOrder());
        }

        {
            TreeMap<Integer, String> configuration = new TreeMap<>();
            configuration.put(2, "user");
            DataModule denseModel = space.makeDenseModule("dense", configuration, 1000);
            Assert.assertEquals(2, denseModel.getQualityOrder());
            Assert.assertEquals(0, denseModel.getQuantityOrder());
        }
    }

}
