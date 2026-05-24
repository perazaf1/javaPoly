package org.example.monopoly;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import org.example.monopoly.model.*;

import java.io.IOException;

// Controller principal de la partie en cours
public class PartieController {

    @FXML private Label labelTour;
    @FXML private Label labelJoueurCourant;
    @FXML private Label labelDes;
    @FXML private Label labelCaseActuelle;
    @FXML private GridPane grillePlateau;
    @FXML private VBox panneauJoueurs;
    @FXML private TextArea logMessages;
    @FXML private Button btnLancerDes;
    @FXML private Button btnAcheter;
    @FXML private Button btnFinTour;
    @FXML private Button btnPayerPrison;
    @FXML private StackPane zoneOverlay;
    @FXML private HBox zonePions;
    @FXML private HBox zoneDes;

    private Partie partie;
    private boolean aLanceLes = false;
    private Label[] labelsCase;

    // Couleurs attribuées à chaque joueur
    private static final String[] COULEURS = {
        "#f38ba8", "#89b4fa", "#a6e3a1", "#f9e2af", "#cba6f7", "#94e2d5"
    };

    // Symboles de pions bien distincts
    private static final String[] SYMBOLES = {
        "\u265A", "\u265B", "\u265C", "\u265D", "\u265E", "\u265F"
    };

    // Faces de dés Unicode
    private static final String[] FACES_DE = {
        "\u2680", "\u2681", "\u2682", "\u2683", "\u2684", "\u2685"
    };

    public void initialiser(Partie partie) {
        this.partie = partie;
        this.labelsCase = new Label[40];

        construirePlateau();
        mettreAJourAffichage();
        ajouterLog("Partie lancée avec " + partie.getJoueurs().size() + " joueurs.");
        ajouterLog("C'est au tour de " + partie.getJoueurCourant().getNom() + ".");
    }

