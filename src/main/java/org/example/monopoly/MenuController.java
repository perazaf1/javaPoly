package org.example.monopoly;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

import org.example.monopoly.model.Partie;
import org.example.monopoly.model.SauvegardeManager;

import java.io.IOException;
import java.util.Arrays;

// Controller du menu principal
public class MenuController {

    @FXML
    private void nouvellePartie() throws IOException {
        HelloApplication.changerScene("config-partie-view.fxml");
    }

    @FXML
    private void chargerPartie() throws IOException {
        String[] sauvegardes = SauvegardeManager.listerSauvegardes();

        if (sauvegardes.length == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chargement");
            alert.setHeaderText("Aucune sauvegarde trouvee");
            alert.setContentText("Il n'y a pas encore de partie sauvegardee.");
            alert.showAndWait();
            return;
        }

        // Dialog pour choisir la sauvegarde
        ChoiceDialog<String> dialog = new ChoiceDialog<>(sauvegardes[0], Arrays.asList(sauvegardes));
        dialog.setTitle("Charger une partie");
        dialog.setHeaderText("Choisissez une sauvegarde");
        dialog.setContentText("Fichier :");

        dialog.showAndWait().ifPresent(fichier -> {
            Partie partie = SauvegardeManager.charger(fichier);
            if (partie != null) {
                try {
                    FXMLLoader loader = HelloApplication.chargerFxml("partie-view.fxml");
                    PartieController controller = loader.getController();
                    controller.initialiser(partie);
                } catch (IOException e) {
                    Alert erreur = new Alert(Alert.AlertType.ERROR);
                    erreur.setTitle("Erreur");
                    erreur.setContentText("Impossible de charger la partie.");
                    erreur.showAndWait();
                }
            }
        });
    }

    @FXML
    private void quitter() {
        Platform.exit();
    }
}
