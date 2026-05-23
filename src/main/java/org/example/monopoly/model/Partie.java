package org.example.monopoly.model;

import java.util.ArrayList;

// Gère le déroulement d'une partie de Monopoly
public class Partie {

    private ArrayList<Joueur> joueurs;
    private Plateau plateau;
    private Banque banque;
    private De de;
    private DeckCartes deckChance;
    private DeckCartes deckCommunaute;

    private int joueurCourantIndex;
    private int numTour;
    private boolean partieTerminee;
    private int nbDoublesConsecutifs;

    // Dernière action pour que le controller sache quoi afficher
    private String dernierMessage;

    // Dernière carte piochée (pour le popup)
    private Carte derniereCarte;

    // Suivi des statistiques
    private Historique historique;

    public Partie() {
        this.joueurs = new ArrayList<>();
        this.plateau = new Plateau();
        this.banque = new Banque();
        this.de = new De();
        this.deckChance = new DeckCartes();
        this.deckCommunaute = new DeckCartes();
        this.historique = new Historique();

        this.joueurCourantIndex = 0;
        this.numTour = 1;
        this.partieTerminee = false;
        this.nbDoublesConsecutifs = 0;
        this.dernierMessage = "";
        this.derniereCarte = null;

        initialiserCartes();
    }

    // Ajoute un joueur à la partie (2 à 6 joueurs)
    public boolean ajouterJoueur(String nom, String pion) {
        if (joueurs.size() >= 6) {
            return false;
        }
        joueurs.add(new Joueur(nom, pion));
        return true;
    }

    // Lance les dés et déplace le joueur courant
    // Retourne true si le joueur peut rejouer (double), false sinon
    public boolean lancerDes() {
        Joueur joueur = getJoueurCourant();
        derniereCarte = null;
        de.lancer();
        dernierMessage = joueur.getNom() + " lance les dés : " + de.toString();

        // Gestion de la prison
        if (joueur.estEnPrison()) {
            return gererPrison(joueur);
        }

        // Vérification des 3 doubles consécutifs
        if (de.estDouble()) {
            nbDoublesConsecutifs++;
            if (nbDoublesConsecutifs >= 3) {
                joueur.allerEnPrison();
                dernierMessage += "\n3 doubles consécutifs ! " + joueur.getNom() + " va en prison.";
                nbDoublesConsecutifs = 0;
                return false;
            }
        } else {
            nbDoublesConsecutifs = 0;
        }

        // Déplacement du joueur
        joueur.deplacer(de.getSomme());
        Case caseCourante = plateau.getCase(joueur.getPosition());
        dernierMessage += "\n" + joueur.getNom() + " avance sur " + caseCourante.getNom();

        // Traitement de la case
        traiterCase(joueur, caseCourante);

        // Vérification faillite
        if (joueur.getArgent() < 0) {
            eliminerJoueur(joueur);
        }

        // Double = rejouer (sauf prison)
        return de.estDouble() && !joueur.estEnPrison();
    }

    // Gère la prison : payer 50 ou tenter un double
    private boolean gererPrison(Joueur joueur) {
        joueur.setToursEnPrison(joueur.getToursEnPrison() + 1);

        if (de.estDouble()) {
            joueur.sortirDePrison();
            dernierMessage += "\nDouble ! " + joueur.getNom() + " sort de prison.";

            joueur.deplacer(de.getSomme());
            Case caseCourante = plateau.getCase(joueur.getPosition());
            dernierMessage += "\n" + joueur.getNom() + " avance sur " + caseCourante.getNom();
            traiterCase(joueur, caseCourante);
            return false;
        }

        // Au 3ème tour en prison, obligation de payer
        if (joueur.getToursEnPrison() >= 3) {
            joueur.payerArgent(50);
            joueur.sortirDePrison();
            dernierMessage += "\n" + joueur.getNom() + " paye 50$ et sort de prison.";

            joueur.deplacer(de.getSomme());
            Case caseCourante = plateau.getCase(joueur.getPosition());
            dernierMessage += "\n" + joueur.getNom() + " avance sur " + caseCourante.getNom();
            traiterCase(joueur, caseCourante);
        } else {
            dernierMessage += "\nPas de double. " + joueur.getNom() + " reste en prison.";
        }

        return false;
    }

