package ca.udem.maville.client.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import ca.udem.maville.client.*;
import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.AdaptedGson;
import ca.udem.maville.utils.DateManagement;
import ca.udem.maville.utils.RequestType;
import ca.udem.maville.utils.TypesTravaux;
import com.google.gson.*;

public class Prestataire extends Utilisateur {

    private String numeroEntreprise;
    private ArrayList<String> quartiers;
    private ArrayList<String> typesTravaux;
    private ArrayList<String> candidatures;

    public Prestataire(String id, String nom, String adresseCourriel, String numeroEntreprise, ArrayList<String> quartiers, ArrayList<String> typesTravaux) {
        super(id, nom, adresseCourriel);
        this.numeroEntreprise = numeroEntreprise;
        this.quartiers = quartiers;
        this.typesTravaux = typesTravaux;
        this.candidatures = new ArrayList<>();
    }

    public String getNumeroEntreprise() {
        return numeroEntreprise;
    }

    public ArrayList<String> getQuartiers() {
        return quartiers;
    }

    public ArrayList<String> getTypesTravaux() {
        return typesTravaux;
    }

    public void addCandidature(String idCandidature) {
        candidatures.add(idCandidature);
    }

    

    public void voirFichesDisponibles() {
        String responseFiche = UseRequest.sendRequest(MaVille.urlHead + "/probleme/getInteresting/" + this.id, RequestType.GET, null);

        if(responseFiche == null) {
            System.out.println(" \nUne erreur est survenue lors de la récuperation des fiches problèmes! Veuillez réessayer plus tard.");
            return;
        }

        JsonElement elemFiche = JsonParser.parseString(responseFiche);
        JsonObject jsonFiche = elemFiche.getAsJsonObject();

        int statuscode = jsonFiche.get("status").getAsInt();
        if (statuscode != 200) {
            System.out.println(" \nUne erreur est survenue lors de la récuperation des fiches problèmes! Veuillez réessayer plus tard.");
            return;
        }

        JsonArray jsonFiches = jsonFiche.get("data").getAsJsonArray();
        Gson gson = AdaptedGson.getGsonInstance();

        if(jsonFiches.isEmpty()) {
            System.out.println("\nAucune fihce problème qui pourrais vous intéresser trouvée.");
            return;
        }

        System.out.println("\nVous verrez ci-dessous la liste des fiches disponibles et qui couvre vos domaines et vos quartiers d'expertise. Si vous voyez une fiche problème qui vous intéresse, " +
                "veuillez noter son id. \nCette information vous sera nécessaire pour déposer une candidature: ");

        System.out.println("\n----- Liste des fiches disponibles -----\n");

        for (int i = jsonFiches.size() - 1; i >= 0; i--){

            JsonElement f = jsonFiches.get(i);
            FicheProbleme signal = gson.fromJson(f.getAsJsonObject(), FicheProbleme.class);

            System.out.println("ID: " + signal.getID());
            System.out.println("Type de Problème: " + signal.getTypeTravaux());
            System.out.println("Quartier: " + signal.getQuartier());
            System.out.println("Description: " + signal.getDescription());
            String priorite = (signal.getPriorite() == 0) ? "faible" : (signal.getPriorite() == 1) ? "moyenne" : "élevée";
            System.out.println("Priorité: " + priorite);
            System.out.println("Date de Création de la fiche problème: " + DateManagement.formatIsoDate(signal.getDateCreationFiche().toInstant().toString()) + "\n");
        }

    }


    public void soumettreCandidature() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Veuillez entrer l'ID de la fiche problème par laquelle vous êtes intéressés : ");
            String idProblem = sc.nextLine();

            String responseProblem = UseRequest.sendRequest(MaVille.urlHead + "/probleme/" + idProblem , RequestType.GET, null);

            if(responseProblem == null) {
                System.out.println("\nUne erreur est survenue lors de la recherche de la fiche problème associée à la candidature! Veuillez réessayre plus tard");
                return;
            }

            JsonElement elemProblem = JsonParser.parseString(responseProblem);
            JsonObject jsonProblem = elemProblem.getAsJsonObject();

            int statuscodeProblem = jsonProblem.get("status").getAsInt();

            if(statuscodeProblem == 404) {
                System.out.println("\nFiche problème inconnue. Veuillez entrer un ID valide.");
                throw new Exception("Entrez un ID de problème valide.");
            }
            else if(statuscodeProblem != 200) {
                System.out.println("\nUne erreur est survenue lors de la recherche de la fiche problème associée à la candidature! Veuillez réessayre plus tard");
                return;
            }

