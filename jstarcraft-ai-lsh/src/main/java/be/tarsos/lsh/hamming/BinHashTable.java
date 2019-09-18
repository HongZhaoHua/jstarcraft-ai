package be.tarsos.lsh.hamming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * An index contains one or more locality sensitive hash tables. These hash
 * tables contain the mapping between a combination of a number of hashes
 * (encoded using an integer) and a list of possible nearest neighbours.
 * 
 * @author Joren Six
 */
class BinHashTable implements Serializable {

    private static final long serialVersionUID = -5410017645908038641L;

    /**
     * Contains the mapping between a combination of a number of hashes (encoded
     * using an integer) and a list of possible nearest neighbours
     */
    private Int2ObjectOpenHashMap<long[][]> hashTable;
    // private THashMap<int,> hashTable;
    private HammingHash[] hashFunctions;
    private HammingHashFamily family;

    /**
     * Initialize a new hash table, it needs a hash family and a number of hash
     * functions that should be used.
     * 
     * @param numberOfHashes The number of hash functions that should be used.
     * @param family         The hash function family knows how to create new hash
     *                       functions, and is used therefore.
     */
    public BinHashTable(int numberOfHashes, HammingHashFamily family) {
        hashTable = new Int2ObjectOpenHashMap<long[][]>();
        this.hashFunctions = new HammingHash[numberOfHashes];
        for (int i = 0; i < numberOfHashes; i++) {
            hashFunctions[i] = family.createHashFunction();
        }
        this.family = family;
    }

    /**
     * Query the hash table for a vector. It calculates the hash for the vector, and
     * does a lookup in the hash table. If no candidates are found, an empty list is
     * returned, otherwise, the list of candidates is returned.
     * 
     * @param query The query vector.
     * @return Does a lookup in the table for a query using its hash. If no
     *         candidates are found, an empty list is returned, otherwise, the list
     *         of candidates is returned.
     */
    public List<BinVector> query(BinVector query) {
        int combinedHash = hash(query);
        List<BinVector> neighbors = new ArrayList<BinVector>();
        if (hashTable.containsKey(combinedHash)) {
            long[][] list = hashTable.get(combinedHash);
            for (int i = 0; i < list.length; i += 4) {
                int id = (int) list[i][0];
                int offset = (int) list[i][1];
                long[] bitsetData = { list[i][2], list[i][3] };
                BitSet bitSet = BitSet.valueOf(bitsetData);
                neighbors.add(new BinVector(id, offset, bitSet));
            }
        }
        return neighbors;
    }

    /**
     * Add a vector to the index.
     * 
     * @param vector
     */
    public void add(BinVector vector) {
        int combinedHash = hash(vector);
        final long[][] newList;
        long[] data = vector.toLongArray();
        if (hashTable.containsKey(combinedHash)) {
            long[][] oldList = hashTable.get(combinedHash);
            newList = new long[oldList.length + 1][0];
            for (int i = 0; i < newList.length - 1; i++) {
                newList[i] = oldList[i];
            }
            newList[newList.length - 1] = data;
        } else {
            newList = new long[1][0];
            newList[0] = data;
        }
        hashTable.put(combinedHash, newList);
    }

    /**
     * Calculate the combined hash for a vector.
     * 
     * @param vector The vector to calculate the combined hash for.
     * @return An integer representing a combined hash.
     */
    private Integer hash(BinVector vector) {
        int hashes[] = new int[hashFunctions.length];
        for (int i = 0; i < hashFunctions.length; i++) {
            hashes[i] = hashFunctions[i].hash(vector);
        }
        Integer combinedHash = family.combine(hashes);
        return combinedHash;
    }

    /**
     * Return the number of hash functions used in the hash table.
     * 
     * @return The number of hash functions used in the hash table.
     */
    public int getNumberOfHashes() {
        return hashFunctions.length;
    }
}
