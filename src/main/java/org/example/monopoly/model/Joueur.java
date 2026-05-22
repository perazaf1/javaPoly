package org.example.monopoly.model;

import java.util.ArrayList;

// Represente un joueur de la partie
public class Joueur {

    private String nom;
    private String pion;
    private int argent;
    private int position;
    private String etat; // "EN_JEU", "EN_PRISON", "FAILLITE"
    private int toursEnPrison;

    private ArrayList<Propriete> proprietes;
    private ArrayList<Gare> gares;
    private ArrayList<Compagnie> compagnies;

    public Joueur(String nom, String pion) {
        this.nom = nom;
        this.pion = pion;
        this.argent = 1500;
        this.position = 0;
        this.etat = "EN_JEU";
        this.toursEnPrison = 0;
        this.proprietes = new ArrayList<>();
        this.gares = new ArrayList<>();
        this.compagnies = new ArrayList<>();
    }

    // Deplace le joueur de nbCases cases (avec passage par la case depart)
    public void deplacer(int nbCases) {
        int anciennePosition = this.position;
        this.position = (this.position + nbCases) % 40;

        // Si le joueur passe par la case depart, il recoit 200
        if (this.position < anciennePosition) {
            recevoirArgent(200);
        }
    }

    // Deplace le joueur directement a une position donnee
    public void deplacerVers(int nouvellePosition) {
        int anciennePosition = this.position;
        this.position = nouvellePosition;

        // Passage par la case depart (sauf si on va en prison)
        if (this.position < anciennePosition && !this.etat.equals("EN_PRISON")) {
            recevoirArgent(200);
        }
    }

    // Retire de l'argent au joueur
    public void payerArgent(int montant) {
        this.argent -= montant;
    }

    // Ajoute de l'argent au joueur
    public void recevoirArgent(int montant) {
        this.argent += montant;
    }

    // Calcule la valeur totale du patrimoine (argent + proprietes)
    public int getPatrimoineTotal() {
        int total = this.argent;

        for (Propriete p : proprietes) {
            total += p.getPrix();
            total += p.getNbMaisons() * p.getCoutConstruction();
            if (p.isHotel()) {
                total += p.getCoutConstruction();
            }
        }
        for (Gare g : gares) {
            total += g.getPrix();
        }
        for (Compagnie c : compagnies) {
            total += c.getPrix();
        }

        return total;
    }

    // Ajoute une propriete au joueur
    public void ajouterPropriete(Propriete propriete) {
        this.proprietes.add(propriete);
    }

    // Ajoute une gare au joueur
    public void ajouterGare(Gare gare) {
        this.gares.add(gare);
    }

    // Ajoute une compagnie au joueur
    public void ajouterCompagnie(Compagnie compagnie) {
        this.compagnies.add(compagnie);
    }

    // Envoie le joueur en prison
    public void allerEnPrison() {
        this.position = 10; // case Prison
        this.etat = "EN_PRISON";
        this.toursEnPrison = 0;
    }

    // Libere le joueur de la prison
    public void sortirDePrison() {
        this.etat = "EN_JEU";
        this.toursEnPrison = 0;
    }

    // Verifie si le joueur est en faillite
    public boolean estEnFaillite() {
        return this.etat.equals("FAILLITE");
    }

    // Verifie si le joueur est en prison
    public boolean estEnPrison() {
        return this.etat.equals("EN_PRISON");
    }

    // Verifie si le joueur possede toutes les proprietes d'un groupe
    public boolean possedeGroupeComplet(String groupe) {
        int nbDansGroupe = 0;
        int nbPossedees = 0;

        // Nombre de proprietes attendues par groupe
        int nbAttendu = 3;
        if (groupe.equals("Marron") || groupe.equals("Bleu fonce")) {
            nbAttendu = 2;
        }

        for (Propriete p : proprietes) {
            if (p.getGroupe().equals(groupe)) {
                nbPossedees++;
            }
        }

        return nbPossedees == nbAttendu;
    }

    // Getters et setters
    public String getNom() {
        return nom;
    }

    public String getPion() {
        return pion;
    }

    public int getArgent() {
        return argent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getToursEnPrison() {
        return toursEnPrison;
    }

    public void setToursEnPrison(int toursEnPrison) {
        this.toursEnPrison = toursEnPrison;
    }

    public ArrayList<Propriete> getProprietes() {
        return proprietes;
    }

    public ArrayList<Gare> getGares() {
        return gares;
    }

    public ArrayList<Compagnie> getCompagnies() {
        return compagnies;
    }

    @Override
    public String toString() {
        return nom + " (" + pion + ") - " + argent + "$";
    }
}
