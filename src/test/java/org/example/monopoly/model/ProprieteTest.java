package org.example.monopoly.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests de la classe Propriete
class ProprieteTest {

    private Propriete propriete;
    private Joueur proprietaire;
    private Joueur autreJoueur;

    @BeforeEach
    void setUp() {
        propriete = new Propriete("Boulevard de Belleville", 1, 60, "Marron",
                new int[]{2, 10, 30, 90, 160, 250}, 50);
        proprietaire = new Joueur("Alice", "Chapeau");
        autreJoueur = new Joueur("Bob", "Voiture");
    }

    @Test
    void testCreation() {
        assertEquals("Boulevard de Belleville", propriete.getNom());
        assertEquals(1, propriete.getPosition());
        assertEquals(60, propriete.getPrix());
        assertEquals("Marron", propriete.getGroupe());
        assertNull(propriete.getProprietaire());
        assertEquals(0, propriete.getNbMaisons());
        assertFalse(propriete.isHotel());
        assertFalse(propriete.isHypothequee());
        assertEquals(50, propriete.getCoutConstruction());
    }

    @Test
    void testCalculerLoyer_terrainNu() {
        propriete.setProprietaire(proprietaire);
        assertEquals(2, propriete.calculerLoyer());
    }

    @Test
    void testCalculerLoyer_terrainNuGroupeComplet() {
        // Donner les 2 proprietes Marron au proprietaire
        Propriete p2 = new Propriete("Rue Lecourbe", 3, 60, "Marron",
                new int[]{4, 20, 60, 180, 320, 450}, 50);
        propriete.setProprietaire(proprietaire);
        proprietaire.ajouterPropriete(propriete);
        proprietaire.ajouterPropriete(p2);
        p2.setProprietaire(proprietaire);

        // Loyer double si groupe complet
        assertEquals(4, propriete.calculerLoyer());
    }

    @Test
    void testCalculerLoyer_avecMaisons() {
        propriete.setProprietaire(proprietaire);
        propriete.construireMaison();
        assertEquals(10, propriete.calculerLoyer()); // loyers[1]

        propriete.construireMaison();
        assertEquals(30, propriete.calculerLoyer()); // loyers[2]

        propriete.construireMaison();
        assertEquals(90, propriete.calculerLoyer()); // loyers[3]

        propriete.construireMaison();
        assertEquals(160, propriete.calculerLoyer()); // loyers[4]
    }

    @Test
    void testCalculerLoyer_hotel() {
        propriete.setProprietaire(proprietaire);
        for (int i = 0; i < 4; i++) propriete.construireMaison();
        propriete.construireHotel();
        assertEquals(250, propriete.calculerLoyer()); // loyers[5]
    }

    @Test
    void testCalculerLoyer_hypothequee() {
        propriete.setProprietaire(proprietaire);
        propriete.setHypothequee(true);
        assertEquals(0, propriete.calculerLoyer());
    }

    @Test
    void testConstruireMaison() {
        assertTrue(propriete.construireMaison());
        assertEquals(1, propriete.getNbMaisons());

        assertTrue(propriete.construireMaison());
        assertTrue(propriete.construireMaison());
        assertTrue(propriete.construireMaison());
        assertEquals(4, propriete.getNbMaisons());

        // On ne peut pas construire plus de 4 maisons
        assertFalse(propriete.construireMaison());
        assertEquals(4, propriete.getNbMaisons());
    }

    @Test
    void testConstruireHotel() {
        // Hotel impossible sans 4 maisons
        assertFalse(propriete.construireHotel());

        // Construire 4 maisons
        for (int i = 0; i < 4; i++) propriete.construireMaison();

        // Maintenant l'hotel est possible
        assertTrue(propriete.construireHotel());
        assertTrue(propriete.isHotel());
        assertEquals(0, propriete.getNbMaisons()); // les maisons sont remplacees

        // Pas de deuxieme hotel
        assertFalse(propriete.construireHotel());
    }

    @Test
    void testActionSurCase_proprieteLibre() {
        int argentAvant = autreJoueur.getArgent();
        propriete.actionSurCase(autreJoueur);
        // Rien ne se passe sur une propriete libre
        assertEquals(argentAvant, autreJoueur.getArgent());
    }

    @Test
    void testActionSurCase_payerLoyer() {
        propriete.setProprietaire(proprietaire);
        proprietaire.ajouterPropriete(propriete);

        int argentPropAvant = proprietaire.getArgent();
        int argentAutreAvant = autreJoueur.getArgent();

        propriete.actionSurCase(autreJoueur);

        // autreJoueur paye 2$ de loyer a proprietaire
        assertEquals(argentAutreAvant - 2, autreJoueur.getArgent());
        assertEquals(argentPropAvant + 2, proprietaire.getArgent());
    }

    @Test
    void testActionSurCase_proprioPropriete() {
        propriete.setProprietaire(proprietaire);
        int argentAvant = proprietaire.getArgent();
        propriete.actionSurCase(proprietaire);
        // Le proprietaire ne paye pas de loyer a lui-meme
        assertEquals(argentAvant, proprietaire.getArgent());
    }

    @Test
    void testActionSurCase_hypothequee() {
        propriete.setProprietaire(proprietaire);
        propriete.setHypothequee(true);
        int argentAvant = autreJoueur.getArgent();
        propriete.actionSurCase(autreJoueur);
        // Pas de loyer sur une propriete hypothequee
        assertEquals(argentAvant, autreJoueur.getArgent());
    }
}
