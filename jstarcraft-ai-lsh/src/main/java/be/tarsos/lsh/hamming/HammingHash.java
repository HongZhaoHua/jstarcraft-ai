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

package be.tarsos.lsh.hamming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

public class HammingHash {
    /**
     * 
     */
    // private static final long serialVersionUID = -625398900309516287L;

    final private int[] projection;

    private static List<Integer> list = null;

    private static List<Integer> getList(int nBitsTotal) {
        if (list == null || list.size() != nBitsTotal) {
            if (list == null) {
                list = new ArrayList<>();
                for (int i = 0; i < nBitsTotal; i++) {
                    list.add(i);
                }
            }
        }
        return list;
    }

    public HammingHash(int nBitsInTotal, int nBitsToUseForProjection) {
        this.projection = new int[nBitsToUseForProjection];
        // To get a random projection that uses different! bits
        // use a list that contains all indexes, shuffle it and
        // take the top n. The list is cached (static) for performance reasons.
        List<Integer> indexList = getList(nBitsInTotal);
        Collections.shuffle(indexList, BinLSH.random);
        for (int i = 0; i < nBitsToUseForProjection; i++) {
            projection[i] = indexList.get(i);
        }
    }

    public int hash(BinVector vector) {
        if (projection.length <= 64) {
            long projectionLong = 0;
            for (int i = 0; i < projection.length; i++) {
                if (vector.get(projection[i])) {
                    long mask = 1;
                    mask = mask << projection[i];
                    projectionLong = (projectionLong | mask);
                }
            }
            return Long.hashCode(projectionLong);
        } else {
            BitSet projectionBitSet = new BitSet(projection.length);
            for (int i = 0; i < projection.length; i++) {
                projectionBitSet.set(i, vector.get(projection[i]));
            }
            return Arrays.hashCode(projectionBitSet.toLongArray());
        }
    }

}
