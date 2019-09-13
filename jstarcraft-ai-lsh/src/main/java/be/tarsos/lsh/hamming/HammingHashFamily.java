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
