package Graph;

import Other.Cluster;
import java.awt.*;
import java.util.List;
import java.util.*;


// Classe per rappresentare un grafo orientato utilizzando liste di adiacenza
public class Graph {
    public int V; // Numero di vertici
    public LinkedList<EdgeLinkedList>[] adjList; // Lista di adiacenza per ogni vertice
    public ArrayList<Node> nodeList;

    // Costruttore del grafo
    public Graph(int size,ArrayList<Node> nodeList) {
        // Nunero dei nodi
        V = size;

        // Assegno lista dei nodi
        this.nodeList= nodeList;

        // Assegno ad ongi nodo la lista linkata degli archi
        this.adjList = new LinkedList[V];
        for (Node n : nodeList)
            this.adjList[n.label] = new LinkedList<>();

    }


    // Metodo per aggiungere un arco nel grafo non orientato
    public void addEdge(int v, int w, int weight) {
        // Controllo che l'arco non esiste già
        if(adjList[v].stream().filter(x -> x.destination==w).toList().size()==0){
            adjList[v].add(new EdgeLinkedList(w,weight)); // Aggiunge l'arco verso w alla lista di adiacenza di v
            adjList[w].add(new EdgeLinkedList(v,weight)); // Aggiunge l'arco verso v alla lista di adiacenza di w
        }/*else {
            throw new RuntimeException("arco gli esistente");
        }*/
    }


    // Metodo per aggiungere un nodo al grafo, si puo utilizzare solo se prima è stata effetuata una espansione della lista linkata degli archi
    public void addNode(int lable, int type){
        if(nodeList.stream().filter(x -> x.label==lable).count()==0) {
            nodeList.add(new Node(lable, type,0));
        }else {
            throw new RuntimeException("nodo gli esistente");
        }
    }


    // Metdo che permette di aumentare il numero di nodi da cui è formato il grafo
    public void resize(int newSize) {
        LinkedList<EdgeLinkedList>[] newAdjList = new LinkedList[newSize];
        for (int i = 0; i < adjList.length; i++) {
            newAdjList[i] = adjList[i];
        }
        for (int i = adjList.length; i < newSize; i++) {
            newAdjList[i] = new LinkedList<>();
        }
        this.adjList = newAdjList;
        V= newSize;
    }


    // Metodo per stampare il grafo
    public void printGraph() {
        System.out.println("Lista dei nodi:");
        for (Node n: this.nodeList) {
            System.out.print(n.label+"("+n.type+"), ");
        }
        System.out.println("\n");

        for (Node n: nodeList) {
            System.out.println("Lista di adiacenza del vertice " + n.label + ":");
            System.out.print("Head");
            for (EdgeLinkedList edge : adjList[n.label]) {
                System.out.print(" -> (n:" + edge.destination+", w:"+ edge.weight+")");
            }
            System.out.print("\n");
        }
    }


    // Metodo per trovare il percorso minimo da un nodo sorgente a un nodo destinazione utilizzando l'algoritmo di Dijkstra
    private int[] dijkstra(int source, int destination) {
        // Array per salvare le distanze minime dal nodo sorgente
        int[] distance = new int[V];
        // Setto tutti i valori a "piu infinito"
        Arrays.fill(distance, Integer.MAX_VALUE);

        // Array dei padri di ogni nodo
        int[] father= new int[V+1];
        father[source]= -1;

        // Array per tenere traccia dei nodi visitati
        boolean[] visited = new boolean[V];

        // Coda per tenere traccia dei nodi da visitare
        PriorityQueue<Node> pq = new PriorityQueue<>(new Node());

        // Aggiungiamo il nodo sorgente alla coda
        pq.add(new Node(source, 0));
        distance[source] = 0;

        while (!pq.isEmpty()) {
            int u = pq.poll().label;

            // Segna il nodo corrente come visitato
            visited[u] = true;

            // Se raggiungiamo la destinazione, possiamo restituire la distanza minima
            if (u == destination) {
                // Setto il valore del percorso ci costo minimo dal nodo source a destination nella posizione V del vettore
                father[V]= distance[u];
                return father;
            }
            // Controlla tutti i vicini di u
            for (EdgeLinkedList neighbor : adjList[u]) {
                int v = neighbor.destination;
                int weight = neighbor.weight;

                // Se il nodo non è stato visitato e il nuovo percorso è più breve, aggiorna la distanza minima
                if (!visited[v] && distance[u] + weight < distance[v]) {
                    distance[v] = distance[u] + weight;
                    pq.add(new Node(v, distance[v]));
                    father[v]=u;
                }
            }
        }

        // Se il nodo destinazione non è raggiungibile
        return null;
    }


