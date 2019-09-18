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

package be.tarsos.lsh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import be.tarsos.lsh.families.DistanceComparator;
import be.tarsos.lsh.families.DistanceMeasure;
import be.tarsos.lsh.families.HashFamily;
import be.tarsos.lsh.util.FileUtils;

/**
 * Implements a Locality Sensitive Hash scheme.
 * 
 * @author Joren Six
 */
public class LSH {
    List<Vector> dataset;
    private Index index;
    private final HashFamily hashFamily;

    public LSH(List<Vector> dataset, HashFamily hashFamily) {
        this.dataset = dataset;
        this.hashFamily = hashFamily;
    }

    /**
     * Build an index by creating a new one and adding each vector.
     * 
     * @param numberOfHashes     The number of hashes to use in each hash table.
     * @param numberOfHashTables The number of hash tables to use.
     */
    public void buildIndex(int numberOfHashes, int numberOfHashTables) {
        // Do we want to deserialize or build a new index???
        // index = new Index(hashFamily, numberOfHashes, numberOfHashTables);
        // Deserialization can cause duplicates?
        // index = Index.deserialize(hashFamily, numberOfHashes, numberOfHashTables);
        index = new Index(hashFamily, numberOfHashes, numberOfHashTables);
        if (dataset != null) {
            for (Vector vector : dataset) {
                index.index(vector);
            }
            Index.serialize(index);
        }
    }

    /**
     * Benchmark the current LSH construction.
     * 
     * @param neighboursSize the expected size of the neighbourhood.
     * @param measure        The measure to use to check for correctness.
     */
    public void benchmark(int neighboursSize, DistanceMeasure measure) {
        long startTime = 0;
        double linearSearchTime = 0;
        double lshSearchTime = 0;
        int numbercorrect = 0;
        int falsePositives = 0;
        int truePositives = 0;
        int falseNegatives = 0;
        // int intersectionSize = 0;
        for (int i = 0; i < dataset.size(); i++) {
            Vector query = dataset.get(i);
            startTime = System.currentTimeMillis();
            List<Vector> lshResult = index.query(query, neighboursSize);
            lshSearchTime += System.currentTimeMillis() - startTime;

            startTime = System.currentTimeMillis();
            List<Vector> linearResult = linearSearch(dataset, query, neighboursSize, measure);
            linearSearchTime += System.currentTimeMillis() - startTime;

            Set<Vector> set = new HashSet<Vector>();
            set.addAll(lshResult);
            set.addAll(linearResult);
            // intersectionSize += set.size();
            // In the best case, LSH result and linear result contain the exact same
            // elements.
            // The number of false positives is the number of vectors that exceed the number
            // of linear results.
            falsePositives += set.size() - linearResult.size();
            // The number of true positives is Union of results - intersection.
            truePositives += lshResult.size() + linearResult.size() - set.size();
            // The number of false Negatives the number of vectors that exceed the number of
            // lsh results .
            falseNegatives += set.size() - lshResult.size();

            // result is only correct if all nearest neighbours are the same (rather
            // strict).
            boolean correct = true;
            for (int j = 0; j < Math.min(lshResult.size(), linearResult.size()); j++) {
                correct = correct && lshResult.get(j) == linearResult.get(j);
            }
            if (correct) {
                numbercorrect++;
            }
        }
        double numberOfqueries = dataset.size();
        double dataSetSize = dataset.size();
        double precision = truePositives / Double.valueOf(truePositives + falsePositives) * 100;
        double recall = truePositives / Double.valueOf(truePositives + falseNegatives) * 100;
        double percentageCorrect = numbercorrect / dataSetSize * 100;
        double percentageTouched = index.getTouched() / numberOfqueries / dataSetSize * 100;
        linearSearchTime /= 1000.0;
        lshSearchTime /= 1000.0;
        int hashes = index.getNumberOfHashes();
        int hashTables = index.getNumberOfHashTables();

        // System.out.printf("%10s%15s%10s%10s%10s%10s%10s%10s\n","#hashes","#hashTables","Correct","Touched","linear","lsh","Precision","Recall");
        System.out.printf("%10d%15d%9.2f%%%9.2f%%%9.4fs%9.4fs%9.2f%%%9.2f%%\n", hashes, hashTables, percentageCorrect, percentageTouched, linearSearchTime, lshSearchTime, precision, recall);
    }

    /**
     * Find the nearest neighbours for a query in the index.
     * 
     * @param query          The query vector.
     * @param neighboursSize The size of the neighbourhood. The returned list length
     *                       contains the maximum number of elements, or less. Zero
     *                       elements are possible.
     * @return A list of nearest neigbours, according to the index. The returned
     *         list length contains the maximum number of elements, or less. Zero
     *         elements are possible.
     */
    public List<Vector> query(final Vector query, int neighboursSize) {
        return index.query(query, neighboursSize);
    }

