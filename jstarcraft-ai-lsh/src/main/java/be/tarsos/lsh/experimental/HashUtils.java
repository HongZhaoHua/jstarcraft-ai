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

package be.tarsos.lsh.experimental;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Experimental reversable pairing functions, which could be usefull for
 * hashing.
 * 
 * @author Joren Six
 */
public class HashUtils {

    private HashUtils() {
    }

    /**
     * <p>
     * These functions are incredibly useful for anyone wanting to set up
     * enumeration schemes, and in general give a single number for every coordinate
     * in an infinite space.
     * 
     * They follow as:
     * 
     * <code>pair[x_, y_] := (x^2 + x + 2x y + 3y + y^2)/2</code>
     * 
     * this number can be transformed back into the original two numbers by:
     * 
     * <code>unpair[z_] := With[{i = Floor[-1/2 + (Sqrt[1 + 8 z])/2]}, {1/2 i (3 + i) - z, -1/2 i (1 + i) + z}]</code>
     * 
     * This can be used to enumerate all state configurations of multiple infinitely
     * growing paramaters. You can think of it as a way of growing towers so that
     * every configuration of tower height is visited and has a unique number for
     * it.
     * 
     * this function is only designed for two parameters but one can imagine a kind
     * folding that pairs many parameters together by successively combining your
     * set of numbers. Such a function can be constructed. One implementation of
     * this is as follows:
     * 
     * <code>Combine[sequence__] := Fold[pair[#2, #1] &, First@{sequence}, Rest@{sequence}]</code>
     * 
     * To uncombine a number into a set of other numbers, the number of parameters
     * must be specified. Here is the code for that:
     * 
     * <code>Uncombine[number_, numParams_] := Reverse[Nest[Flatten[{Most@Flatten@{#}, unpair[Last[Flatten@{#}]]}] &, Last@{number}, numParams - 1]]</code>
     * 
     * @param paired
     * @param components
     * @return return the elements.
     */
    public static long[] unPair(long paired, int components) {
        long[] results = new long[components];
        if (components == 1) {
            results[0] = paired;
        } else {
            long[] intermediatePair = actuallyUnPair(paired);
            results[results.length - 1] = intermediatePair[1];
            for (int i = results.length - 2; i > 0; i--) {
                intermediatePair = actuallyUnPair(intermediatePair[0]);
                results[i] = intermediatePair[1];
            }
            results[0] = intermediatePair[0];
        }
        return results;
    }

    private static long actuallyPair(long a, long b) {
        // http://stackoverflow.com/questions/919612/mapping-two-integers-to-one-in-a-unique-and-deterministic-way
        // http://forum.wolframscience.com/showthread.php?s=&threadid=922
        // http://www.lsi.upc.edu/~alvarez/calculabilitat/enumerabilitat.pdf
        return (a * a + a + 2 * a * b + 3 * b + b * b) / 2;
    }

    public static BigInteger bigPair(BigInteger a, BigInteger b) {
        BigInteger aSquared = a.multiply(a);
        BigInteger twoAB = a.multiply(BigInteger.valueOf(2)).multiply(b);
        BigInteger threeB = b.multiply(BigInteger.valueOf(3));
        BigInteger bSquared = b.multiply(b);
        BigInteger sum = aSquared.add(a).add(twoAB).add(threeB).add(bSquared);
        return sum.divide(BigInteger.valueOf(2));
    }

    public static BigInteger[] bigUnPair(BigInteger c) {
        double i = Math.floor(-0.5 + Math.sqrt(1 + 8 * c.doubleValue()) / 2.0);
        BigInteger[] pair = new BigInteger[2];
        pair[0] = BigDecimal.valueOf(0.5 * i * (3 + i) - c.doubleValue()).toBigInteger();
        pair[1] = BigDecimal.valueOf(-0.5 * i * (1 + i) + c.doubleValue()).toBigInteger();
        return pair;
    }

    private static long[] actuallyUnPair(long c) {
        long[] pair = new long[2];
        // cast to integer floors the number.
        long i = (long) (-0.5 + Math.sqrt(1 + 8 * c) / 2.0);

        pair[0] = (long) (0.5 * i * (3 + i) - c);
        pair[1] = (long) (-0.5 * i * (1 + i) + c);

        return pair;
    }

    public static long pair(long... input) {
        long paired;
        if (input.length == 1) {
            paired = input[0];
        } else {
            long intermediate = input[0];
            for (int i = 1; i < input.length; i++) {
                intermediate = actuallyPair(intermediate, input[i]);
            }
            paired = intermediate;
        }
        return paired;
    }

}
