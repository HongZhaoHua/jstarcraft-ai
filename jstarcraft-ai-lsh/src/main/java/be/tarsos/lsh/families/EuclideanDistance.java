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

import be.tarsos.lsh.Vector;

/**
 * Calculates the
 * <a href="http://en.wikipedia.org/wiki/Euclidean_distance">Euclidean
 * distance</a> between two vectors. Sometimes this is also called the
 * L<sub>2</sub> distance.
 * 
 * @author Joren Six
 */
public class EuclideanDistance implements DistanceMeasure {

    /*
     * (non-Javadoc)
     * 
     * @see
     * be.hogent.tarsos.lsh.families.DistanceMeasure#distance(be.hogent.tarsos.lsh.
     * Vector, be.hogent.tarsos.lsh.Vector)
     */
    @Override
    public double distance(Vector one, Vector other) {
        double sum = 0.0;
        for (int d = 0; d < one.getDimensions(); d++) {
            double delta = one.get(d) - other.get(d);
            sum += delta * delta;
        }
        return Math.sqrt(sum);
    }
}
