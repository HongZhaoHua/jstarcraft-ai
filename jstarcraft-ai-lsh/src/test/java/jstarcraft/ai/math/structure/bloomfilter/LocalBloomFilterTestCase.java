/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jstarcraft.ai.math.structure.bloomfilter;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.core.common.hash.HashUtility;

public class LocalBloomFilterTestCase {

    static Random random = new Random();

    @Test
    public void testOptimal() {
        int elments = 1000;
        float probability = 0.001F;
        int bits = LocalBloomFilter.optimalBits(elments, probability);
        int hashs = LocalBloomFilter.optimalHashs(bits, elments);
        Assert.assertEquals(997, LocalBloomFilter.optimalElements(bits, hashs));
        Assert.assertEquals(9.998266E-4F, LocalBloomFilter.optimalProbability(bits, elments, hashs), 0F);
    }

    protected BloomFilter getBloomFilter(int elments, float probability) {
        int bits = LocalBloomFilter.optimalBits(elments, probability);
        int hashs = LocalBloomFilter.optimalHashs(bits, elments);
        StringHashFamily hashFamily = (random) -> {
            int seed = random.nextInt();
            return (data) -> {
                return HashUtility.murmur2StringHash32(seed, data);
            };
        };
        BloomFilter bloomFilter = new IntegerBloomFilter(bits, hashFamily, hashs, random);
        return bloomFilter;
    }

    @Test
    public void testBloomFilter() {
        int elments = 1000;
        float probability = 0.001F;
        BloomFilter bloomFilter = getBloomFilter(elments, probability);
        int times = 0;
        for (int index = 0; index < elments; index++) {
            String data = String.valueOf(index);
            if (bloomFilter.get(data)) {
                times++;
            }
            bloomFilter.put(data);
            Assert.assertTrue(bloomFilter.get(data));
        }
        Assert.assertTrue(times < elments * probability);
    }

}