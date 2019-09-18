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

import java.util.List;
import java.util.Random;

import be.tarsos.lsh.families.CityBlockDistance;
import be.tarsos.lsh.families.CityBlockHashFamily;
import be.tarsos.lsh.families.CosineHashFamily;
import be.tarsos.lsh.families.DistanceMeasure;
import be.tarsos.lsh.families.EuclideanDistance;
import be.tarsos.lsh.families.EuclidianHashFamily;
import be.tarsos.lsh.families.HashFamily;
import be.tarsos.lsh.util.TestUtils;

/**
 * A class implementing a command line interface for the LSH program.
 * 
 * @author Joren Six
 */
public class CommandLineInterface {

    private String[] arguments;

    private int numberOfHashTables;
    private int numberOfHashes;
    private int numberOfNeighbours;
    private float radius;

    private List<Vector> dataset;
    private List<Vector> queries;

    private int dimensions;
    private DistanceMeasure measure;
    private int timeout = 40; // seconds timeout for radius search.
    private HashFamily family;

    private boolean benchmark;
    private boolean printHelp;
    private boolean linear;

    public CommandLineInterface(String... args) {
        this.arguments = args;
    }

    /**
     * Parses the arguments currently stored in the argument list.
     */
    public void parseArguments() {
        Random rand = new Random(0L);
        numberOfHashTables = getIntegerValue("-t", 4);
        numberOfHashes = getIntegerValue("-h", 4);
        numberOfNeighbours = getIntegerValue("-n", 4);
        String hashFamilyType = getValue("-f", "l2");

        linear = hasOption("-l");

        radius = getFloatValue("-r", 0F);
        printHelp = hasOption("--help") || arguments.length == 0;
        benchmark = hasOption("-b");
        String datasetFile = getValue("-d", null);
        String queryFile = getValue("-q", null);
        if (benchmark) {
            dimensions = 256;
            if (radius == 0) {
                radius = 10;
            }
            dataset = TestUtils.generate(rand, dimensions, 100, 512);
            TestUtils.addNeighbours(rand, dataset, 4, radius);
        }
        if (datasetFile != null) {
            dataset = LSH.readDataset(datasetFile, Integer.MAX_VALUE);
            dimensions = dataset.get(0).getDimensions();
        }
        if (queryFile != null) {
            queries = LSH.readDataset(queryFile, Integer.MAX_VALUE);
            dimensions = queries.get(0).getDimensions();
        }
        if (radius == 0 && hashFamilyType.equalsIgnoreCase("l1")) {
            measure = new CityBlockDistance();
            radius = LSH.determineRadius(rand, dataset, measure, timeout);
        } else if (radius == 0 && hashFamilyType.equalsIgnoreCase("l2")) {
            measure = new EuclideanDistance();
            radius = LSH.determineRadius(rand, dataset, measure, timeout);
        }
        family = getHashFamily(radius, hashFamilyType, dimensions);
    }

    public void startApplication() {
        if (printHelp) {
            printHelp();
        } else if (benchmark) {
            printPrefix();
            printLine();
            startBenchmark();
        } else {
            printPrefix();
            printLine();
            startLSH();
        }
    }

