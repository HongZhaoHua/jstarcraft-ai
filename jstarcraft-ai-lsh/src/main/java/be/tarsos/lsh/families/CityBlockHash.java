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
import java.util.Random;

import be.tarsos.lsh.Vector;

public class CityBlockHash implements HashFunction {
    /**
     * 
     */
    private static final long serialVersionUID = -635398900309516287L;
    private int w;
    private Vector randomPartition;

    public CityBlockHash(Random rand, int dimensions, int width) {
        this.w = width;

        randomPartition = new Vector(dimensions);
        for (int d = 0; d < dimensions; d++) {
            // mean 0
            // standard deviation 1.0
            double val = rand.nextDouble() * w;
            randomPartition.set(d, val);
        }
    }

    public int hash(Vector vector) {
        int hash[] = new int[randomPartition.getDimensions()];
        for (int d = 0; d < randomPartition.getDimensions(); d++) {
            hash[d] = (int) Math.floor((vector.get(d) - randomPartition.get(d)) / Double.valueOf(w));
        }
        return Arrays.hashCode(hash);
    }

    public String toString() {
        return String.format("w:%d\nrandomPartition:%s", w, randomPartition);
    }
}
