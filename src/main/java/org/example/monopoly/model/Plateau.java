package org.example.monopoly.model;

// Represente le plateau de 40 cases du Monopoly
public class Plateau {

    private Case[] cases;

    public Plateau() {
        this.cases = new Case[40];
        initialiserCases();
    }

    // Initialise les 40 cases du plateau Monopoly classique (version francaise)
    private void initialiserCases() {
        // Case 0 : Départ
        cases[0] = new CaseSpeciale("Départ", 0, "Depart");

        // Groupe Marron
        cases[1] = new Propriete("Boulevard de Belleville", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        cases[2] = new CaseSpeciale("Caisse de communauté", 2, "Communaute");
        cases[3] = new Propriete("Rue Lecourbe", 3, 60, "Marron",
                new int[]{4, 20, 60, 180, 320, 450}, 50);

        // Taxe
        cases[4] = new CaseTaxe("Impôt sur le revenu", 4, 200);

        // Gare 1
        cases[5] = new Gare("Gare Montparnasse", 5);

        // Groupe Bleu clair
        cases[6] = new Propriete("Rue de Vaugirard", 6, 100, "Bleu clair",
                new int[]{6, 30, 90, 270, 400, 550}, 50);
        cases[7] = new CaseSpeciale("Chance", 7, "Chance");
        cases[8] = new Propriete("Rue de Courcelles", 8, 100, "Bleu clair",
                new int[]{6, 30, 90, 270, 400, 550}, 50);
        cases[9] = new Propriete("Avenue de la République", 9, 120, "Bleu clair",
                new int[]{8, 40, 100, 300, 450, 600}, 50);

        // Case 10 : Prison (simple visite)
        cases[10] = new CaseSpeciale("Prison / Simple visite", 10, "Prison");

        // Groupe Rose
        cases[11] = new Propriete("Boulevard de la Villette", 11, 140, "Rose",
                new int[]{10, 50, 150, 450, 625, 750}, 100);
        cases[12] = new Compagnie("Compagnie de distribution d'électricité", 12);
        cases[13] = new Propriete("Avenue de Neuilly", 13, 140, "Rose",
                new int[]{10, 50, 150, 450, 625, 750}, 100);
        cases[14] = new Propriete("Rue de Paradis", 14, 160, "Rose",
                new int[]{12, 60, 180, 500, 700, 900}, 100);

        // Gare 2
        cases[15] = new Gare("Gare de Lyon", 15);

        // Groupe Orange
        cases[16] = new Propriete("Avenue Mozart", 16, 180, "Orange",
                new int[]{14, 70, 200, 550, 750, 950}, 100);
        cases[17] = new CaseSpeciale("Caisse de communauté", 17, "Communaute");
        cases[18] = new Propriete("Boulevard Saint-Michel", 18, 180, "Orange",
                new int[]{14, 70, 200, 550, 750, 950}, 100);
        cases[19] = new Propriete("Place Pigalle", 19, 200, "Orange",
                new int[]{16, 80, 220, 600, 800, 1000}, 100);

        // Case 20 : Parc gratuit
        cases[20] = new CaseSpeciale("Parc gratuit", 20, "ParcGratuit");

        // Groupe Rouge
        cases[21] = new Propriete("Avenue Matignon", 21, 220, "Rouge",
                new int[]{18, 90, 250, 700, 875, 1050}, 150);
        cases[22] = new CaseSpeciale("Chance", 22, "Chance");
        cases[23] = new Propriete("Boulevard Malesherbes", 23, 220, "Rouge",
                new int[]{18, 90, 250, 700, 875, 1050}, 150);
        cases[24] = new Propriete("Avenue Henri-Martin", 24, 240, "Rouge",
                new int[]{20, 100, 300, 750, 925, 1100}, 150);

        // Gare 3
        cases[25] = new Gare("Gare du Nord", 25);

        // Groupe Jaune
        cases[26] = new Propriete("Faubourg Saint-Honoré", 26, 260, "Jaune",
                new int[]{22, 110, 330, 800, 975, 1150}, 150);
        cases[27] = new Propriete("Place de la Bourse", 27, 260, "Jaune",
                new int[]{22, 110, 330, 800, 975, 1150}, 150);
        cases[28] = new Compagnie("Compagnie de distribution des eaux", 28);
        cases[29] = new Propriete("Rue La Fayette", 29, 280, "Jaune",
                new int[]{24, 120, 360, 850, 1025, 1200}, 150);

        // Gare 4
        cases[30] = new Gare("Gare Saint-Lazare", 30);

        // Groupe Vert
        cases[31] = new Propriete("Avenue de Breteuil", 31, 300, "Vert",
                new int[]{26, 130, 390, 900, 1100, 1275}, 200);
        cases[32] = new Propriete("Avenue Foch", 32, 300, "Vert",
                new int[]{26, 130, 390, 900, 1100, 1275}, 200);
        cases[33] = new CaseSpeciale("Caisse de communauté", 33, "Communaute");
        cases[34] = new Propriete("Boulevard des Capucines", 34, 320, "Vert",
                new int[]{28, 150, 450, 1000, 1200, 1400}, 200);

        // Gare 5 -> non, c'est Chance
        cases[35] = new CaseSpeciale("Chance", 35, "Chance");

        // Taxe de luxe
        cases[36] = new CaseTaxe("Taxe de luxe", 36, 100);

        // Pas de case 35 gare, on continue
        // Groupe Bleu fonce
        cases[37] = new Propriete("Avenue des Champs-Élysées", 37, 350, "Bleu fonce",
                new int[]{35, 175, 500, 1100, 1300, 1500}, 200);
        cases[38] = new CaseSpeciale("Caisse de communauté", 38, "Communaute");
        cases[39] = new Propriete("Rue de la Paix", 39, 400, "Bleu fonce",
                new int[]{50, 200, 600, 1400, 1700, 2000}, 200);
    }

    // Retourne la case a la position donnee
    public Case getCase(int position) {
        if (position < 0 || position >= 40) {
            return null;
        }
        return cases[position];
    }

    public Case[] getCases() {
        return cases;
    }

    public int getNbCases() {
        return cases.length;
    }
}