    public void startBenchmark() {
        Random rand = new Random(0L);
        System.out.println("Starting TarsosLSH benchmark with " + dataset.size() + " random vectors");
        System.out.println("   Four close neighbours have been added to 100 vectors (100+4x100=500).");
        System.out.println("   The results of LSH are compared with a linear search.");
        System.out.println("   The first four results of LSH and linear are compared.");

        // determine the radius for hash bins automatically
        System.out.println("Radius for Euclidean distance.");
        int radiusEuclidean = (int) LSH.determineRadius(rand, dataset, new EuclideanDistance(), 20);
        System.out.println("\nRadius for City block distance.");
        int radiusCityBlock = (int) LSH.determineRadius(rand, dataset, new CityBlockDistance(), 20);

        HashFamily[] families = { new EuclidianHashFamily(radiusEuclidean, dimensions), new CityBlockHashFamily(radiusCityBlock, dimensions), new CosineHashFamily(dimensions) };

        for (HashFamily family : families) {
            int[] numberOfHashes = { 2, 4, 8 };
            if (family instanceof CosineHashFamily) {
                numberOfHashes[0] *= 8;
                numberOfHashes[1] *= 8;
                numberOfHashes[2] *= 8;
            }

            int[] numberOfHashTables = { 2, 4, 8, 16 };

            LSH lsh = new LSH(dataset, family);
            System.out.println("\n--" + family.getClass().getName());
            System.out.printf("%10s%15s%10s%10s%10s%10s%10s%10s\n", "#hashes", "#hashTables", "Correct", "Touched", "linear", "LSH", "Precision", "Recall");
            for (int i = 0; i < numberOfHashes.length; i++) {
                for (int j = 0; j < numberOfHashTables.length; j++) {
                    lsh.buildIndex(rand, numberOfHashes[i], numberOfHashTables[j]);
                    lsh.benchmark(4, family.createDistanceMeasure());
                }
            }
        }
    }

    private void startLSH() {
        if (linear) {
            if (queries != null) {
                for (Vector query : queries) {
                    List<Vector> neighbours = LSH.linearSearch(dataset, query, numberOfNeighbours, measure);
                    System.out.print(query.getKey() + ";");
                    double sum = 0.0;
                    for (Vector neighbour : neighbours) {
                        sum += measure.distance(query, neighbour);
                        System.out.print(neighbour.getKey() + ";");
                    }
                    System.out.print(sum + ";");
                    System.out.print("\n");
                }
            }
        } else {
            Random rand = new Random(0L);
            LSH lsh = new LSH(dataset, family);
            lsh.buildIndex(rand, numberOfHashes, numberOfHashTables);
            if (queries != null) {
                for (Vector query : queries) {
                    List<Vector> neighbours = lsh.query(query, numberOfNeighbours);
                    System.out.print(query.getKey() + ";");
                    for (Vector neighbour : neighbours) {
                        System.out.print(neighbour.getKey() + ";");
                    }
                    System.out.print("\n");
                }
            }
        }
    }

    private HashFamily getHashFamily(double radius, String hashFamilyType, int dimensions) {
        HashFamily family = null;
        if (hashFamilyType.equalsIgnoreCase("cos")) {
            family = new CosineHashFamily(dimensions);
        } else if (hashFamilyType.equalsIgnoreCase("l1")) {
            int w = (int) (10 * radius);
            w = w == 0 ? 1 : w;
            family = new CityBlockHashFamily(w, dimensions);
        } else if (hashFamilyType.equalsIgnoreCase("l2")) {
            int w = (int) (10 * radius);
            w = w == 0 ? 1 : w;
            family = new EuclidianHashFamily(w, dimensions);
        } else {
            new IllegalArgumentException(hashFamilyType + " is unknown, should be one of cos|l1|l2");
        }
        return family;
    }

