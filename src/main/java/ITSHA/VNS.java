package ITSHA;

import Graph.*;
import Other.Solution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class VNS {

    public static Solution VNSrun(Solution S, ArrayList<Integer>[] CS, int Cmax, int[] p, Point[] range, int[] transform, int numNode, int numC, int dimGraph, ArrayList<Edge> edges) throws CloneNotSupportedException {
        S= (Solution) S.clone();
        Solution Sold= (Solution) S.clone();
        double valS,valSold;

        do {
            Sold = VNS.Insert(Sold, CS, Cmax, p, range, transform, numNode, numC, dimGraph, edges);
            S = VNS.O_Pt(Sold, CS, Cmax, p, range, transform, numNode, numC, dimGraph, edges);

            valS= Solution.valueSolutionTransformed(S.transformSolution(range, transform, numNode, numC, dimGraph), edges);
            valSold= Solution.valueSolutionTransformed(S.transformSolution(range, transform, numNode, numC, dimGraph), edges);

            if(valS < valSold)
                Sold=S;

        }while (valS != valSold);

        return S;
    }

    private static Solution O_Pt(Solution Sold, ArrayList<Integer>[] CS, int Cmax, int[] p,Point[] range,int[] transform, int numNode, int numC, int dimGraph, ArrayList<Edge> edges) throws CloneNotSupportedException {

        // Genero la sequenza dei nodi dei tour della soluzione
        ArrayList<Integer>[] sequenceSol = Sold.sequenceSolution(range,transform,numNode,numC,dimGraph);



        // Nuova sequenza della solzuione
        ArrayList<Integer>[] sequenceSolTemp = new ArrayList[numC];

        // Soluzione migliore
        Solution Sbest= (Solution) Sold.clone();

        // Selesiono un numero casuale un nodo nel primo tour (t1)
        Random random= new Random();
        int tour0= random.nextInt(0, numC);

        if(sequenceSol[tour0].size()-2>1) {
            int randomInt= random.nextInt(1, sequenceSol[tour0].size() - 2);
            int t1= sequenceSol[tour0].get(randomInt);
            int t0= sequenceSol[tour0].get(randomInt+1);
            int t3=-1;

            for(int t2: CS[t1]){
                int tour=-1;

                for(int i=1;i<numC;i++){
                    for(int j=1;j<sequenceSol[i].size()-2;j++){
                        if(sequenceSol[i].get(j)==t2 && i!=tour0){
                            tour = i;
                            t3= sequenceSol[i].get(j+1);
                            break;
                        }
                    }
                }

                // t2 Valido
                if(tour!=-1){
                    // Resetto la sequenza della nuova soluzione con t1 e t2 scambiati
                    for(int i=0;i<numC;i++)
                        sequenceSolTemp[i] = new ArrayList<>();

                    // Prima meta del tuor0, dal nodo 0 a t1
                    for(int i=0; i<= randomInt; i++)
                        sequenceSolTemp[tour0].add(sequenceSol[tour0].get(i));

                    // Seconda meta del tour0, dal nodo t2 a 0, revers del tour che contiene t2
                    int indexT2 = 0;
                    for(int i=1;i<sequenceSol[tour].size();i++){
                        if(sequenceSol[tour].get(i)==t2)
                            indexT2=i;
                    }

                    for(int i=indexT2; i>=0; i--)
                        sequenceSolTemp[tour0].add(sequenceSol[tour].get(i));

                    // Prima meta del tour che contiene t2, da 0 a t0, revers del tour 0
                    for(int i=sequenceSol[tour0].size()-1;i>= randomInt+1;i--)
                        sequenceSolTemp[tour].add(sequenceSol[tour0].get(i));

                    // Seconda meta del tour che contiene t2, da t3 a 0, tuor che contiene t2
                    for(int i=indexT2+1;i<sequenceSol[tour].size();i++)
                        sequenceSolTemp[tour].add(sequenceSol[tour].get(i));

                    // Aggiungo tutti gli altri nodi
                    for(int i=0; i<numC; i++){
                        if(i!=tour && i!=tour0)
                            sequenceSolTemp[i]=sequenceSol[i];
                    }


                    System.out.println("Soluzione con:");
                    System.out.println("t1 :"+t1);
                    System.out.println("t0 :"+t0);
                    System.out.println("t2 :"+t2);
                    System.out.println("t3 :"+t3);

                    for(ArrayList<Integer> arrayList: sequenceSolTemp){
                        System.out.println();
                        for(Integer i: arrayList){
                            System.out.print(" "+i+"-");
                        }
                    }
                    System.out.println();
                    System.out.println();


                    boolean validSolution = true;
                    for(int i=0;i<numC;i++)
                        if(sequenceSolTemp[i].size()-2 > p[i])
                            validSolution = false;

                    if(validSolution){
                        Solution tempSolution = (Solution) Sold.clone();

                        int finalT3 = t3;
                        tempSolution.edges= new ArrayList<Edge>(tempSolution.edges.stream().filter(e -> !e.equals(new Edge(t1,t0,0)) && !e.equals(new Edge(t2,finalT3,0))).toList());
                        tempSolution.edges.add(new Edge(t1,t2,0));
                        tempSolution.edges.add(new Edge(t0,t3,0));

                        System.out.println(tempSolution);

                        if(Solution.valueSolutionTransformed(tempSolution.transformSolution(range, transform, numNode, numC, dimGraph), edges) < Solution.valueSolutionTransformed(Sbest.transformSolution(range, transform, numNode, numC, dimGraph), edges)) {
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println("VALORE CAMBIATO");
                            System.out.println();
                            System.out.println();
                            System.out.println();

                            Sbest = tempSolution;
                        }
                    }
                }
            }
        }

        return Sbest;
    }


    private static Solution Insert(Solution Sold, ArrayList<Integer>[] CS, int Cmax, int[] p,Point[] range,int[] transform, int numNode, int numC, int dimGraph, ArrayList<Edge> edges) throws CloneNotSupportedException {
        // Genero la sequenza dei nodi dei tour della soluzione
        ArrayList<Integer>[] sequenceSol = Sold.sequenceSolution(range, transform, numNode, numC, dimGraph);

        // Nuova sequenza della solzuione
        ArrayList<Integer>[] sequenceSolTemp = new ArrayList[numC];

        // Soluzione migliore
        Solution Sbest= (Solution) Sold.clone();

        // Selesiono un numero casuale un nodo nel primo tour (t1)
        Random random= new Random();
        int tour0= random.nextInt(0, numC);

        if(sequenceSol[tour0].size()-2>=4) {
            int indexT4= random.nextInt(1, sequenceSol[tour0].size() - 3);
            int t4= sequenceSol[tour0].get(indexT4);
            int t0= sequenceSol[tour0].get(indexT4+1);
            int t5=-1;
            int t2=-1;

            for(int t1: CS[t0]) {
                int tour = -1;
                int indexT1=-1;

                for (int i = 1; i < numC; i++) {
                    for (int j = 1; j < sequenceSol[i].size() - 2; j++) {
                        if (sequenceSol[i].get(j) == t1 && i != tour0) {
                            tour = i;
                            indexT1 = j;
                            t2 = sequenceSol[i].get(j + 1);
                            break;
                        }
                    }
                }

                // t1 Valido
                if (tour != -1) {
                    int indexT3= -1;
                    t5= -1;

                    for (int t3 : CS[t2]) {
                        indexT3= -1;

                        // Cerco t3 all'interno del tour0, succesivo al nodo t0
                        for (int j = indexT4+2; j < sequenceSol[tour0].size() - 2; j++) {
                            if (sequenceSol[tour0].get(j) == t3) {
                                indexT3= j;
                                t5 = sequenceSol[tour0].get(j+1);
                                break;
                            }
                        }

                        // t3 valido
                        if(indexT3!= -1){
                            // Resetto la sequenze delle soluzioni
                            for(int i=0;i<numC;i++)
                                sequenceSolTemp[i] = new ArrayList<>();

                            // Prima meta del tuor0, dal nodo 0 a indext4, tour0
                            for(int i=0; i <= indexT4; i++)
                                sequenceSolTemp[tour0].add(sequenceSol[tour0].get(i));

                            // Seconda meta del tour0, da indexT3+1 a 0, tour0
                            for(int i=indexT3+1; i < sequenceSol[tour0].size(); i++)
                                sequenceSolTemp[tour0].add(sequenceSol[tour0].get(i));

                            // Primo terzo del tour che contiene t1, da 0 a indexT1, dal tour
                            for(int i=0; i <= indexT1;i++)
                                sequenceSolTemp[tour].add(sequenceSol[tour].get(i));

                            // Secondo terzo del tour che contiene t1, da indexT4+1 a indexT3, dal tour0
                            for(int i=indexT4+1; i <= indexT3; i++)
                                sequenceSolTemp[tour].add(sequenceSol[tour0].get(i));

                            // Terzo terzo del tour che contiene t1, da indexT1+1 a 0, dal tour
                            for(int i=indexT1+1; i < sequenceSol[tour].size(); i++)
                                sequenceSolTemp[tour].add(sequenceSol[tour].get(i));

                            // Aggiungo tutti gli altri nodi
                            for(int i=0; i<numC; i++){
                                if(i!=tour && i!=tour0)
                                    sequenceSolTemp[i]=sequenceSol[i];
                            }

                            System.out.println("Soluzione con:");
                            System.out.println("t4 :"+t4);
                            System.out.println("t0 :"+t0);
                            System.out.println("t1 :"+t1);
                            System.out.println("t2 :"+t2);
                            System.out.println("t3 :"+t3);
                            System.out.println("t5 :"+t5);
                            System.out.println("indexT3 :"+indexT1);
                            System.out.println("indexT3 :"+indexT3);

                            for(ArrayList<Integer> arrayList: sequenceSolTemp){
                                System.out.println();
                                for(Integer i: arrayList){
                                    System.out.print(" "+i+"-");
                                }
                            }
                            System.out.println();
                            System.out.println();


                            boolean validSolution = true;
                            for(int i=0;i<numC;i++)
                                if(sequenceSolTemp[i].size()-2 > p[i])
                                    validSolution = false;

                            if(validSolution){
                                Solution tempSolution = (Solution) Sold.clone();

                                int finalT5 = t5;
                                int finalT = t2;
                                tempSolution.edges= new ArrayList<Edge>(tempSolution.edges.stream().filter(e -> !e.equals(new Edge(t4,t0,0)) && !e.equals(new Edge(t3, finalT5,0)) && !e.equals(new Edge(t1, finalT,0))).toList());
                                tempSolution.edges.add(new Edge(t4,t5,0));
                                tempSolution.edges.add(new Edge(t1,t0,0));
                                tempSolution.edges.add(new Edge(t2,t3,0));

                                System.out.println(tempSolution);

                                if(Solution.valueSolutionTransformed(tempSolution.transformSolution(range, transform, numNode, numC, dimGraph), edges) < Solution.valueSolutionTransformed(Sbest.transformSolution(range, transform, numNode, numC, dimGraph), edges)) {
                                    System.out.println();
                                    System.out.println();
                                    System.out.println();
                                    System.out.println("VALORE CAMBIATO");
                                    System.out.println();
                                    System.out.println();
                                    System.out.println();

                                    Sbest = tempSolution;
                                }
                            }
                        }
                    }
                }
            }
        }

        return Sbest;
    }
}
