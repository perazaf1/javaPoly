package org.example.monopoly.model;

// Gere les maisons, hotels et la distribution d'argent
public class Banque {

    private int maisonsDisponibles;
    private int hotelsDisponibles;

    public Banque() {
        this.maisonsDisponibles = 32;
        this.hotelsDisponibles = 12;
    }

    // Distribue l'argent de depart a un joueur (1500)
    public void distribuerArgent(Joueur joueur) {
        joueur.recevoirArgent(1500);
    }

    // Vend une maison si disponible
    public boolean acheterMaison() {
        if (maisonsDisponibles <= 0) {
            return false;
        }
        maisonsDisponibles--;
        return true;
    }

    // Vend un hotel (rend 4 maisons au stock)
    public boolean acheterHotel() {
        if (hotelsDisponibles <= 0) {
            return false;
        }
        hotelsDisponibles--;
        maisonsDisponibles += 4; // les 4 maisons reviennent au stock
        return true;
    }

    // Rend des maisons au stock (vente ou faillite)
    public void rendreMaisons(int nb) {
        maisonsDisponibles += nb;
    }

    // Rend un hotel au stock
    public void rendreHotel() {
        hotelsDisponibles++;
    }

    public int getMaisonsDisponibles() {
        return maisonsDisponibles;
    }

    public int getHotelsDisponibles() {
        return hotelsDisponibles;
    }
}
