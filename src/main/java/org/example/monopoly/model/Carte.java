package org.example.monopoly.model;

// Represente une carte Chance ou Caisse de communaute
public class Carte {

    private String description;
    private String typeCarte; // "Chance" ou "Communaute"
    private String typeEffet;  // "ArgentGain", "ArgentPerte", "Deplacement", "Prison", "Reparations"
    private int valeur;        // montant d'argent ou position cible selon le type

    public Carte(String description, String typeCarte, String typeEffet, int valeur) {
        this.description = description;
        this.typeCarte = typeCarte;
        this.typeEffet = typeEffet;
        this.valeur = valeur;
    }

    // Applique l'effet de la carte sur le joueur
    public void appliquerEffet(Joueur joueur) {
        switch (typeEffet) {
            case "ArgentGain":
                joueur.recevoirArgent(valeur);
                break;
            case "ArgentPerte":
                joueur.payerArgent(valeur);
                break;
            case "Deplacement":
                joueur.deplacerVers(valeur);
                break;
            case "Prison":
                joueur.allerEnPrison();
                break;
            case "Reparations":
                // Payer par maison et par hotel
                int totalMaisons = 0;
                int totalHotels = 0;
                for (Propriete p : joueur.getProprietes()) {
                    if (p.isHotel()) {
                        totalHotels++;
                    } else {
                        totalMaisons += p.getNbMaisons();
                    }
                }
                // valeur = cout par maison, hotel = 4x le cout par maison
                int montant = totalMaisons * valeur + totalHotels * (valeur * 4);
                joueur.payerArgent(montant);
                break;
        }
    }

    public String getDescription() {
        return description;
    }

    public String getTypeCarte() {
        return typeCarte;
    }

    public String getTypeEffet() {
        return typeEffet;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return "[" + typeCarte + "] " + description;
    }
}
