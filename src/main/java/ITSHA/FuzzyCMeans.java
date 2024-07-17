package ITSHA;

import Other.Cluster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class FuzzyCMeans {

    private int numClusters;       // Numero di cluster
    private int numCities;         // Numero di città
    private double[][] cities;     // Posizioni delle città
    private double[][] membership; // Matrice di membership
    private double[][] clusterCenters; // Centri dei cluster
    private double weightingExponent; // Esponente di ponderazione
    private int[] maxCitiesPerCluster; // Numero massimo di città per cluster

    // Costruttore
    public FuzzyCMeans(int numClusters, int numCities, double[][] cities, double weightingExponent, int[] maxCitiesPerCluster) {
        this.numClusters = numClusters; //
        this.numCities = numCities; //
        this.cities = cities; //
        this.weightingExponent = weightingExponent; //
        this.membership = new double[numCities][numClusters]; //
        this.clusterCenters = new double[numClusters][cities[0].length]; //
        this.maxCitiesPerCluster = maxCitiesPerCluster;
    }

    // Metodo principale per eseguire l'algoritmo FCM
    public int[] runFCM(int maxIterations, double epsilon) {
        int[] NC = new int[numCities];
        for (int i = 1; i < numCities; i++)
            NC[i] = -1;

        int[] Cnum= new int[numClusters];
        for (int i = 0; i < numClusters; i++)
            Cnum[i] = 0;

        initializeMembership();
        double maxDiff;
        do{
            updateClusterCenters();
            maxDiff = updateMembership();
        }while (maxDiff < epsilon);


        int t=-1;
        for(int j = 0; j < numClusters; j++){
            // Definisco il valore di t
            for(int i=1; i< numCities; i++)
                if(NC[i] == -1)
                    t=i;

            for(int i = 1; i < numCities; i++)
                if(NC[i]==-1 && membership[i][j] > membership[t][j])
                    t=i;
            NC[t]=j;
            Cnum[j]=1;
        }

        for(int i = 1; i < numCities; i++){
            t=-1;
            // Seleziono il primo cluster non vuoto
            for(int j = 0; j < numClusters; j++)
                if(Cnum[j] < maxCitiesPerCluster[j])
                    t=j;

            if(NC[i]==-1){
                for(int j = 0; j < numClusters; j++)
                    if(Cnum[j] < maxCitiesPerCluster[j] && membership[i][j] > membership[i][t])
                        t=j;
                NC[i]=t;
                Cnum[t]++;
            }
        }

        return NC;
    }

    // Inizializzazione della matrice di membership casualmente
    private void initializeMembership() {
        Random random = new Random();
        for (int i = 0; i < numCities; i++) {
            double sum = 0.0;
            for (int j = 0; j < numClusters; j++) {
                membership[i][j] = random.nextDouble();
                sum += membership[i][j];
            }
            for (int j = 0; j < numClusters; j++) {
                membership[i][j] /= sum;
            }
        }
    }

    // Aggiornamento dei centri dei cluster
    private void updateClusterCenters() {
        for (int j = 0; j < numClusters; j++) {
            double[] numerator = new double[cities[0].length];
            double denominator = 0.0;

            for (int i = 1; i < numCities; i++) {
                double uijWeighted = Math.pow(membership[i][j], weightingExponent);
                for (int k = 0; k < cities[0].length; k++) {
                    numerator[k] += uijWeighted * cities[i][k];
                }
                denominator += uijWeighted;
            }

            for (int k = 0; k < cities[0].length; k++) {
                clusterCenters[j][k] = numerator[k] / denominator;
            }
        }
    }

    // Aggiornamento della matrice di membership
    private double updateMembership() {
        double maxDiff = 0.0;

        for (int i = 1; i < numCities; i++) {
            for (int j = 0; j < numClusters; j++) {
                double oldUij = membership[i][j];
                membership[i][j] = calculateUij(i, j);
                double diff = Math.abs(membership[i][j] - oldUij);
                if (diff > maxDiff) {
                    maxDiff = diff;
                }
            }
        }

        return maxDiff;
    }

    // Calcolo del valore di membership per la città i e il cluster j
    private double calculateUij(int i, int j) {
        double denominator = 0.0;
        double distanceToClusterJ = distance(cities[i], clusterCenters[j]);

        for (int k = 0; k < numClusters; k++) {
            double distanceToClusterK = distance(cities[i], clusterCenters[k]);
            denominator += Math.pow(distanceToClusterJ / distanceToClusterK, 2.0 / (weightingExponent - 1.0));
        }

        return 1.0 / denominator;
    }

    // Calcolo della distanza euclidea tra due punti
    private double distance(double[] point1, double[] point2) {
        double sum = 0.0;
        for (int k = 0; k < point1.length; k++) {
            sum += Math.pow(point1[k] - point2[k], 2);
        }
        return Math.sqrt(sum);
    }

    // Stampa i risultati
    public void printResults() {
        System.out.println("Centri dei Cluster:");
        for (int j = 0; j < numClusters; j++) {
            System.out.println(Arrays.toString(clusterCenters[j]));
        }

        System.out.println("Matrice di Membership:");
        for (int i = 0; i < numCities; i++) {
            System.out.println(Arrays.toString(membership[i]));
        }
    }

    // Assegna ogni città al cluster con il valore di membership più alto
    public ArrayList<Cluster> assignCitiesToClusters() {
        ArrayList<Cluster> clusters = new ArrayList<>();
        for (int j = 0; j < numClusters; j++) {
            clusters.add(new Cluster( j, maxCitiesPerCluster[j]));
        }

        // Lista delle città con i loro cluster preferiti (per la ridistribuzione)
        ArrayList<int[]> cityClusterPreferences = new ArrayList<>();

        for (int i = 0; i < numCities; i++) {
            int clusterIndex = 0;
            double maxMembership = membership[i][0];
            for (int j = 1; j < numClusters; j++) {
                if (membership[i][j] > maxMembership) {
                    maxMembership = membership[i][j];
                    clusterIndex = j;
                }
            }
            clusters.get(clusterIndex).addCityIndex(i);
            cityClusterPreferences.add(new int[]{i, clusterIndex});
        }

        // Controllo e ridistribuzione delle città in eccesso
        boolean redistributionNeeded;
        do {
            redistributionNeeded = false;
            for (Cluster cluster : clusters) {
                while (cluster.getCityIndices().size() > cluster.getMaxCities()) {
                    redistributionNeeded = true;
                    int cityIndex = cluster.getCityIndices().remove(cluster.getCityIndices().size() - 1);
                    redistributeCity(cityIndex, clusters, cityClusterPreferences);
                }
            }
        } while (redistributionNeeded);

        return clusters;
    }

    // Ridistribuisce una città a un nuovo cluster
    private void redistributeCity(int cityIndex, ArrayList<Cluster> clusters, ArrayList<int[]> cityClusterPreferences) {
        double maxMembership = 0.0;
        int newClusterIndex = -1;
        for (int j = 0; j < numClusters; j++) {
            if (clusters.get(j).getCityIndices().size() < clusters.get(j).getMaxCities() && membership[cityIndex][j] > maxMembership) {
                maxMembership = membership[cityIndex][j];
                newClusterIndex = j;
            }
        }

        if (newClusterIndex != -1) {
            clusters.get(newClusterIndex).addCityIndex(cityIndex);
        } else {
            // Se non è possibile trovare un cluster valido (caso raro), aggiungi la città a un cluster con meno membri
            int minClusterIndex = 0;
            for (int j = 1; j < numClusters; j++) {
                if (clusters.get(j).getCityIndices().size() < clusters.get(minClusterIndex).getCityIndices().size()) {
                    minClusterIndex = j;
                }
            }
            clusters.get(minClusterIndex).addCityIndex(cityIndex);
        }
    }



    // Assegna ogni città al cluster con il valore di membership più alto
    public int[] assignCitiesToClusters2() {
        ArrayList<Cluster> clusters = this.assignCitiesToClusters();
        int[] cluster = new int[cities.length];
        for(int i=0; i<cities.length; i++) {
            int finalI1 = i;
            cluster[i] = clusters.stream().filter(x -> x.getCityIndices().stream().filter(y -> y== finalI1).toArray().length!=0).findFirst().get().getLabel();
        }

        return cluster;
    }



    public static void main(String[] args) {
        // Dati di esempio (punti 2D)
        double[][] cities = {
                {1.0, 2.0},
                {1.5, 1.8},
                {5.0, 8.0},
                {8.0, 8.0},
                {1.0, 0.6},
                {9.0, 11.0},
                {8.0, 2.0},
                {10.0, 2.0},
                {9.0, 3.0},
                {91.0, 3.0}
        };

        int numClusters = 3;
        double weightingExponent = 2.0;
        int maxIterations = 100;
        double epsilon = 0.01;
        int[] maxCitiesPerCluster = {4,3,3}; // Esempio di numero massimo di città per ciascun cluster

        FuzzyCMeans fcm = new FuzzyCMeans(numClusters, cities.length, cities, weightingExponent, maxCitiesPerCluster);
        int[] NC = fcm.runFCM(maxIterations, epsilon);

        for (int i = 1; i < NC.length; i++) {
            System.out.println("Citta" + i + ":"+NC[i]);
        }

        /*
        fcm.printResults();

        ArrayList<Cluster> clusters = fcm.assignCitiesToClusters();

        for(Cluster c:clusters)
            System.out.println(c);

        int[] cluster = fcm.assignCitiesToClusters2();

        for (int i = 0; i < cities.length; i++) {
            System.out.println("Citta" + i + ":"+cluster[i]);
        }
         */

    }
}