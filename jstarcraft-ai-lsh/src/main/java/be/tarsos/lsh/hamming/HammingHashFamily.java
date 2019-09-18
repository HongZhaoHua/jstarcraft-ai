package be.tarsos.lsh.hamming;

import java.util.Arrays;

public class HammingHashFamily {

    /**
     * 
     */
    // private static final long serialVersionUID = -8926838846356323484L;
    private int nbits;
    private int nbitsProjectionVector;

    public HammingHashFamily(int nbits, int nbitsProjectionVector) {
        this.nbits = nbits;
        this.nbitsProjectionVector = nbitsProjectionVector;
    }

    public HammingHash createHashFunction() {
        return new HammingHash(nbits, nbitsProjectionVector);
    }

    public int combine(int[] hashes) {
        return Arrays.hashCode(hashes);
    }
}
