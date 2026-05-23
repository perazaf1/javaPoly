package org.example.monopoly.model;

// Represente une compagnie (Electricite ou Eau)
public class Compagnie extends Case {

    private int prix;
    private Joueur proprietaire;
    private boolean hypothequee;

    public Compagnie(String nom, int position) {
        super(nom, position);
        this.prix = 150;
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

        // Le loyer depend du lancer de des, gere par le controller
        // Ici on ne fait rien, le controller appellera calculerLoyer(resultatDes)
    }

    // Loyer = 4x le resultat des des si 1 compagnie, 10x si 2 compagnies
    public int calculerLoyer(int resultatDes) {
        if (hypothequee || proprietaire == null) {
            return 0;
        }

        int nbCompagnies = proprietaire.getCompagnies().size();
        if (nbCompagnies == 1) {
            return resultatDes * 4;
        } else if (nbCompagnies == 2) {
            return resultatDes * 10;
        }
        return 0;
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
