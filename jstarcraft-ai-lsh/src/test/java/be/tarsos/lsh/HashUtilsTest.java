/*
*      _______                       _        ____ _     _
*     |__   __|                     | |     / ____| |   | |
*        | | __ _ _ __ ___  ___  ___| |    | (___ | |___| |
*        | |/ _` | '__/ __|/ _ \/ __| |     \___ \|  ___  |
*        | | (_| | |  \__ \ (_) \__ \ |____ ____) | |   | |
*        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|   |_|
*                                                         
* -----------------------------------------------------------
*
*  TarsosLSH is developed by Joren Six.
*  
* -----------------------------------------------------------
*
*  Info    : http://0110.be/tag/TarsosLSH
*  Github  : https://github.com/JorenSix/TarsosLSH
*  Releases: http://0110.be/releases/TarsosLSH/
* 
*/

package be.tarsos.lsh;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.math.BigInteger;

import org.junit.Test;

import be.tarsos.lsh.experimental.HashUtils;

public class HashUtilsTest {

    @Test
    public void testPairingfunction() {
        long a = 3;
        long b = 7;

        // calculated by hand with http://matics.net
        long c = HashUtils.pair(a, b);
        assertEquals("Should be 62", 62, c);

        // calculated by hand with http://matics.net
        long d = HashUtils.pair(b, a);
        assertEquals("Should be 58", 58, d);

        // just to make sure
        assertNotSame("pair(a,b) should not be equal to pair(b,a).", c, d);

        // test unpairing
        long[] pair = HashUtils.unPair(c, 2);
        assertEquals("First element should be 3", 3, pair[0]);
        assertEquals("Second element should be 7", 7, pair[1]);

        // test pairing with 4 elements
        long[] original = { 3, 7, 8, 9 };
        c = HashUtils.pair(original);
        long[] actual = HashUtils.unPair(c, 4);
        assertArrayEquals(original, actual);
    }

    @Test
    public void testBigPairingfunction() {
        long a = 3;
        long b = 7;

        // calculated by hand with http://matics.net
        BigInteger c = HashUtils.bigPair(BigInteger.valueOf(a), BigInteger.valueOf(b));
        assertEquals("Should be 62", 62, c.intValue());

        // test unpairing
        BigInteger[] pair = HashUtils.bigUnPair(c);
        assertEquals("First element should be 3", 3, pair[0].intValue());
        assertEquals("Second element should be 7", 7, pair[1].intValue());

        /*
         * BigInteger d = BigInteger.valueOf(Integer.MAX_VALUE-1000); BigInteger e =
         * BigInteger.valueOf(Integer.MAX_VALUE-5000); BigInteger f =
         * HashUtils.bigPair(d,e); BigInteger[] bigPair = HashUtils.bigUnPair(f);
         * assertEquals("First element should be 3", Integer.MAX_VALUE - 1000 ,
         * bigPair[0].intValue()); assertEquals("Second element should be 7",
         * Integer.MAX_VALUE- 5000 , bigPair[1].intValue());
         */

    }
}
