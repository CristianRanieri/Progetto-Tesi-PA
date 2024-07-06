package ITSHA;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ITSHAAlgorithm {

    public static class City implements Clusterable {
        private double[] points;

        public City(double x, double y) {
            this.points = new double[]{x, y};
        }

        @Override
        public double[] getPoint() {
            return points;
        }
    }

    // Fuzzy C-means Clustering (Using KMeans for simplicity)
    public static List<List<City>> fuzzyCMeans(List<City> cities, int nClusters) {
        KMeansPlusPlusClusterer<City> clusterer = new KMeansPlusPlusClusterer<>(nClusters, 1000);
        List<List<City>> clusters = new ArrayList<>();
        clusterer.cluster(cities).forEach(cluster -> clusters.add(cluster.getPoints()));
        return clusters;
    }

    // Random Greedy Procedure
    public static List<List<City>> randomGreedyProcedure(List<List<City>> clusters) {
        List<List<City>> solution = new ArrayList<>();
        Random random = new Random();

        for (List<City> cluster : clusters) {
            if (cluster.isEmpty()) continue;

            List<City> tour = new ArrayList<>();
            City currentCity = cluster.remove(random.nextInt(cluster.size()));
            tour.add(currentCity);

            while (!cluster.isEmpty()) {
                City nextCity = null;
                double minDistance = Double.MAX_VALUE;

                for (City city : cluster) {
                    double dist = distance(currentCity, city);
                    if (dist < minDistance) {
                        minDistance = dist;
                        nextCity = city;
                    }
                }

                if (nextCity != null) {
                    tour.add(nextCity);
                    cluster.remove(nextCity);
                    currentCity = nextCity;
                }
            }

            solution.add(tour);
        }
        return solution;
    }

    // Variable Neighborhood Search (Placeholder function)
    public static List<List<City>> VNS(List<List<City>> solution) {
        // Placeholder for the VNS logic
        return solution;
    }

    // ITSHA Algorithm
    public static List<List<City>> ITSHA(List<City> cities, int nClusters) {
        // Initialization Stage
        List<List<City>> clusters = fuzzyCMeans(cities, nClusters);
        List<List<City>> initialSolution = randomGreedyProcedure(clusters);

        // Improvement Stage
        List<List<City>> improvedSolution = VNS(initialSolution);

        return improvedSolution;
    }

    // Calculate Euclidean distance between two cities
    public static double distance(City city1, City city2) {
        return new EuclideanDistance().compute(city1.getPoint(), city2.getPoint());
    }

    public static void main(String[] args) {
        List<City> cities = new ArrayList<>();
        cities.add(new City(0, 0));
        cities.add(new City(1, 1));
        cities.add(new City(1, 0));
        cities.add(new City(0, 1));
        cities.add(new City(10, 10));
        cities.add(new City(10, 11));
        cities.add(new City(11, 10));
        cities.add(new City(11, 11));

        int nClusters = 3;
        List<List<City>> solution = ITSHA(cities, nClusters);

        System.out.println(":");
        for (List<City> tour : solution) {
            for (City city : tour) {
                System.out.print("(" + city.getPoint()[0] + "," + city.getPoint()[1] + ") ");
            }
            System.out.println();
        }
    }
}