    private void construirePlateau() {
        grillePlateau.getChildren().clear();

        for (int i = 0; i <= 10; i++) {
            labelsCase[i] = creerLabelCase(i);
            grillePlateau.add(labelsCase[i], 10 - i, 10);
        }
        for (int i = 11; i <= 19; i++) {
            labelsCase[i] = creerLabelCase(i);
            grillePlateau.add(labelsCase[i], 0, 10 - (i - 10));
        }
        for (int i = 20; i <= 30; i++) {
            labelsCase[i] = creerLabelCase(i);
            grillePlateau.add(labelsCase[i], i - 20, 0);
        }
        for (int i = 31; i <= 39; i++) {
            labelsCase[i] = creerLabelCase(i);
            grillePlateau.add(labelsCase[i], 10, i - 30);
        }

        // Logo au centre du plateau, incliné comme le vrai Monopoly
        StackPane centre = new StackPane();
        centre.getStyleClass().add("plateau-centre");
        centre.setPrefSize(500, 400);
        centre.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Image logoImage = new Image(getClass().getResourceAsStream("logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(280);
        logoView.setPreserveRatio(true);
        logoView.setRotate(-15);

        centre.getChildren().add(logoView);
        grillePlateau.add(centre, 1, 1, 9, 9);
    }

    private Label creerLabelCase(int position) {
        Case c = partie.getPlateau().getCase(position);
        Label label = new Label(raccourcirNom(c.getNom()));
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add("case-plateau");
        label.setStyle(getStyleCase(c));

        if (position == 0 || position == 10 || position == 20 || position == 30) {
            label.setPrefSize(80, 80);
        } else if ((position > 0 && position < 10) || (position > 20 && position < 30)) {
            label.setPrefSize(62, 80);
        } else {
            label.setPrefSize(80, 46);
        }
        return label;
    }

    private String getStyleCase(Case c) {
        if (c instanceof Propriete) {
            switch (((Propriete) c).getGroupe()) {
                case "Marron":     return "-fx-background-color: #6d4c41; -fx-text-fill: #fff;";
                case "Bleu clair": return "-fx-background-color: #4fc3f7; -fx-text-fill: #1e1e2e;";
                case "Rose":       return "-fx-background-color: #f06292; -fx-text-fill: #fff;";
                case "Orange":     return "-fx-background-color: #ffa726; -fx-text-fill: #1e1e2e;";
                case "Rouge":      return "-fx-background-color: #ef5350; -fx-text-fill: #fff;";
                case "Jaune":      return "-fx-background-color: #fff176; -fx-text-fill: #1e1e2e;";
                case "Vert":       return "-fx-background-color: #66bb6a; -fx-text-fill: #fff;";
                case "Bleu fonce": return "-fx-background-color: #42a5f5; -fx-text-fill: #fff;";
            }
        } else if (c instanceof Gare) {
            return "-fx-background-color: #bdbdbd; -fx-text-fill: #1e1e2e;";
        } else if (c instanceof Compagnie) {
            return "-fx-background-color: #e0e0e0; -fx-text-fill: #1e1e2e;";
        } else if (c instanceof CaseTaxe) {
            return "-fx-background-color: #ef9a9a; -fx-text-fill: #1e1e2e;";
        } else if (c instanceof CaseSpeciale) {
            String type = ((CaseSpeciale) c).getType();
            if (type.equals("AllerEnPrison")) return "-fx-background-color: #ff8a65; -fx-text-fill: #fff;";
            if (type.equals("Chance"))        return "-fx-background-color: #ce93d8; -fx-text-fill: #1e1e2e;";
            if (type.equals("Communaute"))    return "-fx-background-color: #90caf9; -fx-text-fill: #1e1e2e;";
            return "-fx-background-color: #c8e6c9; -fx-text-fill: #1e1e2e;";
        }
        return "-fx-background-color: #e0e0e0;";
    }

    private String raccourcirNom(String nom) {
        if (nom.length() <= 12) return nom;
        return nom.replace("Boulevard", "Bd.")
                   .replace("Avenue", "Av.")
                   .replace("Compagnie de distribution", "Cie")
                   .replace("Caisse de communauté", "Communauté")
                   .replace("Faubourg", "Fg.")
                   .replace("d'électricité", "Élec.")
                   .replace("des eaux", "Eaux");
    }

    @FXML
    private void lancerDes() {
        if (aLanceLes) return;

        boolean rejoue = partie.lancerDes();
        ajouterLog(partie.getDernierMessage());

        De de = partie.getDe();
        String txt = "Dés : " + de.getValeur1() + " + " + de.getValeur2() + " = " + de.getSomme();
        if (de.estDouble()) txt += "  DOUBLE !";
        labelDes.setText(txt);

        // Affichage visuel des dés
        mettreAJourDesVisuels(de.getValeur1(), de.getValeur2());

        mettreAJourAffichage();

        // Popup carte si une carte a été piochée
        if (partie.getDerniereCarte() != null) {
            afficherCartePopup(partie.getDerniereCarte());
        }

        if (partie.isPartieTerminee()) {
            terminerPartie();
            return;
        }

        if (rejoue) {
            ajouterLog(partie.getJoueurCourant().getNom() + " a fait un double, il relance !");
            btnAcheter.setDisable(!partie.caseEstAchetable());
        } else {
            aLanceLes = true;
            btnLancerDes.setDisable(true);
            btnAcheter.setDisable(!partie.caseEstAchetable());
            btnFinTour.setDisable(false);
        }
    }

    @FXML
    private void acheterPropriete() {
        if (partie.acheterPropriete()) {
            ajouterLog(partie.getDernierMessage());
            btnAcheter.setDisable(true);
            mettreAJourAffichage();
        }
    }

    @FXML
    private void finDuTour() {
        partie.passerAuJoueurSuivant();
        aLanceLes = false;
        btnLancerDes.setDisable(false);
        btnAcheter.setDisable(true);
        btnFinTour.setDisable(true);
        mettreAJourAffichage();
        ajouterLog("--- Tour de " + partie.getJoueurCourant().getNom() + " ---");
        btnPayerPrison.setVisible(partie.getJoueurCourant().estEnPrison());
    }

    @FXML
    private void payerPrison() {
        partie.payerPourSortirDePrison();
        ajouterLog(partie.getDernierMessage());
        btnPayerPrison.setVisible(false);
        mettreAJourAffichage();
    }

    @FXML
    private void sauvegarderPartie() {
        TextInputDialog dialog = new TextInputDialog("sauvegarde_tour" + partie.getNumTour());
        dialog.setTitle("Sauvegarder");
        dialog.setHeaderText("Nom de la sauvegarde");
        dialog.setContentText("Nom :");
        dialog.showAndWait().ifPresent(nom -> {
            if (SauvegardeManager.sauvegarder(partie, nom)) {
                ajouterLog("Sauvegarde effectuée : " + nom);
            } else {
                ajouterLog("Erreur de sauvegarde.");
            }
        });
    }

    @FXML
    private void ouvrirStats() throws IOException {
        FXMLLoader loader = HelloApplication.chargerFxml("statistiques-view.fxml");
        StatistiquesController controller = loader.getController();
        controller.initialiser(partie);
    }

    // Affiche les faces de dés visuellement dans le panneau d'actions
    private void mettreAJourDesVisuels(int val1, int val2) {
        zoneDes.getChildren().clear();
        if (val1 >= 1 && val1 <= 6 && val2 >= 1 && val2 <= 6) {
            Label de1 = new Label(FACES_DE[val1 - 1]);
            de1.getStyleClass().add("de-visuel");
            Label de2 = new Label(FACES_DE[val2 - 1]);
            de2.getStyleClass().add("de-visuel");
            zoneDes.getChildren().addAll(de1, de2);
        }
    }

    // Affiche un popup de carte Chance/Communauté au centre du plateau
    private void afficherCartePopup(Carte carte) {
        VBox popup = new VBox(12);
        popup.setAlignment(Pos.CENTER);
        popup.setMaxWidth(380);
        popup.setMaxHeight(240);

        boolean estChance = carte.getTypeCarte().equals("Chance");
        popup.getStyleClass().add(estChance ? "carte-popup-chance" : "carte-popup-communaute");

        String titre = estChance ? "\u2605 CHANCE \u2605" : "\u2605 COMMUNAUT\u00C9 \u2605";
        Label labelTitre = new Label(titre);
        labelTitre.getStyleClass().add("carte-popup-titre");

        Label labelDesc = new Label(carte.getDescription());
        labelDesc.setWrapText(true);
        labelDesc.setMaxWidth(320);
        labelDesc.getStyleClass().add("carte-popup-description");

        Button btnFermer = new Button("Fermer");
        btnFermer.getStyleClass().add("btn-primary");
        btnFermer.setOnAction(e -> zoneOverlay.getChildren().remove(popup));

        popup.getChildren().addAll(labelTitre, labelDesc, btnFermer);
        zoneOverlay.getChildren().add(popup);

        // Auto-dismiss après 5 secondes
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> zoneOverlay.getChildren().remove(popup));
        pause.play();
    }

    // Met à jour la zone pions au-dessus du plateau
    private void mettreAJourZonePions() {
        zonePions.getChildren().clear();
        for (int i = 0; i < partie.getJoueurs().size(); i++) {
            Joueur j = partie.getJoueurs().get(i);
            if (j.estEnFaillite()) continue;

            String couleur = COULEURS[i % COULEURS.length];
            String sym = SYMBOLES[i % SYMBOLES.length];
            Case caseCourante = partie.getPlateau().getCase(j.getPosition());
            boolean estCourant = (i == partie.getJoueurCourantIndex());

            VBox pion = new VBox(2);
            pion.setAlignment(Pos.CENTER);
            pion.getStyleClass().add("pion-display");
            if (estCourant) {
                pion.setStyle("-fx-border-color: " + couleur + "; -fx-border-width: 2; -fx-border-radius: 8;");
            }

            Label symLabel = new Label(sym);
            symLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: " + couleur + ";");

            Label nomLabel = new Label(j.getNom());
            nomLabel.setStyle("-fx-text-fill: " + couleur + "; -fx-font-size: 11px; -fx-font-weight: bold;");

            Label posLabel = new Label(raccourcirNom(caseCourante.getNom()));
            posLabel.setStyle("-fx-text-fill: #6c7086; -fx-font-size: 9px;");

            Label argentLabel = new Label(j.getArgent() + "$");
            argentLabel.setStyle("-fx-text-fill: #a6e3a1; -fx-font-size: 10px;");

            pion.getChildren().addAll(symLabel, nomLabel, posLabel, argentLabel);
            zonePions.getChildren().add(pion);
        }
    }

    private void mettreAJourAffichage() {
        Joueur joueurCourant = partie.getJoueurCourant();
        int indexCourant = partie.getJoueurCourantIndex();

        labelTour.setText("Tour " + partie.getNumTour());
        String symbole = SYMBOLES[indexCourant % SYMBOLES.length];
        labelJoueurCourant.setText(symbole + " " + joueurCourant.getNom());

        Case caseActuelle = partie.getPlateau().getCase(joueurCourant.getPosition());
        labelCaseActuelle.setText(caseActuelle.getNom() + "\n" + getInfoCase(caseActuelle));

        // Panneau joueurs avec cartes
        panneauJoueurs.getChildren().clear();
        for (int i = 0; i < partie.getJoueurs().size(); i++) {
            Joueur j = partie.getJoueurs().get(i);
            String couleur = COULEURS[i % COULEURS.length];
            String sym = SYMBOLES[i % SYMBOLES.length];
            boolean estCourant = (i == indexCourant);

            VBox carte = new VBox(2);
            carte.getStyleClass().add(estCourant ? "carte-joueur-actif" : "carte-joueur");

            String etat = "";
            if (j.estEnPrison()) etat = "  [PRISON]";
            else if (j.estEnFaillite()) etat = "  [FAILLITE]";

            Label nomLabel = new Label(sym + " " + j.getNom() + etat);
            nomLabel.setStyle("-fx-text-fill: " + couleur + "; -fx-font-size: 13px;"
                + (estCourant ? " -fx-font-weight: bold;" : ""));
            if (j.estEnFaillite()) {
                nomLabel.setStyle("-fx-text-fill: #585b70; -fx-font-size: 13px; -fx-strikethrough: true;");
            }

            Label infoLabel = new Label(j.getArgent() + "$ | "
                + j.getProprietes().size() + " prop. | "
                + j.getGares().size() + " gares | "
                + j.getCompagnies().size() + " cie");
            infoLabel.setStyle("-fx-text-fill: #6c7086; -fx-font-size: 11px;");

            carte.getChildren().addAll(nomLabel, infoLabel);
            panneauJoueurs.getChildren().add(carte);
        }

        mettreAJourPions();
        mettreAJourZonePions();
    }

    private void mettreAJourPions() {
        // Remettre toutes les cases à leur état normal
        for (int i = 0; i < 40; i++) {
            Case c = partie.getPlateau().getCase(i);
            labelsCase[i].setText(raccourcirNom(c.getNom()));
            labelsCase[i].setStyle(getStyleCase(c));
            labelsCase[i].getStyleClass().remove("case-joueur-present");
        }

        // Placer les pions : symbole coloré + bordure sur la case
        for (int i = 0; i < partie.getJoueurs().size(); i++) {
            Joueur j = partie.getJoueurs().get(i);
            if (j.estEnFaillite()) continue;

            int pos = j.getPosition();
            String couleur = COULEURS[i % COULEURS.length];
            String sym = SYMBOLES[i % SYMBOLES.length];

            String texte = labelsCase[pos].getText();
            labelsCase[pos].setText(texte + "\n" + sym);

            if (!labelsCase[pos].getStyleClass().contains("case-joueur-present")) {
                labelsCase[pos].getStyleClass().add("case-joueur-present");
            }

            String styleActuel = labelsCase[pos].getStyle();
            labelsCase[pos].setStyle(styleActuel + " -fx-border-color: " + couleur + "; -fx-border-width: 2.5;");
        }
    }

    private String getInfoCase(Case c) {
        if (c instanceof Propriete) {
            Propriete p = (Propriete) c;
            String info = "Prix: " + p.getPrix() + "$ | Loyer: " + p.calculerLoyer() + "$";
            if (p.getProprietaire() != null) {
                info += "\nProp: " + p.getProprietaire().getNom();
                if (p.isHotel()) info += " | HÔTEL";
                else if (p.getNbMaisons() > 0) info += " | " + p.getNbMaisons() + " maison(s)";
            } else {
                info += "\nÀ vendre !";
            }
            return info;
        } else if (c instanceof Gare) {
            Gare g = (Gare) c;
            if (g.getProprietaire() != null)
                return "200$ | " + g.getProprietaire().getNom() + " | Loyer: " + g.calculerLoyer() + "$";
            return "200$ | À vendre !";
        } else if (c instanceof Compagnie) {
            Compagnie comp = (Compagnie) c;
            if (comp.getProprietaire() != null)
                return "150$ | " + comp.getProprietaire().getNom();
            return "150$ | À vendre !";
        } else if (c instanceof CaseTaxe) {
            return "Taxe de " + ((CaseTaxe) c).getMontant() + "$";
        }
        return "";
    }

    private void ajouterLog(String message) {
        logMessages.appendText(message + "\n");
    }

    private void terminerPartie() {
        Joueur gagnant = partie.getGagnant();
        btnLancerDes.setDisable(true);
        btnAcheter.setDisable(true);
        btnFinTour.setDisable(true);

        if (gagnant != null) {
            ajouterLog("=== " + gagnant.getNom() + " remporte la partie ! ===");
            ajouterLog("Patrimoine : " + gagnant.getPatrimoineTotal() + "$");
        }

        Button btnRetour = new Button("Retour au menu");
        btnRetour.getStyleClass().add("btn-primary");
        btnRetour.setPrefWidth(275);
        btnRetour.setOnAction(e -> {
            try { HelloApplication.changerScene("menu-view.fxml"); }
            catch (IOException ex) { ajouterLog("Erreur retour menu."); }
        });
        panneauJoueurs.getChildren().add(btnRetour);
    }
}