            System.out.println("\nProposez un titre au projet: ");
            String titreProjet = sc.nextLine();

            System.out.println("\nDécrivez le projet que vous comptez réaliser. Veuillez l'écrire au complet, c'est-à-dire ne pas utiliser de retour à la ligne: ");
            String descriptionProblem = sc.nextLine();

            System.out.println("\nSelon vous à quel type de travail correspond ce projet: ");

            ArrayList<TypesTravaux> tab = new ArrayList<>(Arrays.asList(TypesTravaux.values()));

            for (TypesTravaux t :tab ){
                System.out.println( tab.indexOf(t) + ". " + t.getLabel());
            }
            System.out.print("Choix : ");

            String choice = sc.nextLine();
            int choix = Integer.parseInt(choice);

            String typeTravaux = tab.get(choix).getLabel();

            System.out.println("\nQuel sera le cout de ce projet. Ne séparez pas les chiffres par des espaces: ");
            Double coutEstime = Double.parseDouble(sc.nextLine());

            System.out.println("\nVeuillez entrer la date de début des travaux. Utilisez le format dd/MM/yyyy: ");
            String dateDebut = DateManagement.formatDateFR(sc.nextLine());

            System.out.println("\nVeuillez entrer la date de fin des travaux. Utilisez le format dd/MM/yyyy: ");
            String dateFin = DateManagement.formatDateFR(sc.nextLine());

            System.out.println("\nVeuillez citer les rues qui seront affectées par ces travaux. " +
                    "Vous pouvez les séparer par des virgules mais ne retourner pas à la ligne: ");
            String ruesAffectees = sc.nextLine();

            // Envoyer une requête pour créer une nouvelle candidature

            JsonObject newCand = new JsonObject();

            newCand.addProperty("prestataire", this.id);
            newCand.addProperty("nomPrestataire", this.nom);
            newCand.addProperty("ficheProbleme", idProblem);
            newCand.addProperty("numeroEntreprise", this.numeroEntreprise);
            newCand.addProperty("titreProjet", titreProjet);
            newCand.addProperty("description", descriptionProblem);
            newCand.addProperty("typeTravaux", typeTravaux);
            newCand.addProperty("coutEstime", coutEstime);
            newCand.addProperty("dateDebut", dateDebut);
            newCand.addProperty("dateFin", dateFin);
            newCand.addProperty("ruesAffectees", ruesAffectees);

            String responseCand = UseRequest.sendRequest(MaVille.urlHead + "/candidature" , RequestType.POST, newCand.toString());

            if(responseCand == null) {
                System.out.println("\nUne erreur est survenue lors de la création de la candidature! Veuillez réessayer plus tard.");
                return;
            }

            JsonElement elemCand = JsonParser.parseString(responseCand);
            JsonObject jsonCand = elemCand.getAsJsonObject();

            int statuscode = jsonCand.get("status").getAsInt();
            if(statuscode != 200) {
                System.out.println("\nUne erreur est survenue lors de la création de la candidature! Veuillez réessayer plus tard.");
                return;
            }

