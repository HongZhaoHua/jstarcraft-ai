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

package be.tarsos.lsh.test;

import static org.junit.Assert.*;

import org.junit.Test;

import be.tarsos.lsh.Vector;

public class VectorTests {

    @Test
    public void testDotProduct() {
        Vector one = new Vector(3);
        Vector two = new Vector(3);
        one.set(0, 1);
        two.set(0, 7);
        one.set(1, 1);
        two.set(1, -7);
        one.set(2, 0);
        two.set(2, 1);
        assertEquals(0, one.dot(two), 0.000000000001);
        assertEquals(two.dot(one), one.dot(two), 0.000000000001);

        one.set(2, 3);
        assertEquals(3, one.dot(two), 0.000000000001);
    }

}
