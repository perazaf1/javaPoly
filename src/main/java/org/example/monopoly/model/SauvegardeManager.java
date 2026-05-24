package org.example.monopoly.model;

import com.google.gson.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Gere la sauvegarde et le chargement de parties en JSON
public class SauvegardeManager {

    private static final String DOSSIER_SAUVEGARDES = "sauvegardes";

    // Cree le dossier de sauvegardes s'il n'existe pas
    private static File getDossier() {
        File dossier = new File(DOSSIER_SAUVEGARDES);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }
        return dossier;
    }

    // Sauvegarde la partie dans un fichier JSON
    public static boolean sauvegarder(Partie partie, String nomSauvegarde) {
        JsonObject json = new JsonObject();

        // Infos generales
        json.addProperty("nomSauvegarde", nomSauvegarde);
        json.addProperty("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        json.addProperty("numTour", partie.getNumTour());
        json.addProperty("joueurCourantIndex", partie.getJoueurCourantIndex());

        // Joueurs
        JsonArray joueursJson = new JsonArray();
        for (Joueur j : partie.getJoueurs()) {
            JsonObject joueurJson = new JsonObject();
            joueurJson.addProperty("nom", j.getNom());
            joueurJson.addProperty("pion", j.getPion());
            joueurJson.addProperty("argent", j.getArgent());
            joueurJson.addProperty("position", j.getPosition());
            joueurJson.addProperty("etat", j.getEtat());
            joueurJson.addProperty("toursEnPrison", j.getToursEnPrison());
            joueursJson.add(joueurJson);
        }
        json.add("joueurs", joueursJson);

        // Etat des cases (proprietaire, maisons, hotel, hypotheque)
        JsonArray casesJson = new JsonArray();
        for (int i = 0; i < 40; i++) {
            Case c = partie.getPlateau().getCase(i);
            JsonObject caseJson = new JsonObject();
            caseJson.addProperty("position", i);

            if (c instanceof Propriete) {
                Propriete p = (Propriete) c;
                caseJson.addProperty("type", "Propriete");
                caseJson.addProperty("proprietaire", p.getProprietaire() != null ? p.getProprietaire().getNom() : "");
                caseJson.addProperty("nbMaisons", p.getNbMaisons());
                caseJson.addProperty("hotel", p.isHotel());
                caseJson.addProperty("hypothequee", p.isHypothequee());
            } else if (c instanceof Gare) {
                Gare g = (Gare) c;
                caseJson.addProperty("type", "Gare");
                caseJson.addProperty("proprietaire", g.getProprietaire() != null ? g.getProprietaire().getNom() : "");
                caseJson.addProperty("hypothequee", g.isHypothequee());
            } else if (c instanceof Compagnie) {
                Compagnie comp = (Compagnie) c;
                caseJson.addProperty("type", "Compagnie");
                caseJson.addProperty("proprietaire", comp.getProprietaire() != null ? comp.getProprietaire().getNom() : "");
                caseJson.addProperty("hypothequee", comp.isHypothequee());
            }

            casesJson.add(caseJson);
        }
        json.add("cases", casesJson);

        // Banque
        JsonObject banqueJson = new JsonObject();
        banqueJson.addProperty("maisonsDisponibles", partie.getBanque().getMaisonsDisponibles());
        banqueJson.addProperty("hotelsDisponibles", partie.getBanque().getHotelsDisponibles());
        json.add("banque", banqueJson);

        // Historique
        if (partie.getHistorique() != null) {
            JsonArray historiqueJson = new JsonArray();
            for (String event : partie.getHistorique().getEvenements()) {
                historiqueJson.add(event);
            }
            json.add("historique", historiqueJson);

            // Fortune par tour
            JsonObject fortuneJson = new JsonObject();
            for (Joueur j : partie.getJoueurs()) {
                JsonArray fortuneJoueur = new JsonArray();
                ArrayList<Integer> fortunes = partie.getHistorique().getFortuneJoueur(j.getNom());
                if (fortunes != null) {
                    for (int f : fortunes) {
                        fortuneJoueur.add(f);
                    }
                }
                fortuneJson.add(j.getNom(), fortuneJoueur);
            }
            json.add("fortuneParTour", fortuneJson);
        }

        // Ecriture dans le fichier
        String nomFichier = nomSauvegarde.replaceAll("[^a-zA-Z0-9_-]", "_") + ".json";
        File fichier = new File(getDossier(), nomFichier);

        try (FileWriter writer = new FileWriter(fichier)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(json));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Charge une partie depuis un fichier JSON
    public static Partie charger(String nomFichier) {
        File fichier = new File(getDossier(), nomFichier);
        if (!fichier.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(fichier)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            // Creer la partie
            Partie partie = new Partie();

            // Charger les joueurs
            JsonArray joueursJson = json.getAsJsonArray("joueurs");
            for (JsonElement elem : joueursJson) {
                JsonObject joueurJson = elem.getAsJsonObject();
                String nom = joueurJson.get("nom").getAsString();
                String pion = joueurJson.get("pion").getAsString();
                partie.ajouterJoueur(nom, pion);
            }

            // Restaurer l'etat des joueurs
            for (int i = 0; i < joueursJson.size(); i++) {
                JsonObject joueurJson = joueursJson.get(i).getAsJsonObject();
                Joueur joueur = partie.getJoueurs().get(i);

                // Ajuster l'argent (retirer les 1500 initiaux et mettre la valeur sauvegardee)
                int argentSauvegarde = joueurJson.get("argent").getAsInt();
                joueur.payerArgent(joueur.getArgent()); // remettre a 0
                joueur.recevoirArgent(argentSauvegarde);

                joueur.setPosition(joueurJson.get("position").getAsInt());
                joueur.setEtat(joueurJson.get("etat").getAsString());
                joueur.setToursEnPrison(joueurJson.get("toursEnPrison").getAsInt());
            }

            // Restaurer l'etat des cases
            JsonArray casesJson = json.getAsJsonArray("cases");
            for (JsonElement elem : casesJson) {
                JsonObject caseJson = elem.getAsJsonObject();
                int position = caseJson.get("position").getAsInt();

                if (!caseJson.has("type")) {
                    continue;
                }

                String type = caseJson.get("type").getAsString();
                String nomProprietaire = caseJson.get("proprietaire").getAsString();

                // Trouver le joueur proprietaire
                Joueur proprietaire = null;
                if (!nomProprietaire.isEmpty()) {
                    for (Joueur j : partie.getJoueurs()) {
                        if (j.getNom().equals(nomProprietaire)) {
                            proprietaire = j;
                            break;
                        }
                    }
                }

                Case c = partie.getPlateau().getCase(position);

                if (type.equals("Propriete") && c instanceof Propriete) {
                    Propriete p = (Propriete) c;
                    if (proprietaire != null) {
                        p.setProprietaire(proprietaire);
                        proprietaire.ajouterPropriete(p);
                    }
                    p.setHypothequee(caseJson.get("hypothequee").getAsBoolean());
                    int nbMaisons = caseJson.get("nbMaisons").getAsInt();
                    for (int m = 0; m < nbMaisons; m++) {
                        p.construireMaison();
                    }
                    if (caseJson.get("hotel").getAsBoolean()) {
                        p.construireHotel();
                    }
                } else if (type.equals("Gare") && c instanceof Gare) {
                    Gare g = (Gare) c;
                    if (proprietaire != null) {
                        g.setProprietaire(proprietaire);
                        proprietaire.ajouterGare(g);
                    }
                    g.setHypothequee(caseJson.get("hypothequee").getAsBoolean());
                } else if (type.equals("Compagnie") && c instanceof Compagnie) {
                    Compagnie comp = (Compagnie) c;
                    if (proprietaire != null) {
                        comp.setProprietaire(proprietaire);
                        proprietaire.ajouterCompagnie(comp);
                    }
                    comp.setHypothequee(caseJson.get("hypothequee").getAsBoolean());
                }
            }

            // Restaurer la banque
            if (json.has("banque")) {
                JsonObject banqueJson = json.getAsJsonObject("banque");
                Banque banque = partie.getBanque();
                int maisonsActuelles = banque.getMaisonsDisponibles();
                int maisonsSauvegardees = banqueJson.get("maisonsDisponibles").getAsInt();
                // Ajuster le stock
                if (maisonsActuelles > maisonsSauvegardees) {
                    for (int i = 0; i < maisonsActuelles - maisonsSauvegardees; i++) {
                        banque.acheterMaison();
                    }
                }
            }

            // Restaurer le tour et l'index joueur courant
            partie.setNumTour(json.get("numTour").getAsInt());
            partie.setJoueurCourantIndex(json.get("joueurCourantIndex").getAsInt());

            // Restaurer l'historique
            if (json.has("historique")) {
                JsonArray historiqueJson = json.getAsJsonArray("historique");
                for (JsonElement elem : historiqueJson) {
                    partie.getHistorique().ajouterEvenement(elem.getAsString());
                }
            }
            if (json.has("fortuneParTour")) {
                JsonObject fortuneJson = json.getAsJsonObject("fortuneParTour");
                for (Joueur j : partie.getJoueurs()) {
                    if (fortuneJson.has(j.getNom())) {
                        JsonArray fortunes = fortuneJson.getAsJsonArray(j.getNom());
                        for (JsonElement f : fortunes) {
                            partie.getHistorique().ajouterFortune(j.getNom(), f.getAsInt());
                        }
                    }
                }
            }

            return partie;

        } catch (IOException e) {
            return null;
        }
    }

    // Liste les fichiers de sauvegarde disponibles
    public static String[] listerSauvegardes() {
        File dossier = getDossier();
        String[] fichiers = dossier.list((dir, name) -> name.endsWith(".json"));
        return fichiers != null ? fichiers : new String[0];
    }

    // Supprime une sauvegarde
    public static boolean supprimer(String nomFichier) {
        File fichier = new File(getDossier(), nomFichier);
        return fichier.delete();
    }
}
