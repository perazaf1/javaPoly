package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe Carte
class CarteTest {

    private Joueur joueur;

    @BeforeEach
    void setUp() {
        joueur = new Joueur("Alice", "Chapeau");
    }

    @Test
    void testArgentGain() {
        Carte carte = new Carte("Gain 200$", "Chance", "ArgentGain", 200);
        carte.appliquerEffet(joueur);
        assertEquals(1700, joueur.getArgent());
    }

    @Test
    void testArgentPerte() {
        Carte carte = new Carte("Perte 100$", "Chance", "ArgentPerte", 100);
        carte.appliquerEffet(joueur);
        assertEquals(1400, joueur.getArgent());
    }

    @Test
    void testDeplacement() {
        Carte carte = new Carte("Aller case 39", "Chance", "Deplacement", 39);
        carte.appliquerEffet(joueur);
        assertEquals(39, joueur.getPosition());
    }

    @Test
    void testDeplacement_passageCaseDepart() {
        joueur.setPosition(35);
        Carte carte = new Carte("Aller case 0", "Chance", "Deplacement", 0);
        carte.appliquerEffet(joueur);
        assertEquals(0, joueur.getPosition());
        // Le joueur passe par la case depart -> +200$
        assertEquals(1700, joueur.getArgent());
    }

    @Test
    void testPrison() {
        Carte carte = new Carte("Allez en prison", "Chance", "Prison", 0);
        carte.appliquerEffet(joueur);
        assertTrue(joueur.estEnPrison());
        assertEquals(10, joueur.getPosition());
    }

    @Test
    void testReparations_sansMaisons() {
        Carte carte = new Carte("Reparations 25$ par maison", "Chance", "Reparations", 25);
        carte.appliquerEffet(joueur);
        // Pas de maisons = pas de cout
        assertEquals(1500, joueur.getArgent());
    }

    @Test
    void testReparations_avecMaisons() {
        Propriete p = new Propriete("Test", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        joueur.ajouterPropriete(p);
        p.construireMaison();
        p.construireMaison(); // 2 maisons

        Carte carte = new Carte("Reparations 25$ par maison", "Chance", "Reparations", 25);
        carte.appliquerEffet(joueur);
        // 2 maisons * 25$ = 50$
        assertEquals(1450, joueur.getArgent());
    }

    @Test
    void testReparations_avecHotel() {
        Propriete p = new Propriete("Test", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        joueur.ajouterPropriete(p);
        for (int i = 0; i < 4; i++) p.construireMaison();
        p.construireHotel(); // 1 hotel

        Carte carte = new Carte("Reparations 25$ par maison", "Chance", "Reparations", 25);
        carte.appliquerEffet(joueur);
        // 1 hotel * (25 * 4) = 100$
        assertEquals(1400, joueur.getArgent());
    }

    @Test
    void testGetters() {
        Carte carte = new Carte("Description", "Chance", "ArgentGain", 50);
        assertEquals("Description", carte.getDescription());
        assertEquals("Chance", carte.getTypeCarte());
        assertEquals("ArgentGain", carte.getTypeEffet());
        assertEquals(50, carte.getValeur());
    }

    @Test
    void testToString() {
        Carte carte = new Carte("Test", "Chance", "ArgentGain", 50);
        assertTrue(carte.toString().contains("Chance"));
        assertTrue(carte.toString().contains("Test"));
    }
}
