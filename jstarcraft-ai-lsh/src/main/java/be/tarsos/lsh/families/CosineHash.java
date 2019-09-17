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

import java.util.Random;

import be.tarsos.lsh.Vector;

public class CosineHash implements HashFunction {
    /**
     * 
     */
    private static final long serialVersionUID = 778951747630668248L;
    final Vector randomProjection;

    public CosineHash(Random rand, int dimensions) {
        randomProjection = new Vector(dimensions);
        for (int d = 0; d < dimensions; d++) {
            // mean 0
            // standard deviation 1.0
            double val = rand.nextGaussian();
            randomProjection.set(d, val);
        }
    }

    @Override
    public int hash(Vector vector) {
        // calculate the dot product.
        double result = vector.dot(randomProjection);
        // returns a 'bit' encoded as an integer.
        // 1 when positive or zero, 0 otherwise.
        return result > 0 ? 1 : 0;
    }
}
