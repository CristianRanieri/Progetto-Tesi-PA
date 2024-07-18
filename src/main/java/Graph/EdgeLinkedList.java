package Graph;

import java.io.*;
import java.util.ArrayList;
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

    public static List<Edge> readEdgesFromFileTransform(String filePath) {
        List<Edge> edges = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int source = Integer.parseInt(parts[0].trim());
                int destination = Integer.parseInt(parts[1].trim());
                int weight = Integer.parseInt(parts[2].trim());
                edges.add(new Edge(source,destination,weight));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edges;
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


    public static void main(String[] args) {
        String direcotry= "node150_2/";

        // Lista dei nodi
        ArrayList<Node> nodeList = Node.readNodesFromFile(System.getProperty("user.dir")+"/src/main/java/test/"+direcotry+"nodeListTest.txt");


        // Aggiunta archi tra i nodi
        List<Edge> edges = EdgeLinkedList.readEdgesFromFileTransform(System.getProperty("user.dir")+"/src/main/java/test/"+direcotry+"edgeListTest.txt");

        for(Edge e : edges){
            Node x1 = nodeList.stream().filter( node -> node.label == e.getNode1()).findFirst().get();
            Node x2 = nodeList.stream().filter( node -> node.label == e.getNode2()).findFirst().get();

            // Calcola la distanza euclidea
            double distance = Math.sqrt(Math.pow(x2.x - x1.x, 2) + Math.pow(x2.y - x1.y, 2));

            // Arrotonda per eccesso all'intero più vicino
            e.weight =  (int) Math.ceil(distance);
        }

        EdgeLinkedList.writeEdgesToFile(edges,direcotry+"edgeListTest.txt");
    }
}
