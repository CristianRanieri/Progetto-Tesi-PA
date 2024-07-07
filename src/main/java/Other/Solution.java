package Other;


import Graph.*;
import java.awt.*;
import java.util.ArrayList;

public class Solution implements Cloneable {
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




    public ArrayList<Point>[] transformSolution(Point[] range,int[] transform, int numNode, int numC, int dimGraph){
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

        //System.out.println("Soluzione pre trasformazioe");

        for(ArrayList<Integer> arrayList: sequenceSol){
            System.out.println();
            for(Integer i: arrayList){
                System.out.print(" "+i+"-");
            }
        }
        System.out.println();


        //System.out.println("Soluzione post trasformazioe");

        // pre trasformale la soluzione per il grafo iniziale
        ArrayList<Point>[] sequenceSolTra = new ArrayList[numC];
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
                                sequenceSolTra[i].add(new Point(j,1));
                                remove.add(j);
                                added=true;
                            }else {
                                int finalJ = j;
                                sequenceSolTra[i].stream().filter(y-> y.x== finalJ).findFirst().get().y+=1;
                            }
                }

                if(!added){
                    if (!remove.contains(x)) {
                        if(x < dimGraph) {
                            sequenceSolTra[i].add(new Point(x, 1));
                            remove.add(x);
                        }
                    }else {
                        sequenceSolTra[i].stream().filter(y-> y.x== x).findFirst().get().y+=1;
                    }
                }
            }
            sequenceSolTra[i].add(new Point(0,1));
        }

        /*
        for(ArrayList<Point> arrayList: sequenceSolTra){
            System.out.println();
            for(Point i: arrayList){
                System.out.print(" ["+i.x+","+i.y+"]-");
            }
        }
        System.out.println();

         */

        return sequenceSolTra;
    }

    public static float valueSolutionTransformed(ArrayList<Point>[] solution, ArrayList<Edge> edges){
        float val=0;
        int nodePrec;
        int nodeNext;

        for(int i=0;i<solution.length;i++){
            nodePrec= 0;
            for(int j=1;j<solution[i].size();j++){
                nodeNext=(int) solution[i].get(j).getX();
                int finalNodeNext = nodeNext;
                int finalNodePrec = nodePrec;
                //System.out.print(" ["+nodePrec+", "+nodeNext+"] ");
                val+=edges.stream().filter(e -> e.equals(new Edge(finalNodePrec, finalNodeNext,0))).findFirst().get().weight;
                nodePrec=nodeNext;
            }
        }

        return val;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Solution cloned = (Solution) super.clone();
        cloned.edges = new ArrayList<>();
        for (Edge edge : this.edges) {
            cloned.addEdge((Edge) edge.clone());
        }
        return cloned;
    }
}
