package ITSHA;

import Graph.*;
import Other.*;
import java.awt.*;
import java.util.ArrayList;

public class ITSHA{

    public static void ITSHARun(int Cmax, int At, int Tmax, double weightingExponent, double epsilon, int Ac) throws CloneNotSupportedException {
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
        Node node9 = new Node(9, 5, 12.1,5.7);
        Node node10 = new Node(10, 5, 4.2,1.4);

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
        int[] maxCitiesForCluster = new int[2];
        maxCitiesForCluster[0] = 12;
        maxCitiesForCluster[1] = 12;

        // Dimensione del grafo originale/iniziale
        int dimGrph= graph.V;

        // Espando il grfo
        // Array dei range dei nodi aggiunti associati all'i-esimo nodo
        Point[] addNode=graph.expandGraph();

        // Costruiscoil grafo ottimale dove ogni nodo è unito da un arco il quale costo è il valore del cammino minimo tra di essi nel grafo dato in input
        Graph graphOptimalPath = graph.GraphOptimalPath();

        // Archi del grafo ottimo
        ArrayList<Edge> inizialEdges= new ArrayList<>();
        for(Edge edge: graphOptimalPath.getAllEdges())
            inizialEdges.add((Edge) edge.clone());

        // Assegno ai nodi del grafo delle etichette in ordine crescente partendo da 0
        int[] transform= graphOptimalPath.TrnasformGraph();

        double[][] cities= new double[graphOptimalPath.V][2];
        for(Node n: graphOptimalPath.nodeList){
            cities[n.label][0]= n.x;
            cities[n.label][1]= n.y;
        }

        int numClusters = maxCitiesForCluster.length;
        int maxIterations = 100;


        // COSTRUZIONE GRAFO E PARAMETRI CLUSTERING TERMINATA


        // INIZIO ALGORITMO ITSHA

        // Tempo inizio esecuzine algoritmo
        long startTime = System.nanoTime();

        Solution Sbest= new Solution();
        Sbest.value = Integer.MAX_VALUE;

        Solution Sbetter = new Solution();

        Solution S;

        // Instanza di Random Greedy Procedure
        RGP rgp;

        // Inizializzazione parametri FCM
        FuzzyCMeans fcm = new FuzzyCMeans(numClusters, cities.length, cities, weightingExponent, maxCitiesForCluster);

        // Vettore delle città contenenti il numero del Cluster/tour a cui appartiene.
        int[] NC;

        // Inizializzazione Vicinati delle città
        ArrayList<Integer>[] CS = CollectionS.Initialize_Candidates(Cmax, graphOptimalPath);

        // Archi del graphOptimalPath transformato
        ArrayList<Edge> edges;

        while (true) {
            // Calcola il tempo trascorso in secondi
            long elapsedTime = (System.nanoTime() - startTime) / 1_000_000_000;

            // Verifica se il tempo trascorso è maggiore o uguale al numero massimo di secondi
            if (elapsedTime >= Tmax) {
                break;
            }

            Sbetter.value = Integer.MAX_VALUE;

            // Costruzione Cluster
            NC = fcm.runFCM(maxIterations, epsilon);

            // Archi del grafo finale
            edges = graphOptimalPath.getAllEdges();

            // Inizializzazione parametri RGP
            rgp =  new RGP(NC, CS, Sbest, Cmax, edges, numClusters);

            // Esecuzione RGP, creazione soluzione iniziale
            S = rgp.run();

            for(int numT=0; numT < At + 1; numT++){
                // Modifica della soluzione in modo semi-casuale
                S = VNS.Adjust_Solution(S, maxCitiesForCluster, addNode,transform, graphOptimalPath.V, numClusters, dimGrph,Ac);

                // Algoritmo di miglioramento della soluzione
                S = VNS.VNSrun(S, CS, Cmax, maxCitiesForCluster, addNode, transform, graphOptimalPath.V, numClusters, dimGrph, inizialEdges);

                // Impoasto il valore di Sbetter uguale alla soluzione con valore minore
                if(Sbetter.value == Integer.MAX_VALUE || Solution.valueSolutionTransformed(S.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges) < Solution.valueSolutionTransformed(Sbetter.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges))
                    Sbetter= S;
            }

            if(Sbest.value != Integer.MAX_VALUE)
                CollectionS.Adjust_Candidates(CS, S, Sbest);

            if(Sbest.value== Integer.MAX_VALUE || Solution.valueSolutionTransformed(S.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges) < Solution.valueSolutionTransformed(Sbest.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges))
                Sbest= S;
        }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        for (ArrayList<Point> arrayList : Sbest.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph)) {
            for (Point p : arrayList) {
                System.out.print(" [" + p.x + ", " + p.y + "] ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        int val=0;
        int nodePrec;
        int nodeNext;

        for(ArrayList<Point> arrayList:Sbest.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph)){
            nodePrec= 0;
            for(Point p:arrayList) {
                nodeNext= p.x;
                System.out.print(" ["+nodePrec+", "+nodeNext+"] ");
                val += graph.optimalValueDijkstra(nodePrec,nodeNext);
                nodePrec=nodeNext;
            }
        }

        System.out.println();
        System.out.println("Val :"+val);
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        ITSHA.ITSHARun(10,3,30,2,0.0001,5);
    }
}
