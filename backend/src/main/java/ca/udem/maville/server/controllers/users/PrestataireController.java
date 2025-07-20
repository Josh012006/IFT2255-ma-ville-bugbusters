package ca.udem.maville.server.controllers.users;

import ca.udem.maville.server.dao.files.users.PrestataireDAO;
import ca.udem.maville.server.models.users.Prestataire;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * La controller qui gère les différentes interactions du client avec le serveur
 * en tout ce qui concerne les prestataires.
 */
public class PrestataireController {
    public String urlHead;
    public Logger logger;

    public PrestataireController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer tous les prestataires qui pourraient être intéressés
     * par un problème déclaré. Elle a besoin de deux query parameters :
     * Le paramètre quartier contient le nom du quartier où le problème est situé.
     * Le paramètre type contient le nom du type de travail requis.
     * @param ctx qui représente le contexte de la requête
     */
    public void getConcerned(Context ctx) {
        try {
            String quartier = ctx.queryParam("quartier");
            String type = ctx.queryParam("type");

            if(quartier == null || type == null || quartier.isEmpty() || type.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Les query parameters quartier et type sont requis.\"}").contentType("application/json");
                return;
            }

            List<Prestataire> concerned = PrestataireDAO.findToNotify(quartier, type);
            List<String> concernedIds = new ArrayList<>();
            for(Prestataire prestataire : concerned) {
                concernedIds.add(prestataire.getId().toHexString());
            }

            ctx.status(200).json(concernedIds).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette fonction permet de récupérer tous les prestataires
     * de la base de données. Il est utile au niveau du choix de profil.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            List<Prestataire> prestataires = PrestataireDAO.findAll();

            ctx.status(200).json(prestataires).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer les informations sur un prestataire donné
     * en fonction de son id.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Prestataire prestataire = PrestataireDAO.findById(new ObjectId(id));

            if(prestataire == null) {
                ctx.status(404).result("{\"message\": \"Aucun prestataire avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            // Renvoyer le prestataire
            ctx.status(200).json(prestataire).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier les préférences de notifications (abonnements)
     * d'un prestataire, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * NB: Elle remplace complètement les champs tableaux de la base de données par ceux envoyés.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Prestataire prestataire = PrestataireDAO.findById(new ObjectId(id));

            if(prestataire == null) {
                ctx.status(404).result("{\"message\": \"Aucun prestataire avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            ObjectMapper mapper = JavalinJackson.defaultMapper();

            JsonNode json = mapper.readTree(ctx.body());

            JsonNode json1 = json.get("abonnementsQuartier");
            if(json1 != null && json1.isArray()) {
                List<String> abonnementsQuartier = mapper.readerForListOf(String.class).readValue(json1);
                prestataire.setAbonnementsQuartier(abonnementsQuartier);
            }

            JsonNode json2 = json.get("abonnementsType");
            if(json2 != null && json2.isArray()) {
                List<String> abonnementsType = mapper.readerForListOf(String.class).readValue(json2);
                prestataire.setAbonnementsType(abonnementsType);
            }

            PrestataireDAO.save(prestataire);

            ctx.status(200).json(prestataire).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette fonction initialise la base de données avec les prestataires.
     * Elle sera lancée une seule fois au premier test du serveur.
     */
    public void initializePrestataires() {
        try {
            ArrayList<String> abonnementsQuartier1 = new ArrayList<>(List.of("Ville-Marie", "Rosemont–La Petite-Patrie"));
            ArrayList<String> abonnementsType1 = new ArrayList<>(List.of("Travaux Routiers", "Travaux Souterrains"));
            Prestataire prestataire1 = new Prestataire(
                    new ObjectId(), "Groupe InfraTech", "infratech@groupe.com",
                    abonnementsQuartier1, "NEQ12345678",
                    new ArrayList<>(List.of("Ville-Marie", "Rosemont–La Petite-Patrie")),
                    new ArrayList<>(List.of("Travaux Routiers", "Travaux Souterrains")),
                    abonnementsType1
            );
            PrestataireDAO.save(prestataire1);

            ArrayList<String> abonnementsQuartier2 = new ArrayList<>(List.of("Ville-Marie", "Verdun"));
            ArrayList<String> abonnementsType2 = new ArrayList<>(List.of("Travaux Routiers", "Travaux Résidentiel"));
            Prestataire prestataire2 = new Prestataire(
                    new ObjectId(), "Reno-Montréal", "contact@reno-mtl.ca",
                    abonnementsQuartier2, "NEQ22334455",
                    new ArrayList<>(List.of("Ville-Marie", "Verdun")),
                    new ArrayList<>(List.of("Travaux Routiers", "Travaux Résidentiel")),
                    abonnementsType2
            );
            PrestataireDAO.save(prestataire2);

            ArrayList<String> abonnementsQuartier3 = new ArrayList<>(List.of("Le Plateau-Mont-Royal", "Verdun"));
            ArrayList<String> abonnementsType3 = new ArrayList<>(List.of("Entretien Paysager", "Travaux Résidentiel"));
            Prestataire prestataire3 = new Prestataire(
                    new ObjectId(), "ÉcoJardin Montréal", "services@ecojardin.ca",
                    abonnementsQuartier3, "NEQ99887766",
                    new ArrayList<>(List.of("Le Plateau-Mont-Royal", "Verdun")),
                    new ArrayList<>(List.of("Entretien Paysager", "Travaux Résidentiel")),
                    abonnementsType3
            );
            PrestataireDAO.save(prestataire3);

            ArrayList<String> abonnementsQuartier4 = new ArrayList<>(List.of("Le Plateau-Mont-Royal", "Ahuntsic–Cartierville"));
            ArrayList<String> abonnementsType4 = new ArrayList<>(List.of("Travaux De Signalisation Et Eclairage", "Travaux Souterrains"));
            Prestataire prestataire4 = new Prestataire(
                    new ObjectId(), "SignalPro Inc.", "admin@signalpro.ca",
                    abonnementsQuartier4, "NEQ77665544",
                    new ArrayList<>(List.of("Le Plateau-Mont-Royal", "Ahuntsic–Cartierville")),
                    new ArrayList<>(List.of("Travaux De Signalisation Et Eclairage", "Travaux Souterrains")),
                    abonnementsType4
            );
            PrestataireDAO.save(prestataire4);

            ArrayList<String> abonnementsQuartier5 = new ArrayList<>(List.of("LaSalle", "Ville-Marie"));
            ArrayList<String> abonnementsType5 = new ArrayList<>(List.of("Entretien Urbain", "Travaux Résidentiel"));
            Prestataire prestataire5 = new Prestataire(
                    new ObjectId(), "Services Urbains Inc.", "contact@servicesurbains.com",
                    abonnementsQuartier5, "NEQ3344556677",
                    new ArrayList<>(List.of("LaSalle", "Ville-Marie")),
                    new ArrayList<>(List.of("Entretien Urbain", "Travaux Résidentiel")),
                    abonnementsType5
            );
            PrestataireDAO.save(prestataire5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
