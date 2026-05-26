package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe Gare
class GareTest {

    private Joueur proprietaire;
    private Joueur autreJoueur;

    @BeforeEach
    void setUp() {
        proprietaire = new Joueur("Alice", "Chapeau");
        autreJoueur = new Joueur("Bob", "Voiture");
    }

    @Test
    void testCreation() {
        Gare gare = new Gare("Gare Montparnasse", 5);
        assertEquals("Gare Montparnasse", gare.getNom());
        assertEquals(5, gare.getPosition());
        assertEquals(200, gare.getPrix());
        assertNull(gare.getProprietaire());
    }

    @Test
    void testLoyer_uneGare() {
        Gare g1 = new Gare("Gare 1", 5);
        g1.setProprietaire(proprietaire);
        proprietaire.ajouterGare(g1);
        assertEquals(25, g1.calculerLoyer());
    }

    @Test
    void testLoyer_deuxGares() {
        Gare g1 = new Gare("Gare 1", 5);
        Gare g2 = new Gare("Gare 2", 15);
        g1.setProprietaire(proprietaire);
        g2.setProprietaire(proprietaire);
        proprietaire.ajouterGare(g1);
        proprietaire.ajouterGare(g2);
        assertEquals(50, g1.calculerLoyer());
        assertEquals(50, g2.calculerLoyer());
    }

    @Test
    void testLoyer_troisGares() {
        for (int i = 0; i < 3; i++) {
            Gare g = new Gare("Gare " + i, i * 10 + 5);
            g.setProprietaire(proprietaire);
            proprietaire.ajouterGare(g);
        }
        assertEquals(100, proprietaire.getGares().get(0).calculerLoyer());
    }

    @Test
    void testLoyer_quatreGares() {
        for (int i = 0; i < 4; i++) {
            Gare g = new Gare("Gare " + i, i * 10 + 5);
            g.setProprietaire(proprietaire);
            proprietaire.ajouterGare(g);
        }
        assertEquals(200, proprietaire.getGares().get(0).calculerLoyer());
    }

    @Test
    void testLoyer_hypothequee() {
        Gare g = new Gare("Gare", 5);
        g.setProprietaire(proprietaire);
        proprietaire.ajouterGare(g);
        g.setHypothequee(true);
        assertEquals(0, g.calculerLoyer());
    }

    @Test
    void testLoyer_sansProprietaire() {
        Gare g = new Gare("Gare", 5);
        assertEquals(0, g.calculerLoyer());
    }

    @Test
    void testActionSurCase_payerLoyer() {
        Gare g = new Gare("Gare", 5);
        g.setProprietaire(proprietaire);
        proprietaire.ajouterGare(g);

        int argentAvant = autreJoueur.getArgent();
        g.actionSurCase(autreJoueur);
        assertEquals(argentAvant - 25, autreJoueur.getArgent());
    }

    @Test
    void testActionSurCase_gareLibre() {
        Gare g = new Gare("Gare", 5);
        int argentAvant = autreJoueur.getArgent();
        g.actionSurCase(autreJoueur);
        assertEquals(argentAvant, autreJoueur.getArgent());
    }
}