    // Le joueur en prison choisit de payer 50 pour sortir
    public void payerPourSortirDePrison() {
        Joueur joueur = getJoueurCourant();
        if (joueur.estEnPrison()) {
            joueur.payerArgent(50);
            joueur.sortirDePrison();
            dernierMessage = joueur.getNom() + " paye 50$ et sort de prison.";
        }
    }

    // Traite les effets de la case sur laquelle le joueur est arrivé
    private void traiterCase(Joueur joueur, Case caseCourante) {
        if (caseCourante instanceof CaseSpeciale) {
            CaseSpeciale cs = (CaseSpeciale) caseCourante;

            // Pioche de carte Chance ou Communauté
            if (cs.getType().equals("Chance")) {
                Carte carte = deckChance.piocher();
                if (carte != null) {
                    carte.appliquerEffet(joueur);
                    derniereCarte = carte;
                    dernierMessage += "\nCarte Chance : " + carte.getDescription();
                }
                return;
            } else if (cs.getType().equals("Communaute")) {
                Carte carte = deckCommunaute.piocher();
                if (carte != null) {
                    carte.appliquerEffet(joueur);
                    derniereCarte = carte;
                    dernierMessage += "\nCaisse de communauté : " + carte.getDescription();
                }
                return;
            }
        }

        // Pour les autres cases (Propriété, Gare, Compagnie, Taxe, etc.)
        caseCourante.actionSurCase(joueur);

        // Messages spécifiques selon le type de case
        if (caseCourante instanceof CaseTaxe) {
            CaseTaxe ct = (CaseTaxe) caseCourante;
            dernierMessage += "\n" + joueur.getNom() + " paye " + ct.getMontant() + "$ de taxe.";
        } else if (caseCourante instanceof CaseSpeciale) {
            CaseSpeciale cs = (CaseSpeciale) caseCourante;
            if (cs.getType().equals("AllerEnPrison")) {
                dernierMessage += "\n" + joueur.getNom() + " va en prison !";
            }
        }
    }

    // Le joueur courant achète la propriété sur laquelle il se trouve
    public boolean acheterPropriete() {
        Joueur joueur = getJoueurCourant();
        Case caseCourante = plateau.getCase(joueur.getPosition());

        if (caseCourante instanceof Propriete) {
            Propriete prop = (Propriete) caseCourante;
            if (prop.getProprietaire() == null && joueur.getArgent() >= prop.getPrix()) {
                joueur.payerArgent(prop.getPrix());
                prop.setProprietaire(joueur);
                joueur.ajouterPropriete(prop);
                dernierMessage = joueur.getNom() + " achète " + prop.getNom() + " pour " + prop.getPrix() + "$.";
                return true;
            }
        } else if (caseCourante instanceof Gare) {
            Gare gare = (Gare) caseCourante;
            if (gare.getProprietaire() == null && joueur.getArgent() >= gare.getPrix()) {
                joueur.payerArgent(gare.getPrix());
                gare.setProprietaire(joueur);
                joueur.ajouterGare(gare);
                dernierMessage = joueur.getNom() + " achète " + gare.getNom() + " pour " + gare.getPrix() + "$.";
                return true;
            }
        } else if (caseCourante instanceof Compagnie) {
            Compagnie comp = (Compagnie) caseCourante;
            if (comp.getProprietaire() == null && joueur.getArgent() >= comp.getPrix()) {
                joueur.payerArgent(comp.getPrix());
                comp.setProprietaire(joueur);
                joueur.ajouterCompagnie(comp);
                dernierMessage = joueur.getNom() + " achète " + comp.getNom() + " pour " + comp.getPrix() + "$.";
                return true;
            }
        }

        return false;
    }

    // Vérifie si la case actuelle du joueur est achetable (libre et assez d'argent)
    public boolean caseEstAchetable() {
        Joueur joueur = getJoueurCourant();
        Case caseCourante = plateau.getCase(joueur.getPosition());

        if (caseCourante instanceof Propriete) {
            Propriete p = (Propriete) caseCourante;
            return p.getProprietaire() == null && joueur.getArgent() >= p.getPrix();
        } else if (caseCourante instanceof Gare) {
            Gare g = (Gare) caseCourante;
            return g.getProprietaire() == null && joueur.getArgent() >= g.getPrix();
        } else if (caseCourante instanceof Compagnie) {
            Compagnie c = (Compagnie) caseCourante;
            return c.getProprietaire() == null && joueur.getArgent() >= c.getPrix();
        }

        return false;
    }

