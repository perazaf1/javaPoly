package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

// Tests de la classe Historique
class HistoriqueTest {

    private Historique historique;

    @BeforeEach
    void setUp() {
        historique = new Historique();
    }

    @Test
    void testCreation() {
        assertTrue(historique.getEvenements().isEmpty());
        assertTrue(historique.getFortuneParTour().isEmpty());
    }

    @Test
    void testAjouterEvenement() {
        historique.ajouterEvenement("Alice achete une propriete");
        assertEquals(1, historique.getEvenements().size());
        assertEquals("Alice achete une propriete", historique.getEvenements().get(0));
    }

    @Test
    void testAjouterFortune() {
        historique.ajouterFortune("Alice", 1500);
        historique.ajouterFortune("Alice", 1700);

        ArrayList<Integer> fortunes = historique.getFortuneJoueur("Alice");
        assertNotNull(fortunes);
        assertEquals(2, fortunes.size());
        assertEquals(1500, fortunes.get(0));
        assertEquals(1700, fortunes.get(1));
    }

    @Test
    void testEnregistrerTour() {
        ArrayList<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new Joueur("Alice", "Chapeau"));
        joueurs.add(new Joueur("Bob", "Voiture"));

        historique.enregistrerTour(joueurs);

        assertNotNull(historique.getFortuneJoueur("Alice"));
        assertNotNull(historique.getFortuneJoueur("Bob"));
        assertEquals(1500, historique.getFortuneJoueur("Alice").get(0));
        assertEquals(1500, historique.getFortuneJoueur("Bob").get(0));
    }

    @Test
    void testGetFortuneJoueur_inexistant() {
        assertNull(historique.getFortuneJoueur("Inconnu"));
    }
}