    // Restituisce la sequenza dei nodi che compongono il cammino ottimo dal nodo start a end
    public ArrayList<Integer> solutionPathDijkstra(int start,int end){
        int[] father = dijkstra(start,end);

        ArrayList<Integer> solutionPath = new ArrayList<>();
        int node = end;
        while (node!=-1){
            solutionPath.add(0,node);
            node=father[node];
        }
        return solutionPath;
    }

    // Restituisce il valore del cammino minimo dal nodo start a end
    public int optimalValueDijkstra(int start, int end){
        if(start==end)
            return 0;
        int[] father = dijkstra(start,end);
        return father[V];
    }


    // Dijikastra uno a tutti
    private int[] dijkstraOneToAll(int source) {
        // Array per salvare le distanze minime dal nodo sorgente
        int[] distance = new int[V];

        // Setto tutti i valori a "piu infinito"
        Arrays.fill(distance, Integer.MAX_VALUE);

        // Array dei padri di ogni nodo
        int[] father= new int[V];
        father[source]= -1;

        // Array per tenere traccia dei nodi visitati
        boolean[] visited = new boolean[V];

        // Coda per tenere traccia dei nodi da visitare
        PriorityQueue<Node> pq = new PriorityQueue<>(new Node());

        // Aggiungiamo il nodo sorgente alla coda
        pq.add(new Node(source, 0));
        distance[source] = 0;

        while (!pq.isEmpty()) {
            int u = pq.poll().label;

            // Segna il nodo corrente come visitato
            visited[u] = true;

            // Controlla tutti i vicini di u
            for (EdgeLinkedList neighbor : adjList[u]) {
                int v = neighbor.destination;
                int weight = neighbor.weight;

                // Se il nodo non è stato visitato e il nuovo percorso è più breve, aggiorna la distanza minima
                if (!visited[v] && distance[u] + weight < distance[v]) {
                    distance[v] = distance[u] + weight;
                    pq.add(new Node(v, distance[v]));
                    father[v]=u;
                }
            }
        }

        //restituisco tutte le distanze dagli altri nodi
        return distance;
    }


    /*
    // Crea un grafo ausiliario G' di G, il quale è un grafo completo dove ogni arco (u,v) ha come costo il valore ottimo del cammino dal nodo u a v nel G.
    public Graph GraphOptimalPath(){
        List<Node> nodeListG= ((ArrayList<Node>) this.nodeList.clone()).stream().filter(x -> x.type!=0).toList();
        Graph graphA= new Graph(V,new ArrayList<>(nodeListG));
        int[] distance;

        //O(n*m+n^2*log(n))
        for (Node n: nodeListG) {
            //O(m+n*log(n))
            distance= this.dijkstraOneToAll(n.label);

            //O(n)
            for(Node f: nodeListG){
                if(f.label!=n.label)
                    graphA.addEdge(n.label, f.label, distance[f.label]);
            }
        }
        return graphA;
    }
*/