    // Passe au joueur suivant (saute les joueurs en faillite)
    public void passerAuJoueurSuivant() {
        nbDoublesConsecutifs = 0;
        do {
            joueurCourantIndex = (joueurCourantIndex + 1) % joueurs.size();

            // Si on revient au premier joueur, on incrémente le tour
            if (joueurCourantIndex == 0) {
                numTour++;
                // Enregistrer la fortune de chaque joueur pour les stats
                historique.enregistrerTour(joueurs);
            }
        } while (getJoueurCourant().estEnFaillite() && !partieTerminee);
    }

    // Élimine un joueur (faillite)
    private void eliminerJoueur(Joueur joueur) {
        joueur.setEtat("FAILLITE");
        dernierMessage += "\n" + joueur.getNom() + " est en faillite !";

        // Rendre toutes ses propriétés à la banque
        for (Propriete p : joueur.getProprietes()) {
            p.setProprietaire(null);
        }
        for (Gare g : joueur.getGares()) {
            g.setProprietaire(null);
        }
        for (Compagnie c : joueur.getCompagnies()) {
            c.setProprietaire(null);
        }

        // Vérifier s'il ne reste qu'un joueur en jeu
        int joueursEnJeu = 0;
        for (Joueur j : joueurs) {
            if (!j.estEnFaillite()) {
                joueursEnJeu++;
            }
        }
        if (joueursEnJeu <= 1) {
            partieTerminee = true;
            dernierMessage += "\nPartie terminée !";
        }
    }

    // Retourne le gagnant (dernier joueur en jeu ou le plus riche)
    public Joueur getGagnant() {
        Joueur gagnant = null;
        for (Joueur j : joueurs) {
            if (!j.estEnFaillite()) {
                if (gagnant == null || j.getPatrimoineTotal() > gagnant.getPatrimoineTotal()) {
                    gagnant = j;
                }
            }
        }
        return gagnant;
    }

    // Construit une maison sur une propriété du joueur courant
    public boolean construireMaison(Propriete propriete) {
        Joueur joueur = getJoueurCourant();

        if (propriete.getProprietaire() != joueur) {
            return false;
        }
        if (!joueur.possedeGroupeComplet(propriete.getGroupe())) {
            return false;
        }
        if (joueur.getArgent() < propriete.getCoutConstruction()) {
            return false;
        }
        if (!banque.acheterMaison()) {
            return false;
        }
        if (!propriete.construireMaison()) {
            banque.rendreMaisons(1);
            return false;
        }

        joueur.payerArgent(propriete.getCoutConstruction());
        dernierMessage = joueur.getNom() + " construit une maison sur " + propriete.getNom() + ".";
        return true;
    }

    // Construit un hôtel sur une propriété du joueur courant
    public boolean construireHotel(Propriete propriete) {
        Joueur joueur = getJoueurCourant();

        if (propriete.getProprietaire() != joueur) {
            return false;
        }
        if (!joueur.possedeGroupeComplet(propriete.getGroupe())) {
            return false;
        }
        if (joueur.getArgent() < propriete.getCoutConstruction()) {
            return false;
        }
        if (!banque.acheterHotel()) {
            return false;
        }
        if (!propriete.construireHotel()) {
            banque.rendreHotel();
            return false;
        }

        joueur.payerArgent(propriete.getCoutConstruction());
        dernierMessage = joueur.getNom() + " construit un hôtel sur " + propriete.getNom() + " !";
        return true;
    }

