package org.example.monopoly;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage stagePrincipal;

    @Override
    public void start(Stage stage) throws IOException {
        stagePrincipal = stage;
        stage.setTitle("JavaPoly - Monopoly");
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        changerScene("menu-view.fxml");
        stage.show();
    }

    // Change la scene affichee et applique le CSS
    public static void changerScene(String fichierFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fichierFxml));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 850);
        appliquerCss(scene);
        stagePrincipal.setScene(scene);
    }

    // Charge un FXML, retourne le loader pour recuperer le controller
    public static FXMLLoader chargerFxml(String fichierFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fichierFxml));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 850);
        appliquerCss(scene);
        stagePrincipal.setScene(scene);
        return loader;
    }

    // Applique la feuille de style CSS a la scene
    private static void appliquerCss(Scene scene) {
        String css = HelloApplication.class.getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    public static Stage getStagePrincipal() {
        return stagePrincipal;
    }
}
