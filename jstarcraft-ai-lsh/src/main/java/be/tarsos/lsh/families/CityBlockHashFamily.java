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

public class CityBlockHashFamily implements HashFamily {

    /**
     * 
     */
    private static final long serialVersionUID = -8926838846356323484L;
    private int dimensions;
    private int w;

    public CityBlockHashFamily(int w, int dimensions) {
        this.dimensions = dimensions;
        this.w = w;
    }

    @Override
    public HashFunction createHashFunction() {
        return new CityBlockHash(dimensions, w);
    }

    @Override
    public String combine(int[] hashes) {
        // return Arrays.hashCode(hashes);
        return Arrays.toString(hashes);
    }

    @Override
    public DistanceMeasure createDistanceMeasure() {
        return new CityBlockDistance();
    }

}
