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
 * A distance measure defines how distance is calculated, measured as it were,
 * between two vectors. Each hash family has a corresponding distance measure
 * which is abstracted using this interface.
 * 
 * @author Joren Six
 */
public interface DistanceMeasure {

    /**
     * Calculate the distance between two vectors. From one to two.
     * 
     * @param one   The first vector.
     * @param other The other vector
     * @return A value representing the distance between two vectors.
     */
    double distance(Vector one, Vector other);
}