    private boolean hasOption(String option) {
        int index = -1;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].equalsIgnoreCase(option)) {
                index = i;
            }
        }
        return index >= 0;
    }

    private Integer getIntegerValue(String option, Integer defaultValue) {
        String value = getValue(option, defaultValue.toString());
        Integer integerValue = null;
        try {
            integerValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String message;
            message = "Expected integer argument for option " + option + ",  " + value + " is" + " not an integer.";
            printError(message);

        }
        return integerValue;
    }

    private Float getFloatValue(String option, Float defaultValue) {
        String value = getValue(option, defaultValue.toString());
        Float doubleValue = null;
        try {
            doubleValue = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            String message;
            message = "Expected integer argument for option " + option + ",  " + value + " is" + " not an integer.";
            printError(message);

        }
        return doubleValue;
    }

    private String getValue(String option, String defaultValue) {
        int index = -1;
        final String value;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].equalsIgnoreCase(option)) {
                index = i;
            }
        }
        if (index >= 0) {
            value = arguments[index + 1];
        } else {
            value = defaultValue;
        }
        return value;
    }

    /**
     * TarsosLSH
     */
    private void printPrefix() {
        System.out.println("      _______                       _        ____ _     _");
        System.out.println("     |__   __|                     | |     / ____| |   | |");
        System.out.println("        | | __ _ _ __ ___  ___  ___| |    | (___ | |___| |");
        System.out.println("        | |/ _` | '__/ __|/ _ \\/ __| |     \\___ \\|  ___  |");
        System.out.println("        | | (_| | |  \\__ \\ (_) \\__ \\ |____ ____) | |   | |");
        System.out.println("        |_|\\__,_|_|  |___/\\___/|___/_____/|_____/|_|   |_|");
        System.out.println();
    }

    private void printLine() {
        System.err.println("----------------------------------------------------");
    }

    private void printHelp() {
        printPrefix();
        printLine();
        System.out.println("Name");
        System.out.println("	TarsosLSH: finds the nearest neighbours in a data set quickly, using LSH.");
        System.out.println("Synopsis     ");
        System.out.println("	java - jar TarsosLSH.jar [options]");
        System.out.println("Description");
        System.out.println("	Tries to find nearest neighbours for each vector in the");
        System.out.println("	query file, using Euclidean (L<sub>2</sub>) distance by default.");
        System.out.println("	");
        System.out.println("	Both dataset.txt and queries.txt have a similar format:");
        System.out.println("	an optional identifier for the vector and a list of N");
        System.out.println("	coordinates (which should be doubles).");
        System.out.println("	");
        System.out.println("	[Identifier] coord1 coord2 ... coordN");
        System.out.println("	[Identifier] coord1 coord2 ... coordN");
        System.out.println("	");
        System.out.println("	For an example data set with two elements and 4 dimensions:");
        System.out.println("		");
        System.out.println("	Hans 12 24 18.5 -45.6");
        System.out.println("	Jane 13 19 -12.0 49.8");
        System.out.println("	");
        System.out.println("	Options are:");
        System.out.println("		");
        System.out.println("	-d dataset.txt	");
        System.out.println("		The dataset with vectors to store in the hash table");
        System.out.println("	-q queries.txt	");
        System.out.println("		A list of vectors to query against the stored dataset");
        System.out.println("	-f cos|l1|l2");
        System.out.println("		Defines the hash family to use:");
        System.out.println("			l1	City block hash family (L<sub>1</sub>)");
        System.out.println("			l2	Euclidean hash family(L<sub>2</sub>)");
        System.out.println("			cos	Cosine distance hash family");
        System.out.println("	-r radius");
        System.out.println("		Defines the radius in which near neighbours should");
        System.out.println("		be found. Should be a double. By default a reasonable");
        System.out.println("		radius is determined automatically.");
        System.out.println("	-h n_hashes");
        System.out.println("		An integer that determines the number of hashes to");
        System.out.println("		use. By default 4, 32 for the cosine hash family.");
        System.out.println("	-t n_tables");
        System.out.println("		An integer that determines the number of hash tables,");
        System.out.println("		each with n_hashes, to use. By default 4.");
        System.out.println("	-n n_neighbours");
        System.out.println("		Number of neighbours in the neighbourhood, defaults to 3.");
        System.out.println("	-b");
        System.out.println("		Benchmark the settings.");
        System.out.println("	-l");
        System.out.println("		Do a linear search, ignores all LSH settings.");
        System.out.println("	--help");
        System.out.println("		Prints this helpful message.");
        System.out.println("	");
        System.out.println("Examples");
        System.out.println("	Search for nearest neighbours using the l2 hash family with a radius of 500");
        System.out.println("	and utilizing 5 hash tables, each with 3 hashes.");
        System.out.println("	");
        System.out.println("	java -jar TarsosLSH.jar -f l2 -r 500 -h 3 -t 5 -d dataset.txt -q queries.txt");
    }

    private void printError(String message) {
        printHelp();
        printLine();
        System.out.println("GURU MEDITATION:");
        printLine();
        System.out.println(message);

    }
}
