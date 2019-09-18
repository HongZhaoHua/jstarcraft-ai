package be.tarsos.lsh.hamming;

import java.util.List;
import java.util.Random;

/**
 * A class implementing a command line interface for the LSH program.
 * 
 * @author Joren Six
 */
public class BinCommandLineInterface {

    private String[] arguments;

    private int numberOfHashTables;
    private int numberOfHashes;
    private int numberOfNeighbours;
    private double radius;

    private List<BinVector> dataset;
    private List<BinVector> queries;

    private int nBits;

    public BinCommandLineInterface(String... args) {
        this.arguments = args;
    }

    /**
     * Parses the arguments currently stored in the argument list.
     */
    public void parseArguments() {
        numberOfHashTables = getIntegerValue("-t", 16);
        numberOfHashes = getIntegerValue("-h", 2);
        numberOfNeighbours = getIntegerValue("-n", 4);

        radius = getDoubleValue("-r", 0.0);

        String datasetFile = getValue("-d", null);
        String queryFile = getValue("-q", null);

        if (datasetFile != null) {
            dataset = BinLSH.readDataset(datasetFile, Integer.MAX_VALUE);
            // dimensions = dataset.get(0).length();
        }
        if (queryFile != null) {
            queries = BinLSH.readDataset(queryFile, Integer.MAX_VALUE);
            // dimensions = queries.get(0).getDimensions();
        }

    }

    public void startApplication() {
        printPrefix();
        printLine();
        startLSH();
    }

    private void startLSH() {
        Random rand = new Random(0L);
        BinLSH lsh = new BinLSH(dataset);
        lsh.buildIndex(rand, numberOfHashes, numberOfHashTables, 12);
        if (queries != null) {
            for (BinVector query : queries) {
                List<BinVector> neighbours = lsh.query(query, numberOfNeighbours);
                System.out.print(query.getKey() + ";");
                for (BinVector neighbour : neighbours) {
                    System.out.print(neighbour.getKey() + ";");
                }
                System.out.print("\n");
            }
        }

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

    private Double getDoubleValue(String option, Double defaultValue) {
        String value = getValue(option, defaultValue.toString());
        Double doubleValue = null;
        try {
            doubleValue = Double.parseDouble(value);
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

    private void printError(String message) {
        printLine();
        System.out.println("GURU MEDITATION:");
        printLine();
        System.out.println(message);

    }
}
