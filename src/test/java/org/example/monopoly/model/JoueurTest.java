package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe Joueur
class JoueurTest {

    private Joueur joueur;

    @BeforeEach
    void setUp() {
        joueur = new Joueur("Alice", "Chapeau");
    }

    @Test
    void testCreation() {
        assertEquals("Alice", joueur.getNom());
        assertEquals("Chapeau", joueur.getPion());
        assertEquals(1500, joueur.getArgent());
        assertEquals(0, joueur.getPosition());
        assertEquals("EN_JEU", joueur.getEtat());
    }

    @Test
    void testDeplacer() {
        joueur.deplacer(5);
        assertEquals(5, joueur.getPosition());

        joueur.deplacer(3);
        assertEquals(8, joueur.getPosition());
    }

    @Test
    void testDeplacer_passageCaseDepart() {
        joueur.setPosition(38);
        joueur.deplacer(5); // 38 + 5 = 43 -> 3 (modulo 40)
        assertEquals(3, joueur.getPosition());
        // Le joueur recoit 200$ en passant par la case depart
        assertEquals(1700, joueur.getArgent());
    }

    @Test
    void testDeplacerVers() {
        joueur.setPosition(30);
        joueur.deplacerVers(5); // recule = passe par depart
        assertEquals(5, joueur.getPosition());
        assertEquals(1700, joueur.getArgent()); // +200 pour le passage
    }

    @Test
    void testDeplacerVers_sansBonusSiPrison() {
        joueur.setPosition(30);
        joueur.setEtat("EN_PRISON");
        joueur.deplacerVers(10); // va en prison, pas de bonus
        assertEquals(10, joueur.getPosition());
        assertEquals(1500, joueur.getArgent());
    }

    @Test
    void testPayerArgent() {
        joueur.payerArgent(300);
        assertEquals(1200, joueur.getArgent());
    }

    @Test
    void testRecevoirArgent() {
        joueur.recevoirArgent(500);
        assertEquals(2000, joueur.getArgent());
    }

    @Test
    void testAllerEnPrison() {
        joueur.allerEnPrison();
        assertEquals(10, joueur.getPosition());
        assertTrue(joueur.estEnPrison());
        assertEquals(0, joueur.getToursEnPrison());
    }

    @Test
    void testSortirDePrison() {
        joueur.allerEnPrison();
        joueur.sortirDePrison();
        assertFalse(joueur.estEnPrison());
        assertEquals("EN_JEU", joueur.getEtat());
    }

    @Test
    void testEstEnFaillite() {
        assertFalse(joueur.estEnFaillite());
        joueur.setEtat("FAILLITE");
        assertTrue(joueur.estEnFaillite());
    }

    @Test
    void testAjouterPropriete() {
        Propriete p = new Propriete("Test", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        joueur.ajouterPropriete(p);
        assertEquals(1, joueur.getProprietes().size());
        assertEquals(p, joueur.getProprietes().get(0));
    }

    @Test
    void testAjouterGare() {
        Gare g = new Gare("Gare Test", 5);
        joueur.ajouterGare(g);
        assertEquals(1, joueur.getGares().size());
    }

    @Test
    void testAjouterCompagnie() {
        Compagnie c = new Compagnie("Cie Test", 12);
        joueur.ajouterCompagnie(c);
        assertEquals(1, joueur.getCompagnies().size());
    }

    @Test
    void testGetPatrimoineTotal() {
        // 1500 de base
        assertEquals(1500, joueur.getPatrimoineTotal());

        // Ajout d'une propriete a 60$
        Propriete p = new Propriete("Test", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        joueur.ajouterPropriete(p);
        assertEquals(1560, joueur.getPatrimoineTotal());

        // Ajout d'une gare a 200$
        Gare g = new Gare("Gare", 5);
        joueur.ajouterGare(g);
        assertEquals(1760, joueur.getPatrimoineTotal());

        // Ajout d'une compagnie a 150$
        Compagnie c = new Compagnie("Cie", 12);
        joueur.ajouterCompagnie(c);
        assertEquals(1910, joueur.getPatrimoineTotal());
    }

    @Test
    void testPatrimoineAvecMaisons() {
        Propriete p = new Propriete("Test", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        joueur.ajouterPropriete(p);
        p.construireMaison(); // +50 de cout construction
        // patrimoine = 1500 + 60 (prix) + 50 (1 maison * 50) = 1610
        assertEquals(1610, joueur.getPatrimoineTotal());
    }

    @Test
    void testPossedeGroupeComplet_marron() {
        // Le groupe Marron a 2 proprietes
        Propriete p1 = new Propriete("P1", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        Propriete p2 = new Propriete("P2", 3, 60, "Marron",
                new int[]{4, 20, 60, 180, 320, 450}, 50);

        joueur.ajouterPropriete(p1);
        assertFalse(joueur.possedeGroupeComplet("Marron"));

        joueur.ajouterPropriete(p2);
        assertTrue(joueur.possedeGroupeComplet("Marron"));
    }

    @Test
    void testPossedeGroupeComplet_groupeDeTrois() {
        // Les groupes normaux ont 3 proprietes
        Propriete p1 = new Propriete("P1", 6, 100, "Bleu clair",
                new int[]{6, 30, 90, 270, 400, 550}, 50);
        Propriete p2 = new Propriete("P2", 8, 100, "Bleu clair",
                new int[]{6, 30, 90, 270, 400, 550}, 50);
        Propriete p3 = new Propriete("P3", 9, 120, "Bleu clair",
                new int[]{8, 40, 100, 300, 450, 600}, 50);

        joueur.ajouterPropriete(p1);
        joueur.ajouterPropriete(p2);
        assertFalse(joueur.possedeGroupeComplet("Bleu clair"));

        joueur.ajouterPropriete(p3);
        assertTrue(joueur.possedeGroupeComplet("Bleu clair"));
    }

    @Test
    void testToString() {
        String str = joueur.toString();
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("Chapeau"));
        assertTrue(str.contains("1500"));
    }
}
