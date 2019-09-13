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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BinIndex {

    private List<BinHashTable> hashTables;

    public BinIndex(int numberOfHashes, int numberOfHashTables, int nbits, int nbitsForProjection) {
        hashTables = new ArrayList<BinHashTable>();
        for (int i = 0; i < numberOfHashTables; i++) {
            hashTables.add(new BinHashTable(numberOfHashes, new HammingHashFamily(nbits, nbitsForProjection)));
        }
        evaluated = 0;
    }

    /**
     * Add a vector to the current index. The hashes are calculated with the current
     * hash family and added in the right place.
     * 
     * @param vector The vector to add.
     */
    public void index(BinVector vector) {
        for (BinHashTable table : hashTables) {
            table.add(vector);
        }
    }

    int evaluated;

    public List<BinVector> query(final BinVector query, int maxSize) {
        Set<BinVector> candidateSet = new HashSet<BinVector>();
        for (BinHashTable table : hashTables) {
            List<BinVector> v = table.query(query);
            candidateSet.addAll(v);
        }
        List<BinVector> candidates = new ArrayList<BinVector>(candidateSet);
        evaluated += candidates.size();

        Collections.sort(candidates, new HammingDistanceComparator());
        if (candidates.size() > maxSize) {
            candidates = candidates.subList(0, maxSize);
        }
        return candidates;
    }
}
