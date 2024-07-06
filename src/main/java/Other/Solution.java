package Other;


import Graph.*;
import java.awt.*;
import java.util.ArrayList;

public class Solution {
    public ArrayList<Edge> edges;
    public float value;

    public Solution() {
        this.edges = new ArrayList<Edge>();
        this.value = 0;
    }

    public void addEdge(Edge edge){
        if(edges.stream().filter(x-> x.equals(edge)).toList().size()==0) {
            edges.add(edge);
            value += edge.getWeight();
        }
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String
    toString() {
        String s= "{" +
                "edges=";
        for(Edge edge : edges)
            s+=edge.toString();
        return s+"value="+value;
    }






    public void transformSolution(Point[] range,int[] transform, int dimGrph){
        boolean nodeChange1;
        boolean nodeChange2;

        // Porto le etichette dei nodi nella loro forma estesa
        for(Edge edge: edges){
            edge.node1=transform[edge.node1];
            edge.node2=transform[edge.node2];
        }

        // Sostituisco i nodi estesi con il loro nodo associato
        for(Edge edge: edges) {
            nodeChange1= false;
            nodeChange2= false;

            for (int i = 0; i < dimGrph; i++) {
                if (range[i] != null && edge.node1 >= range[i].x && edge.node1 <= range[i].y && !nodeChange1) {
                    edge.node1 = i;
                    nodeChange1=true;
                }
                if (range[i] != null &&  edge.node2 >= range[i].x && edge.node2 <= range[i].y && !nodeChange2) {
                    edge.node2 = i;
                    nodeChange2=true;
                }
            }
        }

        // Rimuovo gli tra due nodi uguali
        edges= new ArrayList<>( edges.stream().filter(x-> x.node1!=x.node2).toList());

        System.out.println("Valore pre trasformazione:"+value);
        value= edges.stream().map(x-> x.weight).reduce(0, Integer::sum);
        System.out.println("Valore post trasformazione:"+value);
    }


    public Solution[] transformSolution2(Point[] range,int[] transform, int numNode, int numC, int dimGraph){
        Solution[] solutionTrans= new Solution[numC];
        for(int i=0;i<numC;i++)
            solutionTrans[i]= new Solution();

        ArrayList<Node> node= new ArrayList<>();
        for(int i=0;i<numNode;i++)
            node.add(new Node(i,0));

        ArrayList<EdgeLinkedList> edgeVisited= new ArrayList<>();

        Graph graph = new Graph(numNode,node);
        for(Edge edge: edges)
            graph.addEdge(edge.node1,edge.node2,edge.weight);

        // Nodo di partenza
        int nodeNext;
        int nodePre = -1;
        boolean find=false;

        for(int i=0;i<numC;i++){
            nodeNext=-1;
            find=false;

            for(EdgeLinkedList edge: graph.adjList[0]){
                if(!edgeVisited.contains(edge) && !find){
                    nodeNext=edge.destination;
                    edgeVisited.add(edge);
                    solutionTrans[i].edges.add(new Edge(0,nodeNext,edge.weight));
                    nodePre=0;
                    find=true;
                }
            }

            // esiste ancora un arco che parte da 0 non visitato
            if(nodeNext!=-1){
                do{
                    int finalNodePre = nodePre;
                    EdgeLinkedList edgeNext= graph.adjList[nodeNext].stream().filter(x-> x.destination!= finalNodePre).toList().get(0);

                    nodePre=nodeNext;
                    nodeNext= edgeNext.destination;

                    if(nodeNext==0) {
                        int finalNodePre1 = nodePre;
                        edgeVisited.add(graph.adjList[0].stream().filter(x-> x.destination== finalNodePre1).toList().get(0));
                    }

                    solutionTrans[i].edges.add(new Edge(nodeNext,nodePre,graph.adjList[nodeNext].getFirst().weight));
                }while (nodeNext!=0);

            }
        }

        ArrayList<Integer>[] sequenceSol = new ArrayList[numC];
        for(int i=0;i<numC;i++) {
            sequenceSol[i] = new ArrayList<>();
            sequenceSol[i].add(0);

            int prec=0;
            for(Edge e: solutionTrans[i].edges){
                if(e.node1!=prec){
                    sequenceSol[i].add(e.node1);
                    prec=e.node1;
                }else if(e.node2!=prec){
                    sequenceSol[i].add(e.node2);
                    prec=e.node2;
                }
            }

        }

        System.out.println("Soluzione pre trasformazioe");

        for(ArrayList<Integer> arrayList: sequenceSol){
            System.out.println();
            for(Integer i: arrayList){
                System.out.print(" "+i+"-");
            }
        }
        System.out.println();


        System.out.println("Soluzione post trasformazioe");

        // pre trasformale la soluzione per il grafo iniziale
        ArrayList<Integer>[] sequenceSolTra = new ArrayList[numC];
        ArrayList<Integer> remove;

        for(int i=0;i<numC;i++) {
            sequenceSolTra[i] = new ArrayList<>();
            remove= new ArrayList<>();
            boolean added= false;


            for(Integer a: sequenceSol[i]){
                int x = transform[a];
                added=false;

                for (int j = 0; j < dimGraph; j++) {
                    if (range[j] != null && !added)
                        if (x >= range[j].x && x <= range[j].y)
                            if (!remove.contains(j)) {
                                sequenceSolTra[i].add(j);
                                remove.add(j);
                                added=true;
                            }
                }

                if(!added){
                    if (!remove.contains(x) && x < dimGraph) {
                        sequenceSolTra[i].add(x);
                        remove.add(x);
                    }
                }
            }
            sequenceSolTra[i].add(0);
        }

        for(ArrayList<Integer> arrayList: sequenceSolTra){
            System.out.println();
            for(Integer i: arrayList){
                System.out.print(" "+i+"-");
            }
        }
        System.out.println();

        return solutionTrans;
    }

}
