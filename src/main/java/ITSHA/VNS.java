package ITSHA;

import Graph.*;
import Other.Solution;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class VNS {

    public static Solution VNSrun(Solution S, ArrayList<Integer>[] CS, int Cmax, int[] p, Point[] range, int[] transform, int numNode, int numC, int dimGraph, ArrayList<Edge> edges) throws CloneNotSupportedException {
        S= (Solution) S.clone();
        Solution Sold= (Solution) S.clone();
        double valS,valSold;

        do {
            Sold = VNS.Insert(Sold, CS, Cmax, p, range, transform, numNode, numC, dimGraph, edges);
            Sold = VNS.Swap(Sold, CS, Cmax, p, range, transform, numNode, numC, dimGraph, edges);
            S = VNS.O_Pt(Sold, CS, Cmax, p, range, transform, numNode, numC, dimGraph, edges);

            valS= Solution.valueSolutionTransformed(S.transformSolution(range, transform, numNode, numC, dimGraph), edges);
            valSold= Solution.valueSolutionTransformed(Sold.transformSolution(range, transform, numNode, numC, dimGraph), edges);

            if(valS < valSold)
                Sold=S;

        }while (valS != valSold);


        return S;
    }

    private static Solution O_Pt(Solution Sold, ArrayList<Integer>[] CS, int Cmax, int[] p,Point[] range,int[] transform, int numNode, int numC, int dimGraph, ArrayList<Edge> edges) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("Iniszio iterazione Two_pt");
        System.out.println();

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

                for(int i=0;i<numC;i++){
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

        System.out.println();
        System.out.println("Iniszio iterazione Insert");
        System.out.println();

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

                for (int i = 0; i < numC; i++) {
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

                        // Cerco t3 all'interno del tour0, succesivo al nodo t4
                        for (int j = indexT4+1; j < sequenceSol[tour0].size() - 2; j++) {
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


    private static Solution Swap(Solution Sold, ArrayList<Integer>[] CS, int Cmax, int[] p,Point[] range,int[] transform, int numNode, int numC, int dimGraph, ArrayList<Edge> edges) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("Iniszio iterazione Swap");
        System.out.println();

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
            int indexT1 = random.nextInt(1, sequenceSol[tour0].size() - 3);
            int t1 = sequenceSol[tour0].get(indexT1);
            int t0 = sequenceSol[tour0].get(indexT1 + 1);
            int t3 = -1;
            int t5 = -1;
            int t7 = -1;

            for(int t2: CS[t1]) {
                int tour = -1;
                int indexT3 = -1;

                for (int i = 0; i < numC; i++) {
                    for (int j = 2; j < sequenceSol[i].size() - 3; j++) {
                        if (sequenceSol[i].get(j) == t2 && i != tour0) {
                            tour = i;
                            indexT3 = j - 1;
                            t3 = sequenceSol[i].get(j - 1);
                            break;
                        }
                    }
                }

                // t2 Valido
                if (tour != -1) {
                    int indexT4 = -1;
                    t5 = -1;

                    for (int t4 : CS[t3]) {
                        indexT4 = -1;

                        // Cerco t4 all'interno del tour0, succesivo al nodo t1
                        for (int j = indexT1 + 1; j < sequenceSol[tour0].size() - 2; j++) {
                            if (sequenceSol[tour0].get(j) == t4) {
                                indexT4 = j;
                                t5 = sequenceSol[tour0].get(j + 1);
                                break;
                            }
                        }

                        // t4 valido
                        if (indexT4 != -1) {
                            int indexT6 = -1;
                            t7 = -1;

                            for (int t6 : CS[t5]) {
                                indexT6 = -1;

                                // Cerco t6 all'interno del tour, succesivo al nodo t3
                                for (int j = indexT3 + 1; j < sequenceSol[tour].size() - 2; j++) {
                                    if (sequenceSol[tour].get(j) == t6) {
                                        indexT6 = j;
                                        t7 = sequenceSol[tour].get(j + 1);
                                        break;
                                    }
                                }

                                // t6 valido
                                if (indexT6 != -1) {
                                    // Resetto la sequenze delle soluzioni
                                    for(int i=0;i<numC;i++)
                                        sequenceSolTemp[i] = new ArrayList<>();

                                    // Primo terzo del tuor0, dal nodo 0 a indext1, tour0
                                    for(int i=0; i <= indexT1; i++)
                                        sequenceSolTemp[tour0].add(sequenceSol[tour0].get(i));

                                    // Secondo terzo del tour0, da indexT3+1 a indexT6, tour
                                    for(int i=indexT3+1; i <= indexT6; i++)
                                        sequenceSolTemp[tour0].add(sequenceSol[tour].get(i));

                                    // Tero terzo del tour0, da indexT4+1 a 0 , tour0
                                    for(int i=indexT4+1; i < sequenceSol[tour0].size(); i++)
                                        sequenceSolTemp[tour0].add(sequenceSol[tour0].get(i));


                                    // Primo terzo del tour che contiene t2, da 0 a indexT3, dal tour
                                    for(int i=0; i <= indexT3; i++)
                                        sequenceSolTemp[tour].add(sequenceSol[tour].get(i));

                                    // Secondo terzo del tour che contiene t2, da indexT4 a indexT1+1, revers del tour0
                                    for(int i=indexT4; i >= indexT1+1; i--)
                                        sequenceSolTemp[tour].add(sequenceSol[tour0].get(i));

                                    // Terzo terzo del tour che contiene t2, da indexT1+1 a 0, dal tour
                                    for(int i=indexT6+1; i < sequenceSol[tour].size(); i++)
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
                                    System.out.println("t4 :"+t4);
                                    System.out.println("t5 :"+t5);
                                    System.out.println("t6 :"+t6);
                                    System.out.println("t7 :"+t7);

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
                                        int finalT5 = t5;
                                        int finalT7 = t7;
                                        tempSolution.edges= new ArrayList<Edge>(tempSolution.edges.stream().filter(e -> !e.equals(new Edge(t1,t0,0)) && !e.equals(new Edge(finalT3,t2 ,0)) && !e.equals(new Edge(t4, finalT5,0)) && !e.equals(new Edge(t6, finalT7,0))).toList());
                                        tempSolution.edges.add(new Edge(t1,t2,0));
                                        tempSolution.edges.add(new Edge(t6,t5,0));
                                        tempSolution.edges.add(new Edge(t3,t4,0));
                                        tempSolution.edges.add(new Edge(t0,t7,0));

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
            }
        }

        return Sbest;
    }

   public static Solution Adjust_Solution (Solution S, int[] p, Point[] range,int[] transform, int numNode, int numC, int dimGraph, int Ac){

        // Sequenza dei nodi dei tour della soluzione
        ArrayList<Integer>[] sequenceSol;

        Random randomGen= new Random();
        int tour0;
        int tour1;
        int positionRemoveNode;
        int positionAddNode;

        for(int i=0 ;i<Ac; i++){
            // Genero e aggiorno la sequenza dei nodi dei tour della soluzione
            sequenceSol = S.sequenceSolution(range, transform, numNode, numC, dimGraph);

            // Genero il tour dal quale eliminare il nodo;
            tour0= randomGen.nextInt(0,numC);

            if(sequenceSol[tour0].size() >= 4) {
                // Genero la posizione del nodo da eliminare del tour0
                positionRemoveNode = randomGen.nextInt(1, sequenceSol[tour0].size() - 1);

                // Genero il tour nel quale inserire il nodo
                tour1= randomGen.nextInt(0,numC);

                // Genero la posizione nel quale inserire il nodo nel nuovo tour1
                positionAddNode= randomGen.nextInt(0, sequenceSol[tour1].size() - 1);

                if((tour0 != tour1 || positionRemoveNode != positionAddNode+1 && positionRemoveNode != positionAddNode) && (tour0 == tour1 || sequenceSol[tour1].size()-2 < p[tour1])){
                    // I 2 tour sono diversi e hanno un numero di di posti occupati minore a quelli disponibili oppure
                    // I 2 tour sono uguali ma i due nodi sono in posizioni diverse

                    // Rimuovo gli archi nel tour0 tra i nodi in posizione PRN-1, PRN e PRN e PRN+1
                    // Rimuovo l'arco nel tour1 tra i nodi in posizione PAN, PAN+1
                    int finalTour0 = tour0;
                    int finalPositionRemoveNode = positionRemoveNode;
                    int finalTour1 = tour1;
                    int finalPositionAddNode = positionAddNode;
                    ArrayList<Integer>[] finalSequenceSol = sequenceSol;
                    S.edges = new ArrayList<Edge>(S.edges.stream().filter(e -> !e.equals(new Edge(finalSequenceSol[finalTour0].get(finalPositionRemoveNode -1), finalSequenceSol[finalTour0].get(finalPositionRemoveNode),0)) && !e.equals(new Edge(finalSequenceSol[finalTour0].get(finalPositionRemoveNode), finalSequenceSol[finalTour0].get(finalPositionRemoveNode+1) ,0)) && !e.equals(new Edge(finalSequenceSol[finalTour1].get(finalPositionAddNode), finalSequenceSol[finalTour1].get(finalPositionAddNode+1) ,0))).toList());

                    // Aggiungo l'arco nel tour0 tra i nodi PRN-1, PRN+1
                    S.edges.add(new Edge(sequenceSol[finalTour0].get(finalPositionRemoveNode-1),sequenceSol[finalTour0].get(finalPositionRemoveNode+1),0));

                    // Aggiungo gli archi nel tour1 tra i nodi in posizione PAN, PRN e PAN+1 e PRN
                    S.edges.add(new Edge(sequenceSol[finalTour1].get(finalPositionAddNode),sequenceSol[finalTour0].get(finalPositionRemoveNode),0));
                    S.edges.add(new Edge(sequenceSol[finalTour1].get(finalPositionAddNode+1),sequenceSol[finalTour0].get(finalPositionRemoveNode),0));

                    System.out.println();
                    System.out.println("tour0 :"+ tour0);
                    System.out.println("tour1 :"+ tour1);
                    System.out.println("positionRemoveNode :"+ positionRemoveNode);
                    System.out.println("positionAddNode :"+ positionAddNode);
                    System.out.println("nodo aggiunto :"+ sequenceSol[tour0].get(positionRemoveNode));
                    System.out.println();
                    System.out.println(S);
                }
            }
        }
        return S;
   }
}



