package org.example.monopoly;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;

import org.example.monopoly.model.*;

import java.io.IOException;
import java.util.ArrayList;

// Controller pour l'affichage des statistiques (graphiques)
public class StatistiquesController {

    @FXML private LineChart<Number, Number> chartFortune;
    @FXML private BarChart<String, Number> chartPatrimoine;
    @FXML private PieChart chartProprietes;

    private Partie partie;

    // Initialise les graphiques avec les donnees de la partie
    public void initialiser(Partie partie) {
        this.partie = partie;
        remplirChartFortune();
        remplirChartPatrimoine();
        remplirChartProprietes();
    }

    // LineChart : evolution de la fortune de chaque joueur par tour
    private void remplirChartFortune() {
        chartFortune.getData().clear();
        chartFortune.setCreateSymbols(false);

        Historique historique = partie.getHistorique();

        for (Joueur j : partie.getJoueurs()) {
            XYChart.Series<Number, Number> serie = new XYChart.Series<>();
            serie.setName(j.getNom());

            ArrayList<Integer> fortunes = historique.getFortuneJoueur(j.getNom());
            if (fortunes != null) {
                for (int i = 0; i < fortunes.size(); i++) {
                    serie.getData().add(new XYChart.Data<>(i + 1, fortunes.get(i)));
                }
            }

            // Ajouter la fortune actuelle comme dernier point
            int dernierTour = (fortunes != null) ? fortunes.size() + 1 : 1;
            serie.getData().add(new XYChart.Data<>(dernierTour, j.getPatrimoineTotal()));

            chartFortune.getData().add(serie);
        }
    }

    // BarChart : comparaison des patrimoines totaux actuels
    private void remplirChartPatrimoine() {
        chartPatrimoine.getData().clear();

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Patrimoine");

        for (Joueur j : partie.getJoueurs()) {
            serie.getData().add(new XYChart.Data<>(j.getNom(), j.getPatrimoineTotal()));
        }

        chartPatrimoine.getData().add(serie);
    }

    // PieChart : repartition du nombre de proprietes par joueur
    private void remplirChartProprietes() {
        chartProprietes.getData().clear();

        for (Joueur j : partie.getJoueurs()) {
            int nbTotal = j.getProprietes().size() + j.getGares().size() + j.getCompagnies().size();
            if (nbTotal > 0) {
                chartProprietes.getData().add(new PieChart.Data(j.getNom() + " (" + nbTotal + ")", nbTotal));
            }
        }

        // Proprietes non achetees
        int nonAchetees = 0;
        for (int i = 0; i < 40; i++) {
            Case c = partie.getPlateau().getCase(i);
            if (c instanceof Propriete && ((Propriete) c).getProprietaire() == null) {
                nonAchetees++;
            } else if (c instanceof Gare && ((Gare) c).getProprietaire() == null) {
                nonAchetees++;
            } else if (c instanceof Compagnie && ((Compagnie) c).getProprietaire() == null) {
                nonAchetees++;
            }
        }
        if (nonAchetees > 0) {
            chartProprietes.getData().add(new PieChart.Data("Disponibles (" + nonAchetees + ")", nonAchetees));
        }
    }

    // Retour a l'ecran de jeu
    @FXML
    private void retourPartie() throws IOException {
        FXMLLoader loader = HelloApplication.chargerFxml("partie-view.fxml");
        PartieController controller = loader.getController();
        controller.initialiser(partie);
    }
}
