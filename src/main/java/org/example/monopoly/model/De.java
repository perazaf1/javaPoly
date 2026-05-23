package org.example.monopoly.model;

import java.util.Random;

// Represente les deux des du jeu
public class De {

    private int valeur1;
    private int valeur2;
    private Random random;

    public De() {
        this.random = new Random();
        this.valeur1 = 0;
        this.valeur2 = 0;
    }

    // Lance les deux des (valeurs entre 1 et 6)
    public void lancer() {
        this.valeur1 = random.nextInt(6) + 1;
        this.valeur2 = random.nextInt(6) + 1;
    }

    // Verifie si les deux des ont la meme valeur
    public boolean estDouble() {
        return valeur1 == valeur2;
    }

    // Retourne la somme des deux des
    public int getSomme() {
        return valeur1 + valeur2;
    }

    public int getValeur1() {
        return valeur1;
    }

    public int getValeur2() {
        return valeur2;
    }

    @Override
    public String toString() {
        return "De [" + valeur1 + " + " + valeur2 + " = " + getSomme() + "]";
    }
}
