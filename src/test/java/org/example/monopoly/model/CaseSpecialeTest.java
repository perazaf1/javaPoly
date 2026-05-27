package org.example.monopoly.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe CaseSpeciale
class CaseSpecialeTest {

    @Test
    void testCreation() {
        CaseSpeciale cs = new CaseSpeciale("Depart", 0, "Depart");
        assertEquals("Depart", cs.getNom());
        assertEquals(0, cs.getPosition());
        assertEquals("Depart", cs.getType());
    }

    @Test
    void testAllerEnPrison() {
        CaseSpeciale cs = new CaseSpeciale("Allez en prison", 30, "AllerEnPrison");
        Joueur joueur = new Joueur("Alice", "Chapeau");

        cs.actionSurCase(joueur);
        assertTrue(joueur.estEnPrison());
        assertEquals(10, joueur.getPosition());
    }

    @Test
    void testParcGratuit_rienNeSePasse() {
        CaseSpeciale cs = new CaseSpeciale("Parc gratuit", 20, "ParcGratuit");
        Joueur joueur = new Joueur("Alice", "Chapeau");
        int argentAvant = joueur.getArgent();
        int posAvant = joueur.getPosition();

        cs.actionSurCase(joueur);
        assertEquals(argentAvant, joueur.getArgent());
        assertEquals(posAvant, joueur.getPosition());
    }

    @Test
    void testPrison_simpleVisite() {
        CaseSpeciale cs = new CaseSpeciale("Prison", 10, "Prison");
        Joueur joueur = new Joueur("Alice", "Chapeau");

        cs.actionSurCase(joueur);
        assertFalse(joueur.estEnPrison());
    }
}
