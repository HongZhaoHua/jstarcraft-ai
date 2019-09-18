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

import static org.junit.Assert.*;

import org.junit.Test;

import be.tarsos.lsh.Vector;
import be.tarsos.lsh.families.CosineDistance;

public class CosineDistanceTestCase {

    @Test
    public void testDistance() {
        Vector v = new Vector("left", new float[] { 1F, 2F, 3F });
        Vector other = new Vector("left", new float[] { 3F, 5F, 7F });

        CosineDistance distance = new CosineDistance();
        float distanceValue = distance.distance(v, other);
        assertEquals(0.0025850534F, distanceValue, 0F);
        assertEquals(distance.distance(other, v), distanceValue, 0F);
        assertEquals(distance.distance(other, other), 0F, 0F);
    }

}
