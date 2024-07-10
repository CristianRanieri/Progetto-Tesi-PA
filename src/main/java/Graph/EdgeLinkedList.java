package Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EdgeLinkedList {
    public int destination;
     public int weight;

    public EdgeLinkedList(int destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public static void readEdgesFromFile(Graph graph,String filePath) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int source = Integer.parseInt(parts[0].trim());
                int destination = Integer.parseInt(parts[1].trim());
                int weight = Integer.parseInt(parts[2].trim());
                graph.addEdge(source,destination,weight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
