package test;

import Graph.*;
import ITSHA.*;
import Other.Solution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MainTest {

    public static void main(String args[]) throws CloneNotSupportedException {
        // Lista dei nodi
        ArrayList<Node> nodeList = new ArrayList<>();

        // Creazione dei nodi
        Node node0 = new Node(0, -1, 1.0, 2.0);
        Node node1 = new Node(1, 0, 5.0, 8.0);
        Node node2 = new Node(2, 2, 8.0, 8.0);
        Node node3 = new Node(3, 3, 1.0, 0.6);
        Node node4 = new Node(4, 5, 9.0, 11.0);
        Node node5 = new Node(5, 0, 8.0, 2.0);
        Node node6 = new Node(6, 2, 10.0, 2.0);
        Node node7 = new Node(7, 0, 9.0, 3.0);
        Node node8 = new Node(8, 0, 91.0, 3.);
        Node node9 = new Node(9, 1, 12.1,5.7);
        Node node10 = new Node(10, 1, 4.2,1.4);

        // Aggiunta dei nodi alla Lista
        nodeList.add(node0);
        nodeList.add(node1);
        nodeList.add(node2);
        nodeList.add(node3);
        nodeList.add(node4);
        nodeList.add(node5);
        nodeList.add(node6);
        nodeList.add(node7);
        nodeList.add(node8);
        nodeList.add(node9);
        nodeList.add(node10);

        // Creazione di un grafo non orientato
        Graph graph = new Graph(nodeList.size(), nodeList);

        // Aggiunta degli archi
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 4, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 7);
        graph.addEdge(1, 5, 10);
        graph.addEdge(2, 5, 7);
        graph.addEdge(2, 9, 3);
        graph.addEdge(9, 5, 11);
        graph.addEdge(3, 6, 2);
        graph.addEdge(4, 7, 3);
        graph.addEdge(7, 6, 4);
        graph.addEdge(6, 5, 4);
        graph.addEdge(8, 5, 4);
        graph.addEdge(10, 5, 4);
        graph.addEdge(10, 9, 8);

        // Array delle Navette
        Point[] cars = new Point[3];
        cars[0] = new Point(1, 6);
        cars[1] = new Point(6, 4);
        cars[2] = new Point(10, 4);


        /*
        graph.printGraph();

        graph = graph.GraphOptimalPath();

        System.out.println();
        System.out.println();
        System.out.println();

        Point[] points=graph.ExpandGraph();
        for(int i=0;i<points.length;i++){
            if(points[i]!=null)
                System.out.print("("+points[i].x+" "+points[i].y+"), ");
            else
                System.out.print(" (null), ");
        }
        graph.printGraph();

        ArrayList<Graph> k_cluster = Graph.K_Clustering(graph, cars);

        for(Graph g: k_cluster){
            g.printGraph();
        }
        */

        // Dimensione del grafo originale/iniziale
        int dimGrph= graph.V;

        // Espando il grfo
        // Array dei range dei nodi aggiunti associati all'i-esimo nodo
        Point[] addNode=graph.expandGraph();

        // Dimensione del grafo esteso
        int dimOG= graph.V;

        // Costruiscoil grafo ottimale dove ogni nodo è unito da un arco il quale costo è il valore del cammino minimo tra di essi nel grafo dato in input
        Graph graphOptimalPath = graph.GraphOptimalPath();

        // Assegno ai nodi del grafo delle etichette in ordine crescente partendo da 0
        int[] transform= graphOptimalPath.TrnasformGraph();

        //graphOptimalPath.printGraph();
        graphOptimalPath.printGraph();



        double[][] cities= new double[graphOptimalPath.V][2];
        for(Node n: graphOptimalPath.nodeList){
            cities[n.label][0]= n.x;
            cities[n.label][1]= n.y;
        }

        int numClusters = cars.length;
        double weightingExponent = 2.0;
        int maxIterations = 100;
        double epsilon = 0.01;
        int[] maxCitiesPerCluster = {7,7,3}; // Esempio di numero massimo di città per ciascun cluster

        FuzzyCMeans fcm = new FuzzyCMeans(numClusters, cities.length, cities, weightingExponent, maxCitiesPerCluster);
        int[] NC = fcm.runFCM(maxIterations, epsilon);

        for (int i = 1; i < NC.length; i++) {
            System.out.println("Citta" + i + ":"+NC[i]);
        }



        ArrayList<Integer>[] CS = new ArrayList[15];
        for (int i = 0; i < 15; i++) {
            CS[i] = new ArrayList<>();
            while (CS[i].size() < 10) {
                int randVal = new Random().nextInt(0,15);
                if (!CS[i].contains(randVal) && randVal != i) {
                    CS[i].add(randVal);
                }
            }
        }

        int Cmax = 10;
        Random random = new Random();

        ArrayList<Edge> edges = graphOptimalPath.getAllEdges();

        Solution Sbest= new Solution();
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(0,1,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(1,2,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(2,0,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(0,14,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(14,3,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(3,2,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(2,11,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(10,11,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(10,0,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(0,6,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(8,6,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(8,9,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(9,7,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(4,7,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(4,13,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(5,13,0))).findFirst().get());
        Sbest.addEdge(edges.stream().filter(x->x.equals(new Edge(0,5,0))).findFirst().get());
        Sbest.setValue(Integer.MAX_VALUE);


        RGP rgp= new RGP(NC,CS,Sbest,Cmax,edges,3);
        Solution S=rgp.run();

        System.out.println(S);

        Solution[] solutionTrans=S.transformSolution2(addNode, transform,graphOptimalPath.V,numClusters, dimGrph);

        for(Solution s: solutionTrans)
            System.out.println("sol= "+s);

        /*
        // pre trasformale la soluzione per il grafo iniziale
        int x=transform[14];
        System.out.println(x);
        for(int i=0; i<dimGrph; i++){
            if(addNode[i]!=null && addNode[i].x>= x && addNode[i].y<= x)
                System.out.println(i);
        }

         */
    }

}