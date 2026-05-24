package org.example.monopoly.model;

// Cases speciales : Depart, Prison (simple visite), Parc gratuit, Aller en prison
public class CaseSpeciale extends Case {

    private String type; // "Depart", "Prison", "ParcGratuit", "AllerEnPrison"

    public CaseSpeciale(String nom, int position, String type) {
        super(nom, position);
        this.type = type;
    }

    @Override
    public void actionSurCase(Joueur joueur) {
        switch (type) {
            case "Depart":
                // Le bonus de 200 est deja gere dans Joueur.deplacer()
                break;
            case "Prison":
                // Simple visite, rien a faire
                break;
            case "ParcGratuit":
                // Rien a faire (regle classique)
                break;
            case "AllerEnPrison":
                joueur.allerEnPrison();
                break;
        }
    }

    public String getType() {
        return type;
    }
}
