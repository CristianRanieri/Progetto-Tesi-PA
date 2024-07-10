package Other;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Cluster {

    public int label;
    public ArrayList<Integer> cityIndices;
    public int maxCities;

    public Cluster(int label, int maxCities) {
        this.label = label;
        this.cityIndices = new ArrayList<>();
        this.maxCities = maxCities;
    }

    public int getLabel() {
        return label;
    }

    public ArrayList<Integer> getCityIndices() {
        return cityIndices;
    }

    public int getMaxCities() {
        return maxCities;
    }

    public void addCityIndex(int index) {
        cityIndices.add(index);
    }

    @Override
    public String toString() {
        return "{" +
                "label='" + label + '\'' +
                ", cityIndices=" + cityIndices +
                '}';
    }

    public static int[] readMaxCitiesForClusterFromFile(String filePath) {
        List<Integer> maxCitiesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                int value = Integer.parseInt(line.trim());
                maxCitiesList.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ordinamento decrescente
        Collections.sort(maxCitiesList, Collections.reverseOrder());

        // Convertiamo la lista in un array di interi
        int[] maxCitiesForCluster = new int[maxCitiesList.size()];
        for (int i = 0; i < maxCitiesList.size(); i++) {
            maxCitiesForCluster[i] = maxCitiesList.get(i);
        }

        return maxCitiesForCluster;
    }
}