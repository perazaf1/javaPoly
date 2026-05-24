package org.example.monopoly.model;

import java.util.ArrayList;
import java.util.HashMap;

// Enregistre les evenements de la partie et les donnees pour les statistiques
public class Historique {

    private ArrayList<String> evenements;
    private HashMap<String, ArrayList<Integer>> fortuneParTour; // nom joueur -> liste de fortunes

    public Historique() {
        this.evenements = new ArrayList<>();
        this.fortuneParTour = new HashMap<>();
    }

    // Ajoute un evenement au log
    public void ajouterEvenement(String evenement) {
        evenements.add(evenement);
    }

    // Enregistre la fortune d'un joueur pour le tour courant
    public void ajouterFortune(String nomJoueur, int fortune) {
        if (!fortuneParTour.containsKey(nomJoueur)) {
            fortuneParTour.put(nomJoueur, new ArrayList<>());
        }
        fortuneParTour.get(nomJoueur).add(fortune);
    }

    // Enregistre la fortune de tous les joueurs (appele a chaque fin de tour)
    public void enregistrerTour(ArrayList<Joueur> joueurs) {
        for (Joueur j : joueurs) {
            ajouterFortune(j.getNom(), j.getPatrimoineTotal());
        }
    }

    // Retourne l'evolution de fortune d'un joueur
    public ArrayList<Integer> getFortuneJoueur(String nomJoueur) {
        return fortuneParTour.get(nomJoueur);
    }

    public ArrayList<String> getEvenements() {
        return evenements;
    }

    public HashMap<String, ArrayList<Integer>> getFortuneParTour() {
        return fortuneParTour;
    }
}
