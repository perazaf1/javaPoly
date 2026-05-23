package org.example.monopoly.model;

// Represente une gare (4 gares sur le plateau)
public class Gare extends Case {

    private int prix;
    private Joueur proprietaire;
    private boolean hypothequee;

    public Gare(String nom, int position) {
        super(nom, position);
        this.prix = 200;
        this.proprietaire = null;
        this.hypothequee = false;
    }

    @Override
    public void actionSurCase(Joueur joueur) {
        if (proprietaire == null) {
            return;
        }

        if (proprietaire == joueur || hypothequee) {
            return;
        }

        int loyer = calculerLoyer();
        joueur.payerArgent(loyer);
        proprietaire.recevoirArgent(loyer);
    }

    // Loyer progressif selon le nombre de gares possedees : 25, 50, 100, 200
    public int calculerLoyer() {
        if (hypothequee || proprietaire == null) {
            return 0;
        }

        int nbGares = proprietaire.getGares().size();
        switch (nbGares) {
            case 1: return 25;
            case 2: return 50;
            case 3: return 100;
            case 4: return 200;
            default: return 0;
        }
    }

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
}
