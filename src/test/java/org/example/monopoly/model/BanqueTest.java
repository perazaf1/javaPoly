package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe Banque
class BanqueTest {

    private Banque banque;

    @BeforeEach
    void setUp() {
        banque = new Banque();
    }

    @Test
    void testCreation() {
        assertEquals(32, banque.getMaisonsDisponibles());
        assertEquals(12, banque.getHotelsDisponibles());
    }

    @Test
    void testAcheterMaison() {
        assertTrue(banque.acheterMaison());
        assertEquals(31, banque.getMaisonsDisponibles());
    }

    @Test
    void testAcheterMaison_plusDeStock() {
        // Acheter les 32 maisons
        for (int i = 0; i < 32; i++) {
            assertTrue(banque.acheterMaison());
        }
        assertEquals(0, banque.getMaisonsDisponibles());
        // Plus de maisons disponibles
        assertFalse(banque.acheterMaison());
    }

    @Test
    void testAcheterHotel() {
        assertTrue(banque.acheterHotel());
        assertEquals(11, banque.getHotelsDisponibles());
        // L'achat d'un hotel rend 4 maisons
        assertEquals(36, banque.getMaisonsDisponibles());
    }

    @Test
    void testAcheterHotel_plusDeStock() {
        for (int i = 0; i < 12; i++) {
            assertTrue(banque.acheterHotel());
        }
        assertFalse(banque.acheterHotel());
    }

    @Test
    void testRendreMaisons() {
        banque.acheterMaison();
        banque.acheterMaison();
        assertEquals(30, banque.getMaisonsDisponibles());
        banque.rendreMaisons(2);
        assertEquals(32, banque.getMaisonsDisponibles());
    }

    @Test
    void testRendreHotel() {
        banque.acheterHotel();
        assertEquals(11, banque.getHotelsDisponibles());
        banque.rendreHotel();
        assertEquals(12, banque.getHotelsDisponibles());
    }

    @Test
    void testDistribuerArgent() {
        Joueur joueur = new Joueur("Test", "Pion");
        // Le joueur demarre deja avec 1500
        banque.distribuerArgent(joueur);
        assertEquals(3000, joueur.getArgent());
    }
}
