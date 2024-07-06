package ITSHA;

import Other.Cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VNSInITSHA {

    public static void main(String[] args) {
        // Esempio di inizializzazione dei dati
        // S sarebbe NC
        List<Cluster> S = initializeSolution(); // Soluzione iniziale come array di cluster
        List<List<Integer>> CS = initializeCandidateSets(); // Inizializzazione dei candidati (esempio)
        int Cmax = 3; // Numero massimo di città candidate
        int p = 5; // Numero massimo di città che possono essere visitate da qualsiasi venditore

        // Implementazione VNS in ITSHA
        List<Cluster> Sold;
        do {
            Sold = new ArrayList<>(S); // Salva la soluzione corrente

            // Step 3: Applica l'operatore Insert
            Sold = Insert(Sold, CS, Cmax, p);

            // Step 4: Applica l'operatore Swap
            Sold = Swap(Sold, CS, Cmax, p);

            // Step 5: Applica l'operatore 2-opt
            S = twoOpt(Sold, CS, Cmax, p);

        } while (!S.equals(Sold));

        // Output della soluzione finale migliorata
        System.out.println("Soluzione finale migliorata:");
        for (Cluster cluster : S) {
            System.out.println(cluster);
        }
    }

    // Inizializzazione della soluzione iniziale come array di cluster
    public static List<Cluster> initializeSolution() {
        List<Cluster> solution = new ArrayList<>();

        // Esempio di inizializzazione (adatta alla tua logica specifica)
        solution.add(new Cluster(1, 3)); //  1 con massimo 3 città
        solution.add(new Cluster(2, 3)); // C 2 con massimo 3 città

        // Aggiungi città ai cluster (esempio)
        solution.get(0).addCityIndex(1);
        solution.get(0).addCityIndex(2);
        solution.get(1).addCityIndex(3);
        solution.get(1).addCityIndex(4);

        return solution;
    }

    // Inizializzazione dei candidati (esempio)
    public static List<List<Integer>> initializeCandidateSets() {
        List<List<Integer>> CS = new ArrayList<>();

        // Esempio di inizializzazione (adatta alla tua logica specifica)
        CS.add(Arrays.asList(5, 6, 7)); // Candidati per il cluster 1
        CS.add(Arrays.asList(8, 9, 10)); // Candidati per il cluster 2

        return CS;
    }

    // Operatore Insert
    public static List<Cluster> Insert(List<Cluster> S, List<List<Integer>> CS, int Cmax, int p) {
        Random rand = new Random();
        int clusterIndex = rand.nextInt(S.size()); // Seleziona casualmente un indice di cluster
        List<Integer> candidateSet = CS.get(clusterIndex % CS.size()); // Ottieni il set di candidati per l'operatore Insert

        // Verifica se il set di candidati non è vuoto
        if (!candidateSet.isEmpty()) {
            // Esempio di logica per l'operatore Insert (adatta alla tua logica specifica)
            int cityToAdd = candidateSet.get(rand.nextInt(candidateSet.size())); // Seleziona casualmente una città dal set di candidati
            int clusterToAddIndex = rand.nextInt(S.size()); // Seleziona casualmente un cluster per aggiungere la città

            // Aggiungi la città solo se non supera il numero massimo di città
            if (S.get(clusterToAddIndex).getCityIndices().size() < Cmax) {
                S.get(clusterToAddIndex).addCityIndex(cityToAdd);
            }
        } else {
            System.out.println("Il set di candidati è vuoto o non è stato inizializzato correttamente.");
        }

        return S;
    }

    // Operatore Swap
    public static List<Cluster> Swap(List<Cluster> S, List<List<Integer>> CS, int Cmax, int p) {
        Random rand = new Random();
        int clusterIndex1 = rand.nextInt(S.size()); // Seleziona casualmente il primo indice di cluster
        int clusterIndex2 = rand.nextInt(S.size()); // Seleziona casualmente il secondo indice di cluster

        // Esempio di logica per l'operatore Swap (adatta alla tua logica specifica)
        // Questo esempio scambia una città tra due cluster casuali
        if (!S.isEmpty() && S.size() > 1) {
            int cityIndex1 = rand.nextInt(S.get(clusterIndex1).getCityIndices().size()); // Indice casuale della città nel cluster 1
            int cityIndex2 = rand.nextInt(S.get(clusterIndex2).getCityIndices().size()); // Indice casuale della città nel cluster 2
            int city1 = S.get(clusterIndex1).getCityIndices().get(cityIndex1);
            int city2 = S.get(clusterIndex2).getCityIndices().get(cityIndex2);

            // Scambia le città tra i cluster (esempio)
            S.get(clusterIndex1).getCityIndices().set(cityIndex1, city2);
            S.get(clusterIndex2).getCityIndices().set(cityIndex2, city1);
        }

        return S;
    }

    // Operatore 2-opt
    public static List<Cluster> twoOpt(List<Cluster> S, List<List<Integer>> CS, int Cmax, int p) {
        Random rand = new Random();
        int clusterIndex1 = rand.nextInt(S.size()); // Seleziona casualmente il primo indice di cluster
        int clusterIndex2 = rand.nextInt(S.size()); // Seleziona casualmente il secondo indice di cluster

        // Esempio di logica per l'operatore 2-opt (adatta alla tua logica specifica)
        // Questo esempio esegue un semplice swap 2-opt in un singolo cluster
        if (!S.isEmpty()) {
            int cityIndex1 = rand.nextInt(S.get(clusterIndex1).getCityIndices().size() - 1); // Primo indice casuale della città
            int cityIndex2 = cityIndex1 + 1; // Secondo indice della città per formare una coppia di scambio

            // Esegui lo swap 2-opt nel cluster selezionato (esempio)
            int city1 = S.get(clusterIndex1).getCityIndices().get(cityIndex1);
            int city2 = S.get(clusterIndex1).getCityIndices().get(cityIndex2);

            // Scambia le città nel cluster (esempio)
            S.get(clusterIndex1).getCityIndices().set(cityIndex1, city2);
            S.get(clusterIndex1).getCityIndices().set(cityIndex2, city1);
        }

        return S;
    }

}