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

package be.tarsos.lsh.experimental;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.tarsos.lsh.LSH;
import be.tarsos.lsh.Vector;
import be.tarsos.lsh.families.DistanceMeasure;
import be.tarsos.lsh.families.EuclideanDistance;

public class Clustering {

    private static class Cluster {
        List<Vector> members;
        Vector seed;

        public Cluster(Vector seed) {
            members = new ArrayList<Vector>();
            this.seed = seed;
        }

        public Vector getSeed() {
            return this.seed;
        }

        public void addVector(Vector v) {
            members.add(v);
        }

        public void determineNewSeed() {
            Vector newSeed = new Vector(seed.getDimensions());
            for (Vector v : members) {
                for (int d = 0; d < v.getDimensions(); d++) {
                    newSeed.set(d, newSeed.get(d) + v.get(d));
                }
            }
            float memberSize = members.size();
            for (int d = 0; d < newSeed.getDimensions(); d++) {
                newSeed.set(d, newSeed.get(d) / memberSize);
            }
            this.seed = newSeed;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < members.size(); i++) {
                sb.append(members.get(i).getKey());
                if (i != members.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    // Implements K-means clustering
    public static void kMeansClustering(int k, List<Vector> dataset) {
        Cluster[] clusters = new Cluster[k];
        Collections.shuffle(dataset);
        for (int i = 0; i < k; i++) {
            Vector randomVector = dataset.get(i);
            clusters[i] = new Cluster(randomVector);
            clusters[i].addVector(randomVector);
        }
        DistanceMeasure dm = new EuclideanDistance();
        boolean clusteringStable = false;
        String previousClustering = "";
        while (!clusteringStable) {
            for (Cluster c : clusters) {
                c.determineNewSeed();
                c.members.clear();
            }
            for (Vector v : dataset) {
                double smallestDistance = Double.MAX_VALUE;
                Cluster closesCluster = null;
                for (Cluster c : clusters) {
                    double distance = dm.distance(v, c.getSeed());
                    if (distance < smallestDistance) {
                        closesCluster = c;
                        smallestDistance = distance;
                    }
                }
                closesCluster.addVector(v);
            }

            StringBuilder newClustering = new StringBuilder();
            for (Cluster c : clusters) {
                newClustering.append(c.toString());
            }
            clusteringStable = previousClustering.equals(newClustering.toString());
            previousClustering = newClustering.toString();
        }
        for (Cluster c : clusters) {
            System.out.println(c.toString());
        }
    }

    public static void nearestNeighbourClustering(double threshold, List<Vector> dataset) {
        // Nearest neighbour clustering
        List<Cluster> clusters = new ArrayList<Clustering.Cluster>();
        DistanceMeasure dm = new EuclideanDistance();
        for (Vector v : dataset) {
            double smallestDistance = Double.MAX_VALUE;
            Cluster closestCluster = null;
            for (Cluster c : clusters) {
                for (Vector w : c.members) {
                    double distance = dm.distance(w, v);
                    if (distance < smallestDistance) {
                        closestCluster = c;
                        smallestDistance = distance;
                    }
                }
            }
            if (smallestDistance < threshold) {
                closestCluster.addVector(v);
            } else {
                closestCluster = new Cluster(v);
                closestCluster.addVector(v);
                clusters.add(closestCluster);
            }
        }
        for (Cluster c : clusters) {
            System.out.println(c.toString());
        }
    }

    public static void main(String... strings) {
        Vector[] a = new Vector[8];
        List<Vector> dataset = new ArrayList<Vector>();
        for (int i = 0; i < a.length; i++) {
            a[i] = new Vector(2);
            dataset.add(a[i]);
        }
        // see
        // http://webdocs.cs.ualberta.ca/~zaiane/courses/cmput695/F07/exercises/Exercises695Clus-solution.pdf
        a[0].setKey("a1");
        a[0].set(0, 2);
        a[0].set(1, 10);
        a[1].setKey("a2");
        a[1].set(0, 2);
        a[1].set(1, 5);
        a[2].setKey("a3");
        a[2].set(0, 8);
        a[2].set(1, 4);
        a[3].setKey("a4");
        a[3].set(0, 5);
        a[3].set(1, 8);
        a[4].setKey("a5");
        a[4].set(0, 7);
        a[4].set(1, 5);
        a[5].setKey("a6");
        a[5].set(0, 6);
        a[5].set(1, 4);
        a[6].setKey("a7");
        a[6].set(0, 1);
        a[6].set(1, 2);
        a[7].setKey("a8");
        a[7].set(0, 4);
        a[7].set(1, 9);

        System.out.println("\nK-means clustering");
        kMeansClustering(3, dataset);

        System.out.println("\nNearest neighbour clustering");
        nearestNeighbourClustering(4, dataset);

        nearestNeighbourClustering(1000, LSH.readDataset("output_dataset.txt", Integer.MAX_VALUE));
    }

}
