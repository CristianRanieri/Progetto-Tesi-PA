package ITSHA;

import Graph.Edge;
import Other.Cluster;
import Other.Solution;

import java.util.ArrayList;
import java.util.Random;

public class RGP {
    // Clustering di parenteze
    private int[] NC;
    // Vettore dei vicini di ogni città
    private ArrayList<Integer>[] CS;
    // Insieme degli archi della soluzione ottima
    private Solution Sbest;
    // Numero massimo di città di ogni cluster
    private int Cmax;
    // Numero di
    private int m;
    // Numero di città
    private int n;
    // Solzuoine nuova
    Solution S;
    // Lista degli archi
    ArrayList<Edge> edges;
    public RGP(int[] NC, ArrayList<Integer>[] CS, Solution sbest, int cmax, ArrayList<Edge> edges, int m) {
        this.NC = NC;
        this.CS = CS;
        this.edges=edges;
        Sbest = sbest;
        Cmax = cmax;
        this.m= m;
        n= NC.length;
        S= new Solution();
    }

    public Solution run (){
        //  di ogni venditore
        Cluster[] C= new Cluster[m];
        for(int i=0;i<m;i++)
            C[i]= new Cluster(i,10);

        for (int j=0;j<m;j++){
            C[j].addCityIndex(0);
            for(int i=1; i< n; i++){
                if(NC[i]==j)
                    C[j].addCityIndex(i);
            }
        }


        // Vettore Se di n elementi settati a 0
        int[] Se= new int[n];
        for (int i=0; i<n;i++)
            Se[i]=0;

        for (int j=0;j<m;j++){
            Se[0]=0;

            // Starting City, selezionata in modo randomico
            int sc;
            Random randomGen = new Random();
            int size = C[j].getCityIndices().size();
            int random= randomGen.nextInt(0,size);
            // Seleziono in modo randomico una città
            sc = C[j].getCityIndices().get(random);


            System.out.println("C["+j+"].size prima: "+C[j].getCityIndices().size());


            // Rimuovo la citta random
            C[j].getCityIndices().remove((Object)sc);
            // Setto che la citta con indice sc è stata selezionata
            Se[sc]=1;

            System.out.println("C["+j+"].size dopo: "+C[j].getCityIndices().size());

            // Current City
            int cc= sc;

            do {
                // Lista di città
                ArrayList<Integer> AC= new ArrayList<>();

                // Città collegate dalla città cc nella soluzione
                int a1=0,a2=0;

                if(Sbest.getValue() != Integer.MAX_VALUE){
                    for(int i=0;i<Sbest.getEdges().size();i++){
                        if(Sbest.getEdges().get(i).getNode2() == cc)
                            a1= Sbest.getEdges().get(i).getNode1();
                        if(Sbest.getEdges().get(i).getNode1() == cc)
                            a2= Sbest.getEdges().get(i).getNode2();
                    }

                    if(NC[a1]==j && Se[a1]==0)
                        AC.add(a1);
                    if(NC[a2]==j && Se[a2]==0)
                        AC.add(a2);
                }

                if(AC.size()==0){
                    for (Integer k : CS[cc])
                        if((NC[k]==j || k==0 ) && Se[k]==0) {
                            AC.add(k);

                            System.out.println("aggiungo: "+k);
                        }
                }

                if(AC.size()==0){
                    for (Integer k : C[j].getCityIndices())
                        if((NC[k]==j || k==0 ) && Se[k]==0) {
                            AC.add(k);

                            System.out.println("aggiungo: "+k);
                        }
                }

                // Seleziono in modo randomico la Next City
                int nc;

                //System.out.println("AC.size="+AC.size());


                random= randomGen.nextInt(0,AC.size());
                nc= AC.get(random);
                Se[nc]= 1;


                System.out.println("C["+j+"].size2 prima: "+C[j].getCityIndices().size());


                // Rimuovo la città con indice nc dal cluster C[j]
                C[j].getCityIndices().remove((Object)nc);


                System.out.println("C["+j+"].size2 dopo: "+C[j].getCityIndices().size());


                // Aggiungo il nuovo arco alla soluzione dalla citta cc a nc
                int finalCc = cc;
                Edge e= edges.stream().filter(x-> x.equals(new Edge(finalCc,nc,0))).findFirst().get();
                S.addEdge(e);

                cc=nc;
            }while (C[j].getCityIndices().size()!=0);

            // Aggiungo il nuovo arco alla soluzione dalla citta cc a sc
            int finalCc1 = cc;
            Edge e= edges.stream().filter(x-> x.equals(new Edge(finalCc1,sc,0))).findFirst().get();
            S.addEdge(e);
        }
        return S;
    }


    public static void main(String[] args) {
        int[] NC = new int[15];
        /*
        for (int i = 0; i < NC.length; i++) {
            NC[i] = new Random().nextInt(3); // Populating NC with random values 0, 1, or 2
            System.out.println("NC["+i+"]: "+NC[i]);
        }
         */
        NC[0]=0;
        NC[1]=0;
        NC[2]=1;
        NC[3]=1;
        NC[4]=2;
        NC[5]=2;
        NC[6]=2;
        NC[7]=2;
        NC[8]=2;
        NC[9]=2;
        NC[10]=1;
        NC[11]=1;
        NC[12]=0;
        NC[13]=2;
        NC[14]=1;

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

        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (i != j) {
                    edges.add(new Edge(i, j, random.nextInt(1,15)));
                }
            }
        }

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


        RGP rgp= new RGP(NC,CS,Sbest,Cmax,edges,3);
        Solution S=rgp.run();

        System.out.println(S);
    }
}
