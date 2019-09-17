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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import be.tarsos.lsh.Vector;
import be.tarsos.lsh.families.CityBlockDistance;

public class CityBlockDistanceTestCase {

    @Test
    public void testDistance() {
        Vector v = new Vector(3);
        v.set(0, 1);
        v.set(1, 2);
        v.set(2, 3);

        Vector other = new Vector(3);
        other.set(0, 3);
        other.set(1, 5);
        other.set(2, 7);

        CityBlockDistance distance = new CityBlockDistance();
        double distanceValue = distance.distance(v, other);
        assertEquals("Expected about 9.0", 9.0, distanceValue, 0.00001);
        assertEquals("d(one,two) = d(two,one)", distance.distance(other, v), distanceValue, 0.00001);
        assertEquals("d(one,one) = 0", distance.distance(other, other), 0, 0.00001);

        // move other closer
        other.set(0, 1);
        other.set(1, 2);
        other.set(2, 3);
        double newDistanceValue = distance.distance(v, other);
        assertTrue("Expected a smaller distance, since vectors are closer", newDistanceValue < distanceValue);
    }

}
