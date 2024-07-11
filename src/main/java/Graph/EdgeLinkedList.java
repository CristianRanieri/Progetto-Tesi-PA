package Graph;

import java.io.*;
import java.util.List;

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

    public static void writeEdgesToFile(List<Edge> nodeList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/src/main/java/test/"+fileName))) {
            for (Edge edge : nodeList) {
                writer.write(edge.node1 + ", " + edge.node2 + ", " + edge.weight);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
