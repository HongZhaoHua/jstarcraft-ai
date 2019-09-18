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

import java.util.Comparator;

import be.tarsos.lsh.Vector;

/**
 * This comparator can be used to sort candidate neighbours according to their
 * distance to a query vector. Either for linear search or to sort the LSH
 * candidates found in colliding hash bins.
 * 
 * @author Joren Six
 */
public class DistanceComparator implements Comparator<Vector> {

    private final Vector query;
    private final DistanceMeasure distanceMeasure;

    /**
     * 
     * @param query           The query vector.
     * @param distanceMeasure The distance vector to use.
     */
    public DistanceComparator(Vector query, DistanceMeasure distanceMeasure) {
        this.query = query;
        this.distanceMeasure = distanceMeasure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Vector one, Vector other) {
        float oneDistance = distanceMeasure.distance(query, one);
        float otherDistance = distanceMeasure.distance(query, other);
        return Float.compare(oneDistance, otherDistance);
    }
}
