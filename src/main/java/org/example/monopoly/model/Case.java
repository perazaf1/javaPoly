package org.example.monopoly.model;

// Classe abstraite representant une case du plateau
public abstract class Case {

    private String nom;
    private int position;

    public Case(String nom, int position) {
        this.nom = nom;
        this.position = position;
    }

    // Action executee quand un joueur arrive sur cette case
    public abstract void actionSurCase(Joueur joueur);

    public String getNom() {
        return nom;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return nom + " (case " + position + ")";
    }
}
