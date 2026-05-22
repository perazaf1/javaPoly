package org.example.monopoly.model;

import java.util.ArrayList;
import java.util.Collections;

// Deck de cartes (Chance ou Caisse de communaute)
public class DeckCartes {

    private ArrayList<Carte> cartes;
    private int indiceCourant;

    public DeckCartes() {
        this.cartes = new ArrayList<>();
        this.indiceCourant = 0;
    }

    // Ajoute une carte au deck
    public void ajouterCarte(Carte carte) {
        cartes.add(carte);
    }

    // Melange les cartes aleatoirement
    public void melanger() {
        Collections.shuffle(cartes);
        indiceCourant = 0;
    }

    // Pioche la carte du dessus et passe a la suivante
    public Carte piocher() {
        if (cartes.isEmpty()) {
            return null;
        }

        // Si on a parcouru tout le deck, on remelange
        if (indiceCourant >= cartes.size()) {
            melanger();
        }

        Carte carte = cartes.get(indiceCourant);
        indiceCourant++;
        return carte;
    }

    public int getNbCartes() {
        return cartes.size();
    }

    public ArrayList<Carte> getCartes() {
        return cartes;
    }
}
