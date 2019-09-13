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
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import be.tarsos.lsh.util.FileUtils;

public class BinLSH {

    public static Random random;
    private final List<BinVector> dataset;
    private BinIndex index;

    public BinLSH(List<BinVector> dataset) {
        this.dataset = dataset;
    }

    public void buildIndex(int numberOfHashes, int numberOfHashTables, int nbitsForProjection) {
        this.buildIndex(numberOfHashes, numberOfHashTables, nbitsForProjection, new Random().nextLong());
    }

    public void buildIndex(int numberOfHashes, int numberOfHashTables, int nbitsForProjection, long seed) {
        BinLSH.random = new Random(seed);
        // Do we want to deserialize or build a new index???
        // index = new Index(hashFamily, numberOfHashes, numberOfHashTables);
        // Deserialization can cause duplicates?
        int nbits = dataset.get(0).getNumberOfBits();
        index = new BinIndex(numberOfHashes, numberOfHashTables, nbits, nbitsForProjection);
        if (dataset != null) {
            for (BinVector vector : dataset) {
                index.index(vector);
            }
        }
    }

    public static void main(String... strings) {
        BinCommandLineInterface cli = new BinCommandLineInterface(strings);
        cli.parseArguments();
        cli.startApplication();
    }

    public static List<BinVector> readDataset(String file, int maxSize) {
        List<BinVector> ret = new ArrayList<BinVector>();
        List<String[]> data = FileUtils.readCSVFile(file, " ", -1);

        if (data.size() > maxSize) {
            data = data.subList(0, maxSize);
        }
        boolean firstColumnIsKey = false;
        try {
            Double.parseDouble(data.get(0)[0]);
        } catch (Exception e) {
            firstColumnIsKey = true;
        }
        int nbits = firstColumnIsKey ? data.get(0).length - 1 : data.get(0).length;
        int startIndex = firstColumnIsKey ? 1 : 0;
        for (String[] row : data) {
            BitSet s = new BitSet(nbits);
            String key = null;
            if (firstColumnIsKey) {
                key = row[0];
            }
            for (int d = startIndex; d < row.length; d++) {
                int value = Integer.parseInt(row[d]);
                s.set(d - startIndex, value == 1);
            }
            ret.add(new BinVector(key, s));
        }
        return ret;
    }

    public List<BinVector> query(BinVector query, int numberOfNeighbours) {
        return index.query(query, numberOfNeighbours);
    }

}
