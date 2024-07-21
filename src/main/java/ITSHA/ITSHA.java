package ITSHA;

import Graph.*;
import Other.*;
import java.awt.*;
import java.util.ArrayList;

public class ITSHA{

    public static ArrayList<Point>[] ITSHARun(int Cmax, int At, int Tmax, double weightingExponent, double epsilon, int Ac, String directory, String fileNameNode, String fileNameEdge, String fileNameCars) throws CloneNotSupportedException {
        // Lista dei nodi
        ArrayList<Node> nodeList = Node.readNodesFromFile(System.getProperty("user.dir")+"/src/main/java/test/"+directory+fileNameNode);

        // Creazione di un grafo non orientato
        Graph graph = new Graph(nodeList.size(), nodeList);

        // Aggiunta archi tra i nodi
        EdgeLinkedList.readEdgesFromFile(graph,System.getProperty("user.dir")+"/src/main/java/test/"+directory+fileNameEdge);

        // Inizializzazione array delle Navette/Cluster
        int[] maxCitiesForCluster = Cluster.readMaxCitiesForClusterFromFile(System.getProperty("user.dir")+"/src/main/java/test/"+directory+fileNameCars);

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

            // Esecuzione RGP, creazione solution iniziale
            S = rgp.run();

            for(int numT=0; numT < At + 1; numT++){
                // Modifica della solution in modo semi-casuale
                S = VNS.Adjust_Solution(S, maxCitiesForCluster, addNode,transform, graphOptimalPath.V, numClusters, dimGrph,Ac);

                // Algoritmo di miglioramento della solution
                S = VNS.VNSrun(S, CS, Cmax, maxCitiesForCluster, addNode, transform, graphOptimalPath.V, numClusters, dimGrph, inizialEdges);

                // Impoasto il valore di Sbetter uguale alla solution con valore minore
                if(Sbetter.value == Integer.MAX_VALUE || Solution.valueSolutionTransformed(S.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges) < Solution.valueSolutionTransformed(Sbetter.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges))
                    Sbetter= S;
            }

            if(Sbest.value != Integer.MAX_VALUE)
                CollectionS.Adjust_Candidates(CS, S, Sbest);

            if(Sbest.value == Integer.MAX_VALUE || Solution.valueSolutionTransformed(S.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges) < Solution.valueSolutionTransformed(Sbest.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph), inizialEdges))
                Sbest= S;
        }

        ArrayList<Point>[] finalSolution = Sbest.transformSolution(addNode, transform, graphOptimalPath.V, numClusters, dimGrph);


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        for (ArrayList<Point> arrayList : finalSolution) {
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


        for(ArrayList<Point> arrayList : finalSolution){
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

        //if(val < 2700)
            Solution.writeSolutionOnFile(finalSolution,directory+"solution",val);

        return finalSolution;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        String directory= "node50_3/";

        for (int i = 0; i < 10; i++) {

            ArrayList<Point>[] solution = ITSHA.ITSHARun(10, 3, 30, 2, 0.0001, 5, directory, "nodeListTest.txt", "edgeListTest.txt", "carsListTest.txt");

            boolean b = Solution.verifySolution(solution, directory + "nodeListTest.txt", directory + "carsListTest.txt");

            if (b)
                System.out.println("Verifica risultato: RISULTATO AMMISSIBILE");
            else
                System.out.println("Verifica risultato: XX RISULTATO NON AMMISSIBILE XX");
        }
    }
}
