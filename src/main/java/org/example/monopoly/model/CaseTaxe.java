package org.example.monopoly.model;

// Cases de taxe : Impot sur le revenu (200) et Taxe de luxe (100)
public class CaseTaxe extends Case {

    private int montant;

    public CaseTaxe(String nom, int position, int montant) {
        super(nom, position);
        this.montant = montant;
    }

    @Override
    public void actionSurCase(Joueur joueur) {
        joueur.payerArgent(montant);
    }

    public int getMontant() {
        return montant;
    }
}