    // Initialise les 16 cartes Chance et 16 cartes Caisse de communauté
    private void initialiserCartes() {
        // Cartes Chance
        deckChance.ajouterCarte(new Carte("Rendez-vous Rue de la Paix", "Chance", "Deplacement", 39));
        deckChance.ajouterCarte(new Carte("Avancez jusqu'à la case Départ", "Chance", "Deplacement", 0));
        deckChance.ajouterCarte(new Carte("Avancez au Boulevard Henri-Martin", "Chance", "Deplacement", 24));
        deckChance.ajouterCarte(new Carte("Avancez à l'Avenue Mozart", "Chance", "Deplacement", 16));
        deckChance.ajouterCarte(new Carte("Allez à la Gare Montparnasse", "Chance", "Deplacement", 5));
        deckChance.ajouterCarte(new Carte("Allez à la Gare de Lyon", "Chance", "Deplacement", 15));
        deckChance.ajouterCarte(new Carte("La banque vous verse 50$", "Chance", "ArgentGain", 50));
        deckChance.ajouterCarte(new Carte("Vous avez gagné 150$", "Chance", "ArgentGain", 150));
        deckChance.ajouterCarte(new Carte("Amende pour excès de vitesse : 15$", "Chance", "ArgentPerte", 15));
        deckChance.ajouterCarte(new Carte("Payez une amende de 100$", "Chance", "ArgentPerte", 100));
        deckChance.ajouterCarte(new Carte("Allez en prison", "Chance", "Prison", 0));
        deckChance.ajouterCarte(new Carte("Faites des réparations : 25$ par maison", "Chance", "Reparations", 25));
        deckChance.ajouterCarte(new Carte("Vous êtes imposé : 20$", "Chance", "ArgentPerte", 20));
        deckChance.ajouterCarte(new Carte("La banque vous verse 100$", "Chance", "ArgentGain", 100));
        deckChance.ajouterCarte(new Carte("Recevez 200$ de dividendes", "Chance", "ArgentGain", 200));
        deckChance.ajouterCarte(new Carte("Reculez de 3 cases", "Chance", "ArgentPerte", 0));

        // Cartes Caisse de communauté
        deckCommunaute.ajouterCarte(new Carte("Avancez jusqu'à la case Départ", "Communaute", "Deplacement", 0));
        deckCommunaute.ajouterCarte(new Carte("Erreur de la banque, recevez 200$", "Communaute", "ArgentGain", 200));
        deckCommunaute.ajouterCarte(new Carte("Payez la note du médecin : 50$", "Communaute", "ArgentPerte", 50));
        deckCommunaute.ajouterCarte(new Carte("Vous héritez de 100$", "Communaute", "ArgentGain", 100));
        deckCommunaute.ajouterCarte(new Carte("Payez votre assurance : 50$", "Communaute", "ArgentPerte", 50));
        deckCommunaute.ajouterCarte(new Carte("Recevez votre intérêt : 25$", "Communaute", "ArgentGain", 25));
        deckCommunaute.ajouterCarte(new Carte("Les réparations coûtent 40$ par maison", "Communaute", "Reparations", 40));
        deckCommunaute.ajouterCarte(new Carte("Vous avez gagné le deuxième prix de beauté : 10$", "Communaute", "ArgentGain", 10));
        deckCommunaute.ajouterCarte(new Carte("Allez en prison", "Communaute", "Prison", 0));
        deckCommunaute.ajouterCarte(new Carte("Recevez 20$ pour votre anniversaire", "Communaute", "ArgentGain", 20));
        deckCommunaute.ajouterCarte(new Carte("Payez l'hôpital : 100$", "Communaute", "ArgentPerte", 100));
        deckCommunaute.ajouterCarte(new Carte("Retour à Belleville", "Communaute", "Deplacement", 1));
        deckCommunaute.ajouterCarte(new Carte("Vous avez gagné 50$ au concours", "Communaute", "ArgentGain", 50));
        deckCommunaute.ajouterCarte(new Carte("La vente de votre stock rapporte 45$", "Communaute", "ArgentGain", 45));
        deckCommunaute.ajouterCarte(new Carte("Payez la taxe scolaire : 50$", "Communaute", "ArgentPerte", 50));
        deckCommunaute.ajouterCarte(new Carte("Recevez 100$ de placement", "Communaute", "ArgentGain", 100));

        deckChance.melanger();
        deckCommunaute.melanger();
    }

    // Getters
    public Joueur getJoueurCourant() {
        return joueurs.get(joueurCourantIndex);
    }

    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Banque getBanque() {
        return banque;
    }

    public De getDe() {
        return de;
    }

    public int getNumTour() {
        return numTour;
    }

    public boolean isPartieTerminee() {
        return partieTerminee;
    }

    public String getDernierMessage() {
        return dernierMessage;
    }

    public int getJoueurCourantIndex() {
        return joueurCourantIndex;
    }

    public DeckCartes getDeckChance() {
        return deckChance;
    }

    public DeckCartes getDeckCommunaute() {
        return deckCommunaute;
    }

    public Historique getHistorique() {
        return historique;
    }

    public Carte getDerniereCarte() {
        return derniereCarte;
    }

    // Setters utilisés par SauvegardeManager pour restaurer l'état
    public void setNumTour(int numTour) {
        this.numTour = numTour;
    }

    public void setJoueurCourantIndex(int index) {
        this.joueurCourantIndex = index;
    }
}