    private int[][] floydWarshall() {
        int[][] dist = new int[V][V];

        for (int i = 0; i < V; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }

        for (int i = 0; i < V; i++) {
            for (EdgeLinkedList edge : adjList[i]) {
                dist[i][edge.destination] = edge.weight;
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    public Graph GraphOptimalPath() {
        int[][] shortestPaths = floydWarshall();
        List<Node> nodeListG= ((ArrayList<Node>) this.nodeList.clone()).stream().filter(x -> x.type!=0 ).toList();
        Graph auxiliaryGraph= new Graph(V,new ArrayList<>(nodeListG));

        for (Node n: nodeListG) {
            for(Node f: nodeListG){
                if(f.label!=n.label)
                    auxiliaryGraph.addEdge(n.label, f.label, shortestPaths[n.label][f.label]);
            }
        }

        return auxiliaryGraph;
    }


    public ArrayList<Edge> getAllEdges() {
        ArrayList<Edge> edges = new ArrayList<>();
        boolean[][] visited = new boolean[V][V];

        for (Node n : this.nodeList) {
            for (EdgeLinkedList edge : adjList[n.label]) {
                if (!visited[n.label][edge.destination] && !visited[edge.destination][n.label]) {
                    edges.add(new Edge(n.label, edge.destination, edge.weight));
                    visited[n.label][edge.destination] = true;
                    visited[edge.destination][n.label] = true;
                }
            }
        }

        return edges;
    }

    public ArrayList<Edge> sortEdges() {
        ArrayList<Edge> edges = getAllEdges();
        edges.sort(Comparator.comparingInt((Edge t) -> t.getWeight())
                .thenComparingInt(t -> t.getNode2())
                .thenComparing((Edge t) -> t.getNode1(), Comparator.reverseOrder()));
        return edges;
    }


    // Questo metodo restituisce k-cluseter di un grafo, dove ad ogni k è associato una capienza [1-5] e ogni nodi del groafo un peso [0-10].
    // Precondizione il numero dei pesi è uguale alla somma delle k capienze.
    // Ogni punto dell array è una vettura, e il valore rappresenta il suo rappresentativo e il numero di posti disponibili.
    // Il peso di ogni arco rappresetna il numero di persone in quel punto.

    // Versione in cui ogni punto puo avere piu di una persona

    /*
    public static List<Set<Integer>> K_Clustering(Graph graph, Point[] cars){
        // Rappresenta i cluster
        Map<Integer, Set<Integer>> clusterMap = new HashMap<>();
        List<Set<Integer>> tempCluster;
        boolean b;

        // Numero di unioni da effettuare
        int k;

        // Lable nodi aggiunti
        Point[] addNode;

        // Espando il grafo
        addNode= graph.expandGraph();

        // Ordino tutti gli archi
        ArrayList<Edge> sortEdge = graph.sortEdges();

        // Ordino le cars
        Graph.sortPointsByYDescending(cars);

        UnionFind<Integer> nodeUnionFind= new UnionFind<>(graph.nodeList.stream().map(x -> x.label).collect(Collectors.toSet()));

        // Setto il valore di k come la differenza del numero di nodi meno il numero di cluster
        k= graph.nodeList.size()-cars.length;
        for(Edge e: sortEdge){
            // Reset hashmap
            clusterMap = new HashMap<>();

            if(!nodeUnionFind.inSameSet(e.x,e.y)){

                // Trasformo la mappa in k set
                for (Node n : graph.nodeList) {
                    int root = nodeUnionFind.find(n.label);
                    clusterMap.putIfAbsent(root, new HashSet<>());
                    clusterMap.get(root).add(n.label);
                }
                tempCluster= new ArrayList<>(clusterMap.values());


                // Unisco le due liste dei cluster che dovrebbero essere uniti
                Set<Integer> u= null,v= null;
                for(Set<Integer> set: tempCluster){
                    if(set.contains(e.x))
                        u=set;
                    if(set.contains(e.y))
                        v=set;
                }
                u.addAll(v);
                tempCluster.remove(v);


                // Sort dei cluster in base alla size
                tempCluster.sort(Comparator.comparingInt((Set<Integer> set) -> set.size()).reversed());

                int i=0;
                for (Set<Integer> cluster : tempCluster) {
                //    System.out.println("Cluster " + i + " : " + cluster);
                    i++;
                }
                //System.out.println();

                b=true;
                // Controllo che l'assegamento delle persone alle cars sia valido
                int j=0;
                for(Point car : cars){
                //    System.out.println("car.y: "+car.y+" tempCluster.get(j).size(): "+tempCluster.get(j).size());
                //    System.out.println();
                    if(car.y < tempCluster.get(j).size())
                        b=false;
                    j++;
                }

                //System.out.println("valore b:"+ b);
                //System.out.println();

                // Unisco i due cluster nel caso in cui la loro unione non genera unb cluster troppo grande per una car
                if(b) {
                    nodeUnionFind.union(nodeUnionFind.find(e.x), nodeUnionFind.find(e.y));
                    k--;
                }
            }
            if(k==0)
                break;
        }

        // Trasformo la mappa in k set
        clusterMap = new HashMap<>();
        for (Node n : graph.nodeList) {
            int root = nodeUnionFind.find(n.label);
            clusterMap.putIfAbsent(root, new HashSet<>());
            clusterMap.get(root).add(n.label);
        }

        return new ArrayList<>(clusterMap.values());
    }


     */

    public Point[] expandGraph(){
        Point[] addNode = new Point[V];
        int pos=V;
        int Vtemp=V;
        int lastNode;
        Node n=null;

        // Determino il numero di nodi da aggiungere.
        int P=0;
        for(Node node: this.nodeList){
            if(node.type > 1)
                P+= node.type-1;
        }
        System.out.println("P: "+(V+P));

        // Incremento del numero di nodi del grafo
        this.resize(V+P);

        for (int i = 0; i < Vtemp; i++) {
            int finalI = i;

            if(!this.nodeList.stream().filter(x -> x.label== finalI).toList().isEmpty())
                n = this.nodeList.stream().filter(x -> x.label== finalI).toList().get(0);

            if(n!=null){
                // Il grafo contiene il nodo con quella etichetta
                // Setto il range dei nodi aggiunti per il nodo i
                if(n.type>1) {
                    addNode[i] = new Point(pos, pos + n.type - 2);

                    // Aggiungo i nodi e gli archi al grafo
                    lastNode=i;
                    for(int j=0; j < n.type-1; j++) {
                        this.addNode(pos+j,1);
                        this.addEdge(lastNode,pos+j,0);

                        // Aggiungo tutti gli archi verso il nodo n,lable pure a pos+j
                        for(EdgeLinkedList edge : this.adjList[n.label])
                            if(this.adjList[pos+j].stream().filter(x -> x.destination==edge.destination).toList().isEmpty() && edge.destination!=pos+j)
                                this.adjList[pos+j].add(edge);

                        lastNode=pos+j;
                    }

                    // Chiudo l'anello
                    this.addEdge(lastNode,i,0);

                    pos+=n.type-1;
                }
                else
                    addNode[i]=null;
            }else
                addNode[i]=null;
        }

        System.out.println("V: "+V);
        return addNode;
    }


    // Assegno ad ongi nodo del grafo un etichetta in ordine crescente partendo da 0
    public int[] TrnasformGraph() throws CloneNotSupportedException {
        int[] transform= new int[this.nodeList.size()];
        int[] transformI= new int[V];
        ArrayList<Node> nodeList= new ArrayList<>();
        Graph transformedGraph;
        Node copy;

        int i=0;
        for(Node n: this.nodeList){
            transform[i]= n.label;
            transformI[n.label]= i;
            copy=(Node)n.clone();
            copy.label=i;
            //copy.type= 1;
            nodeList.add(copy);
            i++;
        }

        transformedGraph= new Graph(nodeList.size(),nodeList);

        for(Node n: this.nodeList){
            for (EdgeLinkedList edgeLinkedList : this.adjList[n.label])
                transformedGraph.addEdge(transformI[n.label],transformI[edgeLinkedList.destination],edgeLinkedList.weight);
        }

        this.V=transformedGraph.V;
        this.adjList=transformedGraph.adjList;
        this.nodeList=transformedGraph.nodeList;

        return transform;
    }


    //
    public static List<Cluster> Clustering(Graph graph, Point[] cars) {
        // Set dei nodi assegnati alle cars
        List<Cluster> clusters= new ArrayList<Cluster>();

        for(int i=0; i<cars.length; i++)
            clusters.add(new Cluster(cars[i].x,0));

        // Espando il grafo
        Point[] addNode= graph.expandGraph();

        // Ordino le cars in ordine decrescente di numero di posti
        Graph.sortPointsByYDescending(cars);

        // Lista dei nodi assegnati
        boolean[] nodiAssegnati= new boolean[graph.V];
        for(int i=0;i<graph.V;i++)
            nodiAssegnati[i] = false;

        // Lista deigli archi ordinati
        ArrayList<Edge> sortEdge = graph.sortEdges();

        // Ciclo su ogni arco
        for(Edge e: sortEdge){
            // Controllo se il secondo estremo è una vattura e il primo è un nodo qualunque
            if(graph.nodeList.stream().filter(x -> x.label == e.getNode1()).toList().get(0).type==-2 && graph.nodeList.stream().filter(x -> x.label == e.getNode2()).toList().get(0).type!=-2) {
                // Controllo see il nodo y non è stato assegnato ad un cluster
                if (!nodiAssegnati[e.getNode2()]) {
                    int i=0;
                    for(Cluster cluster: clusters){
                        if (cluster.label==e.getNode1() && cars[i].y > cluster.cityIndices.size() && !nodiAssegnati[e.getNode2()]) {
                            cluster.cityIndices.add(e.getNode2());
                            nodiAssegnati[e.getNode2()] = true;
                        }
                        i++;
                    }
                }
            }else if(graph.nodeList.stream().filter(x -> x.label == e.getNode2()).toList().get(0).type==-2 && graph.nodeList.stream().filter(x -> x.label == e.getNode1()).toList().get(0).type!=-2){
                if (!nodiAssegnati[e.getNode1()]) {
                    // Se il nodo y non è stato assegnato ad un cluster
                    int i=0;
                    for(Cluster cluster: clusters){
                        if(cluster.label==e.getNode2() && cars[i].y > cluster.cityIndices.size()&& !nodiAssegnati[e.getNode1()]){
                            cluster.cityIndices.add(e.getNode1());
                            nodiAssegnati[e.getNode1()]= true;
                        }
                        i++;
                    }
                }
            }
        }

        return clusters;
    }



    // Metodo che Ordina una lista di punti in modo decrescente
    public static void sortPointsByYDescending(Point[] points) {
        Arrays.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                // Ordinamento per y in ordine decrescente
                return Integer.compare(p2.y, p1.y); // Invertiamo p2.y e p1.y per l'ordine decrescente
            }
        });
    }
}
