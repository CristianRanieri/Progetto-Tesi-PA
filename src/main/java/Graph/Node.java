package Graph;

import Other.Cluster;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Node implements Comparator<Node>, Cloneable{
    //numero del nodo
    public int label;
    //costo per raggiungere il nodo utilizzato da dijikastra e altri algoritmi(per esempio per la PQ)
    public int cost;
    // 3 tipologie
    // -1 locale (punto di interesse)
    // 0 nodo di transizione
    // 1-10 nodo che contine una persona
    public int type;
    // Posizioni longitudine e latitudine
    public double x;
    public double y;

    public Node(){}

    public Node(int label, int type, int cost) {
        this.label = label;
        this.type= type;
        this.cost = cost;
    }

    public Node(int label, int type, double x, double y) {
        this.label = label;
        this.type= type;
        this.x = x;
        this.y = y;
    }

    public Node(int label, int cost) {
        this.label = label;
        this.type= 0;
        this.cost = cost;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Metodo per confrontare i nodi in base al costo
    @Override
    public int compare(Node node1, Node node2) {
        if (node1.cost < node2.cost) return -1;
        if (node1.cost > node2.cost) return 1;
        return 0;
    }


    public static ArrayList<Node> readNodesFromFile(String filePath) {
        ArrayList<Node> nodeList = new ArrayList<>();

        // Usa FileReader per leggere il file dal percorso assoluto specificato
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                int parentId = Integer.parseInt(parts[1].trim());
                double x = Double.parseDouble(parts[2].trim());
                double y = Double.parseDouble(parts[3].trim());
                nodeList.add(new Node(id, parentId, x, y));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodeList;
    }

    public static void writeNodesToFile(List<Node> nodeList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/src/main/java/test/"+fileName))) {
            for (Node node : nodeList) {
                writer.write(node.label + ", " + node.type + ", " + node.x + ", " + node.y);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {

        String directory = "node250_4/";

        //GENERAZIONE NODI

        List<Node> nodeList = new ArrayList<>();
        nodeList.add(new Node(0,-1, 50.0,50.0));

        Random random= new Random();
        int type;
        int totType = 0;

        for(int i = 1; i < 150; i++) {

            if(totType> 250)
                type=0;
            else {
                type = random.nextInt(0, 20);

                if(type <= 10)
                    type = 0;
                else
                    type = random.nextInt(0, 10);
            }

            totType += type;

            double x = Math.round(random.nextDouble() * 100 * 10) / 10.0; // Genera e approssima alla prima cifra decimale
            double y = Math.round(random.nextDouble() * 100 * 10) / 10.0;

            nodeList.add(new Node(i,type, x, y));
        }

        System.out.println("valore type:"+ totType);

        // Scrittura della lista di nodi in un file
        Node.writeNodesToFile(nodeList, directory+"nodeListTest.txt");



        //GENERAZIONE ARCHI

        List<Edge> edges = new ArrayList<>();

        for(int i = 0; i < 149; i++) {
            edges.add(new Edge(i,i+1,random.nextInt(1,15)));
        }

        for(int i = 0; i < 4500; i++) {
            int node1 = random.nextInt(0,150);
            int node2 = random.nextInt(0,150);

            Edge edge= new Edge(node1,node2, random.nextInt(1,15));

            if(edges.stream().filter(e -> e.equals(edge)).toList().isEmpty())
                edges.add(edge);
            else
                i--;
        }

        EdgeLinkedList.writeEdgesToFile(edges,directory+"edgeListTest.txt");


        // GENERAZIONE CLUSTER

        ArrayList<Integer> clusterValues = new ArrayList<>();

        int clusterTotal = 0;
        int clusterValue;

        while (clusterTotal < totType){
            clusterValue = random.nextInt(1,10);

            clusterValues.add(clusterValue);

            clusterTotal += clusterValue;
        }

        Cluster.writeMaxCitiesForClusterToFile(clusterValues, directory+"carsListTest.txt");
    }

}
