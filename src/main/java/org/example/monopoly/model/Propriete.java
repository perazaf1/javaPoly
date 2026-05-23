package org.example.monopoly.model;

// Represente une propriete (terrain) achetable
public class Propriete extends Case {

    private int prix;
    private Joueur proprietaire;
    private boolean hypothequee;
    private String groupe;
    private int[] loyers; // loyers[0]=terrain nu, [1]=1 maison, ..., [5]=hotel
    private int nbMaisons;
    private boolean hotel;
    private int coutConstruction;

    public Propriete(String nom, int position, int prix, String groupe,
                     int[] loyers, int coutConstruction) {
        super(nom, position);
        this.prix = prix;
        this.proprietaire = null;
        this.hypothequee = false;
        this.groupe = groupe;
        this.loyers = loyers;
        this.nbMaisons = 0;
        this.hotel = false;
        this.coutConstruction = coutConstruction;
    }

    @Override
    public void actionSurCase(Joueur joueur) {
        if (proprietaire == null) {
            // Terrain libre : le joueur peut l'acheter (gere par le controller)
            return;
        }

        if (proprietaire == joueur || hypothequee) {
            // C'est sa propre propriete ou elle est hypothequee
            return;
        }

        // Le joueur doit payer le loyer au proprietaire
        int loyer = calculerLoyer();
        joueur.payerArgent(loyer);
        proprietaire.recevoirArgent(loyer);
    }

    // Calcule le loyer selon le nombre de maisons/hotel
    public int calculerLoyer() {
        if (hypothequee) {
            return 0;
        }

        if (hotel) {
            return loyers[5];
        }

        if (nbMaisons > 0) {
            return loyers[nbMaisons];
        }

        // Terrain nu : loyer double si le proprietaire a tout le groupe
        int loyerBase = loyers[0];
        if (proprietaire != null && proprietaire.possedeGroupeComplet(groupe)) {
            loyerBase *= 2;
        }
        return loyerBase;
    }

    // Construit une maison sur la propriete (max 4)
    public boolean construireMaison() {
        if (hotel || nbMaisons >= 4) {
            return false;
        }
        nbMaisons++;
        return true;
    }

    // Construit un hotel (remplace les 4 maisons)
    public boolean construireHotel() {
        if (hotel || nbMaisons < 4) {
            return false;
        }
        hotel = true;
        nbMaisons = 0;
        return true;
    }

    // Getters et setters
    public int getPrix() {
        return prix;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Joueur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public boolean isHypothequee() {
        return hypothequee;
    }

    public void setHypothequee(boolean hypothequee) {
        this.hypothequee = hypothequee;
    }

    public String getGroupe() {
        return groupe;
    }

    public int[] getLoyers() {
        return loyers;
    }

    public int getNbMaisons() {
        return nbMaisons;
    }

    public boolean isHotel() {
        return hotel;
    }

    public int getCoutConstruction() {
        return coutConstruction;
    }
}
