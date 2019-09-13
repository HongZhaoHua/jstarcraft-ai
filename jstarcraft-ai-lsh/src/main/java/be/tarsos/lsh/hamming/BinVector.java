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
package be.tarsos.lsh.hamming;

import java.util.BitSet;

public class BinVector {

    /**
     * 
     * Values are stored here.
     */
    private final BitSet bitSet;

    /**
     * An optional key, identifier for the vector.
     */
    private final String key;

    private final int identifier;
    private final int offset;

    private long[] longRepresentation = null;

    /**
     * Creates a new vector with the requested number of dimensions.
     * 
     * @param bytes The number of bytes.
     */
    public BinVector(int bits) {
        this(null, new BitSet(bits));
    }

    public BinVector(String key, BitSet bitSet) {
        this.bitSet = bitSet;
        this.key = key;
        this.identifier = 0;
        this.offset = 0;
    }

    public BinVector(int id, int offset, BitSet bitSet) {
        this.bitSet = bitSet;
        this.key = null;
        this.identifier = id;
        this.offset = offset;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("key:").append(key).append(",\n");
        sb.append("values:[");
        for (int d = 0; d < bitSet.length() - 1; d++) {
            sb.append(bitSet.get(d) ? "1" : "0").append(",");
        }
        sb.append(bitSet.get(bitSet.length() - 1) ? "1" : "0").append("]");
        return sb.toString();
    }

    public int getNumberOfBits() {
        return bitSet.length();
    }

    public int hammingDistance(BinVector other) {
        // clone the bit set of this object
        BitSet xored = (BitSet) bitSet.clone();

        // xor (modify) the bit set
        xored.xor(other.bitSet);
        // return the number of 1's
        return xored.cardinality();
    }

    public void set(int bitIndex, boolean value) {
        bitSet.set(bitIndex, value);
    }

    public boolean get(int bitIndex) {
        return bitSet.get(bitIndex);
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    public String getKey() {
        return key;
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getOffset() {
        return offset;
    }

    public long[] toLongArray() {

        if (longRepresentation == null) {
            long[] data = bitSet.toLongArray();

            long leastSignificantBits = 0;
            long mostSignificantBits = 0;

            if (data.length == 2) {
                mostSignificantBits = data[0];
                leastSignificantBits = data[1];
            } else if (data.length == 1) {
                mostSignificantBits = data[0];
            }

            longRepresentation = new long[4];
            longRepresentation[0] = identifier;
            longRepresentation[1] = offset;
            longRepresentation[2] = mostSignificantBits;
            longRepresentation[3] = leastSignificantBits;
        }
        return longRepresentation;
    }
}
