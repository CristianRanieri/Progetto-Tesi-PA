package Other;

import java.util.ArrayList;

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
}