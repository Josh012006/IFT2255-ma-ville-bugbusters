package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.dao.files.ProjetDAO;
import ca.udem.maville.server.models.Projet;
import ca.udem.maville.utils.RequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.javalin.http.Context;

import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * La controller qui gère les différentes interactions du client avec le serveur
 * en tout ce qui concerne les projets.
 */
public class ProjetController {

    public String urlHead;
    public Logger logger;


    public ProjetController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer tous les travaux prévus.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            List<Projet> projets = ProjetDAO.findAll();

            // Envoyer le message de succès
            ctx.status(200).json(projets).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer tous les projets d'un prestataire en particulier.
     * Le paramètre de path user précise l'id du prestataire.
     * @param ctx qui représente le contexte de la requête
     */
    public void getByPrestataire(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");

            List<Projet> presProjets = ProjetDAO.findPrestataireProjet(new ObjectId(idUser));

            // Envoyer le message de succès
            ctx.status(200).json(presProjets).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de créer un nouveau projet pour un prestataire donné.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - prestataire: qui est l'id du prestataire auquel le projet appartient
     * - nomPrestataire: qui représente le nom de l'entreprise à laquelle le projet appartient
     * - ficheProbleme: qui est l'id de la fiche problème concernée
     * - quartier: qui représente le quartier affecté par les travaux
     * - titreProjet: qui est le titre du projet
     * - description: La description du projet
     * - typeTravaux: qui représente le type travail à réaliser
     * - cout: Le cout du projet
     * - priorite: qui est la priorité attribuée au projet.
     * - dateDebut: qui doit être sous format ISO
     * - dateFin: qui doit être sous format ISO
     * - ruesAffectees: Les rues affectées par les travaux
     * Elle nécessite un query parameter candidature qui représente l'id de la candidature pour pouvoir la marquer
     * comme acceptée et envoyer la notification au prestataire.
     * Elle s'occupe automatiquement d'assigner les champs id et statut.
     * Elle inclut l'envoi des notifications aux résidents ayant signalé le problème, à ceux s'étant abonnés et au
     * prestataire ayant proposé la candidature.
     * @param ctx qui représente le contexte de la requête.
     */
    public void create(Context ctx) {
        try {

            String candidature = ctx.queryParam("candidature");

            if(candidature == null || candidature.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Les query parameter candidature est requis.\"}").contentType("application/json");
                return;
            }

            // Récupérer les informations sur le nouveau projet.
            Projet newProjet = ctx.bodyAsClass(Projet.class);

            // Récupérer tous les abonnés pour les ajouter à la liste des abonnes.
            ObjectMapper mapper = JavalinJackson.defaultMapper();

            String response = UseRequest.sendRequest(urlHead + "/probleme/getReporters/" + newProjet.getFicheProbleme().toHexString(), RequestType.GET, null);
            JsonNode json = mapper.readTree(response);

            if(json.get("status").asInt() != 200) {
                throw new Exception(json.get("message").asText());
            }

            JsonNode data = json.get("data");

            if(!data.isArray()) {
                throw new Exception("Reporters n'est pas un tableau.");
            }

            List<ObjectId> abonnes = new ArrayList<>();
            for(JsonNode reporter : data) {
                abonnes.add(new ObjectId(reporter.get("id").asText()));
            }
            newProjet.setAbonnes(abonnes);

            // Envoyer une notification à tout ceux qui sont concernés
            this.notifyConcerned(newProjet);

            // Marquer la candidature comme acceptée. Cela inclut l'envoi de la notification au prestataire.
            String response3 = UseRequest.sendRequest(urlHead + "/candidature/markAsAccepted/" + candidature, RequestType.PATCH, null);
            JsonNode json3 = mapper.readTree(response3);
            JsonNode data3 = json3.get("data");
            if(json3.get("status").asInt() != 200) {
                throw new Exception(data3.get("message").asText());
            }

            // Renvoyer le projet pour marquer le succès
            ctx.status(201).json(newProjet).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer un projet en particulier grâce à son id.
     * @param ctx représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Projet projet = ProjetDAO.findById(new ObjectId(id));

            // Renvoyer le projet
            ctx.status(200).json(projet).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'un projet, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * NB: Elle remplace complètement les champs tableaux de la base de données par ceux envoyés.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            // On vérifie que le projet existe vraiment.
            Projet projet = ProjetDAO.findById(new ObjectId(id));

            if(projet == null) {
                ctx.status(404).result("{\"message\": \"Aucun projet avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            Projet modifiedProjet = ctx.bodyAsClass(Projet.class);

            ProjetDAO.save(modifiedProjet);

            // Envoyer une notification à tout ceux qui sont abonnées au quartier ou aux rues
            this.notifyConcerned(modifiedProjet);

            ctx.status(200).json(modifiedProjet).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette fonction regroupe la logique d'envoi de notifications aux personnes intéressées, concernées ou abonnées au projet.
     * @throws Exception quand l'information reçue n'a pas le bon format.
     */
    private void notifyConcerned(Projet projet) throws Exception {
        ObjectMapper mapper = JavalinJackson.defaultMapper();

        // Envoyer des notifications à tous les abonnés (qui sont ici en réalité ceux ayant signalés) et aussi à ceux intéressés
        Set<String> toSend = new HashSet<>();
        for(ObjectId abonne : projet.getAbonnes()) {
            toSend.add(abonne.toHexString());
        }

        String quartier = projet.getQuartier();
        List<String> ruesAffectees = projet.getRuesAffectees();
        String rues = String.join(",", ruesAffectees);

        String response1 = UseRequest.sendRequest(urlHead + "/resident/getConcerned?quartier=" + quartier + "&rues=" + rues, RequestType.GET, null);
        JsonNode json1 = mapper.readTree(response1);
        JsonNode data1 = json1.get("data");
        if(json1.get("status").asInt() != 200) {
            throw new Exception(data1.get("message").asText());
        }

        if(!data1.isArray()) {
            throw new Exception("Les résidents intéressés n'est pas un tableau.");
        }

        for(JsonNode element : data1) {
            toSend.add(element.asText());
        }

        for(String userId : toSend) {
            String body = "{" +
                    "\"message\": \"Un nouveau projet a été prévu dans le quartier ou les rues auxquels vous êtes abonnés.\"," +
                    "\"user\": \"" + userId + "\"," +
                    "\"url\": \"/projet/" + projet.getId() + "\"," + // Todo: Vérifier l'url une fois l'interface finie.
                    "}";
            String response2 = UseRequest.sendRequest(urlHead + "/notification", RequestType.POST, body);

            JsonNode json2 = mapper.readTree(response2);

            if(json2.get("status").asInt() != 201) {
                JsonNode info = json2.get("data");
                logger.error(info.get("message").asText());
            }
        }
    }

    /**
     * Cette fonction initialise la base de données avec les projets de travaux venant de l'API de Montréal.
     * Elle sera lancée une seule fois au premier test du serveur.
     */
    private void initializeProjetsFromAPI() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
