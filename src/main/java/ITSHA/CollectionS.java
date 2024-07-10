package ITSHA;

import Graph.*;
import Other.Solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionS {

    public static ArrayList<Integer>[] Initialize_Candidates(int Cmax, Graph graph) {
        int dimGraph = graph.V;
        List<EdgeLinkedList > edges;
        ArrayList<Integer>[] CS = new ArrayList[dimGraph];

        for (int i = 0; i < dimGraph; i++) {
            CS[i] = new ArrayList<>();
            edges = graph.adjList[i].stream().sorted(Comparator.comparingInt(edge -> edge.weight)).collect(Collectors.toList());
            while (CS[i].size() < Cmax) {
                for(EdgeLinkedList edge : edges)
                    CS[i].add(edge.destination);
            }
        }

        return CS;
    }

    public static void Adjust_Candidates(ArrayList<Integer>[] CS, Solution S, Solution Sbetter){
        int i,j;

        for(Edge e1: S.edges){
            for(Edge e2: Sbetter.edges){
                // Controllo se i due archi sono uguali
                if(e1.equals(e2)){
                    i= e1.node1;
                    j= e1.node2;
                    // Controllo se j appartiene a CS[i]
                    if(!CS[i].contains(j)) {
                        /*
                        for (Integer c: CS[i]) {
                            System.out.print(c+", ");
                        }
                         */

                        // Rimuovo il l'ultimo elemento di CS[i]
                        CS[i].remove(CS[i].get(CS[i].size() - 1));

                        // Aggiungi j all'ultima posizione di CS[i]
                        CS[i].add(CS[i].size(), j);

                        /*
                        System.out.println();
                        System.out.println("dopo la tra");
                        for (Integer c: CS[i]) {
                            System.out.print(c+", ");
                        }
                        System.out.println();
                        System.out.println();
                         */
                    }
                }
            }
        }
    }
}
