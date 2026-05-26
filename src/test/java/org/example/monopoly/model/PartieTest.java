package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe Partie
class PartieTest {

    private Partie partie;

    @BeforeEach
    void setUp() {
        partie = new Partie();
        partie.ajouterJoueur("Alice", "Chapeau");
        partie.ajouterJoueur("Bob", "Voiture");
    }

    @Test
    void testCreation() {
        assertEquals(2, partie.getJoueurs().size());
        assertEquals(1, partie.getNumTour());
        assertFalse(partie.isPartieTerminee());
        assertEquals("Alice", partie.getJoueurCourant().getNom());
    }

    @Test
    void testAjouterJoueur_max6() {
        Partie p = new Partie();
        for (int i = 0; i < 6; i++) {
            assertTrue(p.ajouterJoueur("J" + i, "P" + i));
        }
        // Le 7e joueur est refuse
        assertFalse(p.ajouterJoueur("J6", "P6"));
        assertEquals(6, p.getJoueurs().size());
    }

    @Test
    void testLancerDes() {
        partie.lancerDes();
        // Le joueur a ete deplace
        assertNotNull(partie.getDernierMessage());
        assertTrue(partie.getDe().getSomme() >= 2);
    }

    @Test
    void testPasserAuJoueurSuivant() {
        assertEquals(0, partie.getJoueurCourantIndex());
        partie.passerAuJoueurSuivant();
        assertEquals(1, partie.getJoueurCourantIndex());
        assertEquals("Bob", partie.getJoueurCourant().getNom());
    }

    @Test
    void testPasserAuJoueurSuivant_boucle() {
        partie.passerAuJoueurSuivant(); // -> Bob
        partie.passerAuJoueurSuivant(); // -> Alice, numTour++
        assertEquals(0, partie.getJoueurCourantIndex());
        assertEquals(2, partie.getNumTour());
    }

    @Test
    void testCaseEstAchetable_caseDepart() {
        // Le joueur est sur la case Depart (position 0) -> pas achetable
        assertFalse(partie.caseEstAchetable());
    }

    @Test
    void testCaseEstAchetable_proprieteLibre() {
        // Deplacer Alice sur une propriete libre (position 1)
        partie.getJoueurCourant().setPosition(1);
        assertTrue(partie.caseEstAchetable());
    }

    @Test
    void testAcheterPropriete() {
        Joueur alice = partie.getJoueurCourant();
        alice.setPosition(1); // Boulevard de Belleville, 60$

        assertTrue(partie.acheterPropriete());
        assertEquals(1440, alice.getArgent()); // 1500 - 60
        assertEquals(1, alice.getProprietes().size());
    }

    @Test
    void testAcheterPropriete_dejaPrise() {
        Joueur alice = partie.getJoueurCourant();
        alice.setPosition(1);
        partie.acheterPropriete(); // Alice achete

        // Bob essaye d'acheter la meme case
        partie.passerAuJoueurSuivant();
        Joueur bob = partie.getJoueurCourant();
        bob.setPosition(1);
        assertFalse(partie.acheterPropriete());
    }

    @Test
    void testAcheterGare() {
        Joueur alice = partie.getJoueurCourant();
        alice.setPosition(5); // Gare Montparnasse, 200$

        assertTrue(partie.acheterPropriete());
        assertEquals(1300, alice.getArgent());
        assertEquals(1, alice.getGares().size());
    }

    @Test
    void testAcheterCompagnie() {
        Joueur alice = partie.getJoueurCourant();
        alice.setPosition(12); // Compagnie d'electricite, 150$

        assertTrue(partie.acheterPropriete());
        assertEquals(1350, alice.getArgent());
        assertEquals(1, alice.getCompagnies().size());
    }

    @Test
    void testAcheterPropriete_pasAssezArgent() {
        Joueur alice = partie.getJoueurCourant();
        alice.payerArgent(1490); // il reste 10$
        alice.setPosition(1); // propriete a 60$
        assertFalse(partie.acheterPropriete());
    }

    @Test
    void testPayerPourSortirDePrison() {
        Joueur alice = partie.getJoueurCourant();
        alice.allerEnPrison();
        assertTrue(alice.estEnPrison());

        partie.payerPourSortirDePrison();
        assertFalse(alice.estEnPrison());
        assertEquals(1450, alice.getArgent()); // 1500 - 50
    }

    @Test
    void testConstruireMaison() {
        Joueur alice = partie.getJoueurCourant();

        // Donner les 2 proprietes Marron a Alice
        Propriete p1 = (Propriete) partie.getPlateau().getCase(1);
        Propriete p2 = (Propriete) partie.getPlateau().getCase(3);
        p1.setProprietaire(alice);
        p2.setProprietaire(alice);
        alice.ajouterPropriete(p1);
        alice.ajouterPropriete(p2);

        assertTrue(partie.construireMaison(p1));
        assertEquals(1, p1.getNbMaisons());
        assertEquals(1450, alice.getArgent()); // 1500 - 50
    }

    @Test
    void testConstruireMaison_sansGroupeComplet() {
        Joueur alice = partie.getJoueurCourant();
        Propriete p1 = (Propriete) partie.getPlateau().getCase(1);
        p1.setProprietaire(alice);
        alice.ajouterPropriete(p1);

        // Pas de groupe complet -> echec
        assertFalse(partie.construireMaison(p1));
    }

    @Test
    void testConstruireHotel() {
        Joueur alice = partie.getJoueurCourant();

        Propriete p1 = (Propriete) partie.getPlateau().getCase(1);
        Propriete p2 = (Propriete) partie.getPlateau().getCase(3);
        p1.setProprietaire(alice);
        p2.setProprietaire(alice);
        alice.ajouterPropriete(p1);
        alice.ajouterPropriete(p2);

        // Construire 4 maisons
        for (int i = 0; i < 4; i++) {
            assertTrue(partie.construireMaison(p1));
        }
        // Puis l'hotel
        assertTrue(partie.construireHotel(p1));
        assertTrue(p1.isHotel());
    }

    @Test
    void testGetGagnant() {
        Joueur gagnant = partie.getGagnant();
        assertNotNull(gagnant);
    }

    @Test
    void testGetPlateau() {
        assertNotNull(partie.getPlateau());
        assertEquals(40, partie.getPlateau().getNbCases());
    }

    @Test
    void testGetBanque() {
        assertNotNull(partie.getBanque());
    }

    @Test
    void testGetHistorique() {
        assertNotNull(partie.getHistorique());
    }

    @Test
    void testDecks() {
        assertNotNull(partie.getDeckChance());
        assertNotNull(partie.getDeckCommunaute());
        assertEquals(16, partie.getDeckChance().getNbCartes());
        assertEquals(16, partie.getDeckCommunaute().getNbCartes());
    }
}