            System.out.println("\nVotre candidature a bien été reçue. Une notification vous sera envoyé lorsqu'elle sera traitée par un agent. Merci!");

        } catch (Exception e) {
            System.out.println("Choix invalide. Veuillez recommencer la procédure.");
        }
    }


    public JsonArray afficherProjets() {
        String responseProjet = UseRequest.sendRequest(MaVille.urlHead + "/projet/getByPrestataire/" + this.id, RequestType.GET, null);

        if(responseProjet == null) {
            System.out.println("\nUne erreur est survenue lors de la récupération de vos projets. Veuillez réessayer plus tard.");
            return null;
        }

        JsonElement elemProjet = JsonParser.parseString(responseProjet);
        JsonObject jsonProjet = elemProjet.getAsJsonObject();

        int statuscodeProjet = jsonProjet.get("status").getAsInt();
        if (statuscodeProjet != 200) {
            System.out.println("Une erreur est survenue lors de la récupération de vos projets. Veuillez réessayer plus tard.");
        }

        JsonArray jsonProjets = jsonProjet.get("data").getAsJsonArray();
        if(jsonProjets.isEmpty()) {
            System.out.println("\nVous n'avez aucun projet enregistré.");
            return null;
        }


        Gson gson = AdaptedGson.getGsonInstance();
        System.out.println("\n----- Vos projets -----\n");

        for (JsonElement projet : jsonProjets) {
            Projet p = gson.fromJson(projet.getAsJsonObject(), Projet.class);

            System.out.println("ID: " + p.getID());
            System.out.println("Projet: " + p.getTitreProjet());
            System.out.println("Quartier: " + p.getQuartier());
            System.out.println("Rues affectées: " + p.getRuesAffectees());
            System.out.println("Période de réalisation: Du " + DateManagement.formatIsoDate(p.getDateDebut().toInstant().toString()) + " au " + DateManagement.formatIsoDate(p.getDateFin().toInstant().toString()));
            System.out.println("Type de travaux: " + p.getTypeTravaux());
            System.out.println("Statut: " + p.getStatut());
            System.out.println("Description: " + p.getDescription() + "\n");
        }

        return jsonProjets;
    }

    public void modifierProjet() {
        try {

            Scanner sc = new Scanner(System.in);

            JsonArray myProjects = afficherProjets();

            if(myProjects == null) {
                return;
            }

            System.out.println("\nSouhaitez vous modifier un projet?");
            System.out.println("1. Oui");
            System.out.println("2. Non");

            String choix = sc.nextLine();

            if(!choix.equals("1")) {
                if(!choix.equals("2")) {
                    throw new Exception("Répondez par oui ou par non.");
                }
                return;
            }

            System.out.println("\n----- Modification de projet -----\n");

            System.out.println("\nVeuillez entrer l'ID du projet que vous souhaitez modifier: ");
            String id = sc.nextLine();

            JsonObject toModify = null;

            for(JsonElement p : myProjects) {
                JsonObject projet = p.getAsJsonObject();
                if(projet.get("id").getAsString().equals(id)) {
                    toModify = projet;
                    break;
                }
            }

            if(toModify == null) {
                System.out.println("\nAucun projet avec un tel ID retrouvé.");
                throw new Exception("Aucun projet avec un tel ID retrouvé.");
            }

            System.out.println("\nQuelle information souhaitez-vous modifier: ");
            System.out.println("1. Description du projet");
            System.out.println("2. Statut");
            System.out.println("3. Date de fin prévue");
            System.out.print("Choix: ");

            String toChange = sc.nextLine();

            JsonObject toSend = new JsonObject();

            switch(toChange) {
                case "1":
                    System.out.println("\nVeuillez entrer la nouvelle description du projet. Veuillez l'écrire au complet, c'est-à-dire ne pas utiliser de retour à la ligne:");
                    String newDescription = sc.nextLine();
                    toSend.addProperty("description", newDescription);
                    break;
                case "2":
                    System.out.println("\nQuelle est le nouveau statut du projet:");
                    System.out.println("1. En cours");
                    System.out.println("2. Suspendu");
                    System.out.println("3. Annulé");
                    System.out.print("Choix: ");

                    String newStatus = sc.nextLine();
                    switch(newStatus) {
                        case "1":
                            toSend.addProperty("statut", "enCours");
                            break;
                        case "2":
                            toSend.addProperty("statut", "suspendu");
                            break;
                        case "3":
                            toSend.addProperty("statut", "annule");
                            break;
                        default:
                            throw new Exception("Erreur du statut du projet.");
                    }
                    break;
                case "3":
                    System.out.println("\nVeuillez entrer la nouvelle date de fin des travaux. Utilisez le format dd/MM/yyyy: ");
                    String dateFin = DateManagement.formatDateFR(sc.nextLine());
                    toSend.addProperty("dateFin", dateFin);
                    break;
                default:
                    throw new Exception("Choix de modification invalide.");
            }

            String patchResponse = UseRequest.sendRequest(MaVille.urlHead + "/projet/" + toModify.get("id").getAsString(), RequestType.PATCH, toSend.toString());

            if(patchResponse == null) {
                System.out.println("Une erreur est intervenue lors de la modification du projet. Veuillez réessayer plus tard.");
                return;
            }

            JsonElement patch = new JsonParser().parse(patchResponse);
            JsonObject jsonPatch = patch.getAsJsonObject();

            if(jsonPatch.get("status").getAsInt() != 200) {
                System.out.println("Une erreur est intervenue lors de la modification du projet. Veuillez réessayer plus tard.");
                return;
            }

            System.out.println("La modification a été effectuée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Choix invalide. Veuillez recommencer la procédure.");
        }


    }
}
