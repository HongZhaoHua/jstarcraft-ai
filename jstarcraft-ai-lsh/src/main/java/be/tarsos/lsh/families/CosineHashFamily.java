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

package be.tarsos.lsh.families;

import java.util.Arrays;

public class CosineHashFamily implements HashFamily {

    /**
     * 
     */
    private static final long serialVersionUID = 7678152513757669089L;
    private final int dimensions;

    public CosineHashFamily(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public HashFunction createHashFunction() {
        return new CosineHash(dimensions);
    }

    @Override
    public String combine(int[] hashes) {
        // Treat the hashes as a series of bits.
        // They are either zero or one, the index
        // represents the value.
        // int result = 0;
        // factor holds the power of two.
        // int factor = 1;
        // for(int i = 0 ; i < hashes.length ; i++){
        // result += hashes[i] == 0 ? 0 : factor;
        // factor *= 2;
        // }
        // return result;
        return Arrays.toString(hashes);
    }

    @Override
    public DistanceMeasure createDistanceMeasure() {
        return new CosineDistance();
    }
}
