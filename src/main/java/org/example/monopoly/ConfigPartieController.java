package org.example.monopoly;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.example.monopoly.model.Partie;

import java.io.IOException;
import java.util.ArrayList;

// Controller pour la configuration d'une nouvelle partie
public class ConfigPartieController {

    @FXML private Spinner<Integer> spinnerNbJoueurs;
    @FXML private VBox zoneJoueurs;
    @FXML private Label labelErreur;
    @FXML private Button btnLancer;

    private ArrayList<TextField> champsNom = new ArrayList<>();
    private ArrayList<ComboBox<String>> champsPion = new ArrayList<>();

    private String[] pionsDisponibles = {
        "Chapeau", "Voiture", "Chien", "Chat", "Bateau", "Chaussure"
    };

    // Initialisation du spinner apres le chargement du FXML
    @FXML
    private void initialize() {
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2);
        spinnerNbJoueurs.setValueFactory(factory);
    }

    // Genere les champs de saisie pour chaque joueur
    @FXML
    private void validerNbJoueurs() {
        int nbJoueurs = spinnerNbJoueurs.getValue();
        zoneJoueurs.getChildren().clear();
        champsNom.clear();
        champsPion.clear();

        for (int i = 0; i < nbJoueurs; i++) {
            HBox ligne = new HBox(10);
            ligne.setAlignment(javafx.geometry.Pos.CENTER);

            Label label = new Label("Joueur " + (i + 1) + " :");
            label.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
            label.setPrefWidth(80);

            TextField champNom = new TextField();
            champNom.setPromptText("Nom du joueur");
            champNom.setPrefWidth(200);
            champsNom.add(champNom);

            ComboBox<String> comboPion = new ComboBox<>();
            for (String pion : pionsDisponibles) {
                comboPion.getItems().add(pion);
            }
            comboPion.setValue(pionsDisponibles[i]);
            comboPion.setPrefWidth(150);
            champsPion.add(comboPion);

            ligne.getChildren().addAll(label, champNom, comboPion);
            zoneJoueurs.getChildren().add(ligne);
        }

        btnLancer.setDisable(false);
        labelErreur.setText("");
    }

    // Lance la partie avec les joueurs configures
    @FXML
    private void lancerPartie() throws IOException {
        // Verification des noms
        for (int i = 0; i < champsNom.size(); i++) {
            String nom = champsNom.get(i).getText().trim();
            if (nom.isEmpty()) {
                labelErreur.setText("Tous les joueurs doivent avoir un nom !");
                return;
            }
        }

        // Verification des pions uniques
        ArrayList<String> pionsChoisis = new ArrayList<>();
        for (ComboBox<String> combo : champsPion) {
            String pion = combo.getValue();
            if (pionsChoisis.contains(pion)) {
                labelErreur.setText("Chaque joueur doit avoir un pion different !");
                return;
            }
            pionsChoisis.add(pion);
        }

        // Creation de la partie
        Partie partie = new Partie();
        for (int i = 0; i < champsNom.size(); i++) {
            String nom = champsNom.get(i).getText().trim();
            String pion = champsPion.get(i).getValue();
            partie.ajouterJoueur(nom, pion);
        }

        // Charger la vue de jeu et lui passer la partie
        FXMLLoader loader = HelloApplication.chargerFxml("partie-view.fxml");
        PartieController controller = loader.getController();
        controller.initialiser(partie);
    }

    @FXML
    private void retourMenu() throws IOException {
        HelloApplication.changerScene("menu-view.fxml");
    }
}
