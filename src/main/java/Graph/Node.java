package Graph;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

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
}
