package Graph;

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
}
