/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jstarcraft.ai.jsat.utils;



import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 *
 * @author Edward Raff
 */
public class ListUtilsTest {

    public ListUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of splitList method, of class ListUtils.
     */
    @Test
    public void testSplitList() {
        System.out.println("splitList");
        IntArrayList sourceList = new IntArrayList(500);
        for (int i = 0; i < 500; i++)
            sourceList.add(i);
        List<List<Integer>> ll1 = ListUtils.splitList(sourceList, 5);
        Assert.assertEquals(5, ll1.size());

        for (int i = 0; i < 5; i++) {
            List<Integer> l = ll1.get(i);
            Assert.assertEquals(100, l.size());
            for (int j = 0; j < l.size(); j++)
                Assert.assertEquals(i * 100 + j, l.get(j).intValue());// intValue called b/c it becomes ambigous to the compiler without it
        }

        ll1 = ListUtils.splitList(sourceList, 7);// Non divisible amount
        Assert.assertEquals(7, ll1.size());
        int pos = 0;
        for (List<Integer> l : ll1) {
            Assert.assertTrue("List should have had only 71 or 72 values", l.size() == 72 || l.size() == 71);
            for (int j = 0; j < l.size(); j++) {
                Assert.assertEquals(pos + j, l.get(j).intValue());
            }
            pos += l.size();
        }
        Assert.assertEquals(500, pos);
    }

    @Test
    public void testSwap() {
        System.out.println("swap");
        IntArrayList test = new IntArrayList();
        test.add(1);
        test.add(2);
        ListUtils.swap(test, 0, 1);
        Assert.assertEquals(2, test.getInt(0));
        Assert.assertEquals(1, test.getInt(1));

        ListUtils.swap(test, 0, 1);
        Assert.assertEquals(1, test.getInt(0));
        Assert.assertEquals(2, test.getInt(1));

        ListUtils.swap(test, 0, 0);
        Assert.assertEquals(1, test.getInt(0));
        Assert.assertEquals(2, test.getInt(1));
    }
}
