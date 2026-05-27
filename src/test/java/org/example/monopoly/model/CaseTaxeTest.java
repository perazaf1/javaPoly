package org.example.monopoly.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe CaseTaxe
class CaseTaxeTest {

    @Test
    void testCreation() {
        CaseTaxe ct = new CaseTaxe("Impot", 4, 200);
        assertEquals("Impot", ct.getNom());
        assertEquals(4, ct.getPosition());
        assertEquals(200, ct.getMontant());
    }

    @Test
    void testActionSurCase() {
        CaseTaxe ct = new CaseTaxe("Impot", 4, 200);
        Joueur joueur = new Joueur("Alice", "Chapeau");

        ct.actionSurCase(joueur);
        assertEquals(1300, joueur.getArgent()); // 1500 - 200
    }

    @Test
    void testTaxeDeLuxe() {
        CaseTaxe ct = new CaseTaxe("Taxe de luxe", 36, 100);
        Joueur joueur = new Joueur("Alice", "Chapeau");

        ct.actionSurCase(joueur);
        assertEquals(1400, joueur.getArgent()); // 1500 - 100
    }
}