    /**
     * Search for the actual nearest neighbours for a query vector using an
     * exhaustive linear search. For each vector a priority queue is created, the
     * distance between the query and other vectors is used to sort the priority
     * queue. The closest k neighbours show up at the head of the priority queue.
     * 
     * @param dataset    The data set with a bunch of vectors.
     * @param query      The query vector.
     * @param resultSize The k nearest neighbours to find. Returns k vectors if the
     *                   data set size is larger than k.
     * @param measure    The distance measure used to sort the priority queue with.
     * @return The list of k nearest neighbours to the query vector, according to
     *         the given distance measure.
     */
    public static List<Vector> linearSearch(List<Vector> dataset, final Vector query, int resultSize, DistanceMeasure measure) {
        DistanceComparator dc = new DistanceComparator(query, measure);
        PriorityQueue<Vector> pq = new PriorityQueue<Vector>(dataset.size(), dc);
        pq.addAll(dataset);
        List<Vector> vectors = new ArrayList<Vector>();
        for (int i = 0; i < resultSize; i++) {
            vectors.add(pq.poll());
        }
        return vectors;
    }

    /**
     * Read a data set from a text file. The file has the following contents, with
     * identifier being an optional string identifying the vector and a list of N
     * coordinates (which should be doubles). This results in an N-dimensional
     * vector.
     * 
     * <pre>
     * [Identifier] coord1 coord2 ... coordN 
     * [Identifier] coord1 coord2 ... coordN
     * </pre>
     * 
     * For example a data set with two elements with 4 dimensions looks like this:
     * 
     * <pre>
     * Hans 12 24 18.5 -45.6 
     * Jane 13 19 -12.0 49.8
     * </pre>
     * 
     * 
     * @param file    The file to read.
     * @param maxSize The maximum number of elements in the data set (even if the
     *                file defines more points).
     * @return a list of vectors, the data set.
     */
    public static List<Vector> readDataset(String file, int maxSize) {
        List<Vector> ret = new ArrayList<Vector>();
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
        int dimensions = firstColumnIsKey ? data.get(0).length - 1 : data.get(0).length;
        int startIndex = firstColumnIsKey ? 1 : 0;
        for (String[] row : data) {
            Vector item = new Vector(dimensions);
            if (firstColumnIsKey) {
                item.setKey(row[0]);
            }
            for (int d = startIndex; d < row.length; d++) {
                float value = Float.parseFloat(row[d]);
                item.set(d - startIndex, value);
            }
            ret.add(item);
        }
        return ret;
    }

    static float determineRadius(List<Vector> dataset, DistanceMeasure measure, int timeout) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        float radius = 0f;
        DetermineRadiusTask drt = new DetermineRadiusTask(dataset, measure);
        Future<Float> future = executor.submit(drt);
        try {
            System.out.println("Determine radius..");
            radius = 0.9F * future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Determined radius: " + radius);
        } catch (TimeoutException e) {
            System.err.println("Terminated!");
            radius = 0.9F * drt.getRadius();
        } catch (InterruptedException e) {
            System.err.println("Execution interrupted!" + e.getMessage());
            radius = 0.9F * drt.getRadius();
        } catch (ExecutionException e) {
            radius = 0.9F * drt.getRadius();
        }
        executor.shutdownNow();
        return radius;
    }

    static class DetermineRadiusTask implements Callable<Float> {
        private float queriesDone = 0F;
        private float radiusSum = 0F;
        private final List<Vector> dataset;
        private final Random rand;
        private final DistanceMeasure measure;

        public DetermineRadiusTask(List<Vector> dataset, DistanceMeasure measure) {
            this.dataset = dataset;
            this.rand = new Random();
            this.measure = measure;
        }

        @Override
        public Float call() throws Exception {
            for (int i = 0; i < 30; i++) {
                Vector query = dataset.get(rand.nextInt(dataset.size()));
                List<Vector> result = linearSearch(dataset, query, 2, measure);
                // the first vector is the query self, the second the closest.
                radiusSum += measure.distance(query, result.get(1));
                queriesDone++;
            }
            return radiusSum / queriesDone;
        }

        public float getRadius() {
            return radiusSum / queriesDone;
        }
    }

    public static void main(String args[]) {
        CommandLineInterface cli = new CommandLineInterface(args);
        cli.parseArguments();
        cli.startApplication();
    }
}
