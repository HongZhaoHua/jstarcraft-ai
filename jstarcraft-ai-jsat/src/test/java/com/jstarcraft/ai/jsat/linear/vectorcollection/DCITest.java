/*
 * Copyright (C) 2015 Edward Raff <Raff.Edward@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jstarcraft.ai.jsat.linear.vectorcollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.linear.DenseVector;
import com.jstarcraft.ai.jsat.linear.Vec;
import com.jstarcraft.ai.jsat.linear.VecPaired;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;
import com.jstarcraft.ai.jsat.utils.random.XORWOW;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 *
 * @author Edward Raff <Raff.Edward@gmail.com>
 */
public class DCITest {
    static List<VectorCollection<Vec>> collectionFactories;

    public DCITest() {
    }

    @BeforeClass
    public static void setUpClass() {
        collectionFactories = new ArrayList<>();
        collectionFactories.add(new DCI<>(25, 5));
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSearch_Vec_double() {
        System.out.println("search");
        Random rand = new XORWOW(123);

        VectorArray<Vec> vecCol = new VectorArray<>(new EuclideanDistance());
        for (int i = 0; i < 2500; i++)
            vecCol.add(DenseVector.random(3, rand));

        for (VectorCollection<Vec> factory : collectionFactories) {
            VectorCollection<Vec> collection0 = factory.clone();
            collection0.build(vecCol, new EuclideanDistance());
            VectorCollection<Vec> collection1 = factory.clone();
            collection1.build(true, vecCol, new EuclideanDistance());

            collection0 = collection0.clone();
            collection1 = collection1.clone();

            for (int iters = 0; iters < 10; iters++)
                for (double range : new double[] { 0.25, 0.5, 0.75, 2.0 }) {
                    int randIndex = rand.nextInt(vecCol.size());

                    IntArrayList nn_true = new IntArrayList();
                    IntArrayList nn_test = new IntArrayList();

                    DoubleArrayList nd_true = new DoubleArrayList();
                    DoubleArrayList nd_test = new DoubleArrayList();

                    vecCol.search(vecCol.get(randIndex), range, nn_true, nd_true);
                    collection0.search(vecCol.get(randIndex), range, nn_test, nd_test);

                    int found = (int) IntStream.of(nn_test.elements()).limit(nn_test.size()).filter(nn_true::contains).count();

                    // Since DCI is approximate, allow for missing half
                    assertEquals(nn_true.size(), found, nn_true.size() / 2.0);

                    collection1.search(vecCol.get(randIndex), range, nn_test, nd_test);

                    found = (int) IntStream.of(nn_test.elements()).limit(nn_test.size()).filter(nn_true::contains).count();

                    // Since DCI is approximate, allow for missing half
                    assertEquals(nn_true.size(), found, nn_true.size() / 2.0);
                }
        }

    }

    @Test
    public void testSearch_Vec_int() {
        System.out.println("search");
        Random rand = new XORWOW(123);

        VectorArray<Vec> vecCol = new VectorArray<>(new EuclideanDistance());
        for (int i = 0; i < 2500; i++)
            vecCol.add(DenseVector.random(3, rand));

        for (VectorCollection<Vec> factory : collectionFactories) {
            VectorCollection<Vec> collection0 = factory.clone();
            collection0.build(vecCol, new EuclideanDistance());
            VectorCollection<Vec> collection1 = factory.clone();
            collection1.build(true, vecCol, new EuclideanDistance());

            collection0 = collection0.clone();
            collection1 = collection1.clone();

            for (int iters = 0; iters < 10; iters++)
                for (int neighbours : new int[] { 1, 2, 4, 10, 20 }) {
                    int randIndex = rand.nextInt(vecCol.size());

                    IntArrayList nn_true = new IntArrayList();
                    IntArrayList nn_test = new IntArrayList();

                    DoubleArrayList nd_true = new DoubleArrayList();
                    DoubleArrayList nd_test = new DoubleArrayList();

                    vecCol.search(vecCol.get(randIndex), neighbours, nn_true, nd_true);
                    collection0.search(vecCol.get(randIndex), neighbours, nn_test, nd_test);

                    int found = (int) IntStream.of(nn_test.elements()).limit(nn_test.size()).filter(nn_true::contains).count();

                    // Since DCI is approximate, allow for missing half
                    assertEquals(neighbours, found, neighbours / 2.0);

                    collection1.search(vecCol.get(randIndex), neighbours, nn_test, nd_test);

                    found = (int) IntStream.of(nn_test.elements()).limit(nn_test.size()).filter(nn_true::contains).count();

                    // Since DCI is approximate, allow for missing half
                    assertEquals(neighbours, found, neighbours / 2.0);

                }
        }

    }

//    @Test
    public void testSearch_Vec_int_incramental() {
        System.out.println("search");
        Random rand = new XORWOW(123);

        VectorArray<Vec> vecCol = new VectorArray<Vec>(new EuclideanDistance());
        for (int i = 0; i < 1000; i++)
            vecCol.add(DenseVector.random(3, rand));

        IncrementalCollection<Vec> collection0 = new VPTree<Vec>(new EuclideanDistance());
        for (Vec v : vecCol)
            collection0.insert(v);

        for (int iters = 0; iters < 10; iters++)
            for (int neighbours : new int[] { 1, 2, 5, 10, 20 }) {
                int randIndex = rand.nextInt(vecCol.size());

                List<? extends VecPaired<Vec, Double>> foundTrue = vecCol.search(vecCol.get(randIndex), neighbours);
                List<? extends VecPaired<Vec, Double>> foundTest0 = collection0.search(vecCol.get(randIndex), neighbours);

                assertEquals(collection0.getClass().getName() + " failed", foundTrue.size(), foundTest0.size());
                for (int i = 0; i < foundTrue.size(); i++) {
                    assertTrue(collection0.getClass().getName() + " failed " + (i + 1) + "'th / " + neighbours + " " + foundTrue.get(i).pNormDist(2, foundTest0.get(i)), foundTrue.get(i).equals(foundTest0.get(i), 1e-13));
                }
            }
    }
}
