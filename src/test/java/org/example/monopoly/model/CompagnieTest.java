package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe Compagnie
class CompagnieTest {

    private Joueur proprietaire;
    private Joueur autreJoueur;

    @BeforeEach
    void setUp() {
        proprietaire = new Joueur("Alice", "Chapeau");
        autreJoueur = new Joueur("Bob", "Voiture");
    }

    @Test
    void testCreation() {
        Compagnie c = new Compagnie("Cie Electricite", 12);
        assertEquals("Cie Electricite", c.getNom());
        assertEquals(12, c.getPosition());
        assertEquals(150, c.getPrix());
        assertNull(c.getProprietaire());
    }

    @Test
    void testLoyer_uneCompagnie() {
        Compagnie c = new Compagnie("Cie 1", 12);
        c.setProprietaire(proprietaire);
        proprietaire.ajouterCompagnie(c);

        // Loyer = 4x le resultat des des
        assertEquals(28, c.calculerLoyer(7)); // 7 * 4
        assertEquals(48, c.calculerLoyer(12)); // 12 * 4
        assertEquals(8, c.calculerLoyer(2)); // 2 * 4
    }

    @Test
    void testLoyer_deuxCompagnies() {
        Compagnie c1 = new Compagnie("Cie 1", 12);
        Compagnie c2 = new Compagnie("Cie 2", 28);
        c1.setProprietaire(proprietaire);
        c2.setProprietaire(proprietaire);
        proprietaire.ajouterCompagnie(c1);
        proprietaire.ajouterCompagnie(c2);

        // Loyer = 10x le resultat des des
        assertEquals(70, c1.calculerLoyer(7)); // 7 * 10
        assertEquals(120, c2.calculerLoyer(12)); // 12 * 10
    }

    @Test
    void testLoyer_sansProprietaire() {
        Compagnie c = new Compagnie("Cie", 12);
        assertEquals(0, c.calculerLoyer(7));
    }

    @Test
    void testLoyer_hypothequee() {
        Compagnie c = new Compagnie("Cie", 12);
        c.setProprietaire(proprietaire);
        proprietaire.ajouterCompagnie(c);
        c.setHypothequee(true);
        assertEquals(0, c.calculerLoyer(7));
    }
}
