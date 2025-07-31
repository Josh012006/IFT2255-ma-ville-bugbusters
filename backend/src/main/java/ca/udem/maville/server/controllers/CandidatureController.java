package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.dao.files.CandidatureDAO;
import ca.udem.maville.server.models.Candidature;
import ca.udem.maville.utils.RequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.List;


/**
 * La controller qui gère les différentes interactions du client avec le serveur
 * en tout ce qui concerne les candidatures.
 */
public class CandidatureController {
    public String urlHead;
    public Logger logger;

    public CandidatureController( String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer toutes les candidatures non traitées présentes
     * dans la base de données.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            List<Candidature> candidatures = CandidatureDAO.findAll();

            ctx.status(200).json(candidatures).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer toutes les candidatures d'un prestaire.
     * Le paramètre de path user représente l'id du prestataire.
     * @param ctx qui représente le contexte de la requête
     */
    public void getByPrestataire(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");

            List<Candidature> candidatures = CandidatureDAO.findPrestataireCandidatures(new ObjectId(idUser));

            // Renvoyer les candidatures trouvées dans un tableau
            ctx.status(200).json(candidatures).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de créer une nouvelle candidature pour un prestataire.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - prestataire: qui est l'id du prestataire qui dépose la candidature. Très important.
     * - nomPrestataire: qui représente le nom de l'entreprise
     * - ficheProbleme: qui est l'id de la fiche problème concernée
     * - numeroEntreprise: qui représente le numéro d'entreprise du prestataire
     * - titreProjet: qui est le titre du projet proposé
     * - description: La description du projet proposé
     * - typeTravaux: qui représente le type travail à réaliser sous forme Label
     * - coutEstime: Le cout proposé pour le projet
     * - dateDebut: qui doit être sous format ISO
     * - dateFin: qui doit être sous format ISO
     * - ruesAffectees: Les rues affectées par les travaux sous forme de String
     * Elle s'occupe automatiquement d'assigner les champs id et statut.
     * Elle inclut aussi un envoi de notification au STPM.
     * @param ctx représente le contexte de la requête
     */
    public void create(Context ctx) {
        try {
            // Récupérer les informations sur la nouvelle candidature
            Candidature newCandidature = ctx.bodyAsClass(Candidature.class);

            // S'assurer que le statut est le bon avant de sauvegarder
            newCandidature.setStatut("en attente");
            CandidatureDAO.save(newCandidature);

            // Envoyer une notification au STPM.
            String body = "{" +
                    "\"message\": \"Une nouvelle candidature a été déposée par un prestataire.\"," +
                    "\"user\": \"507f1f77bcf86cd799439011\"," +
                    "\"url\": \"/stpm/candidature/" + newCandidature.getId() + "\"" +
                    "}";
            String response = UseRequest.sendRequest(urlHead + "/notification", RequestType.POST, body);

            ObjectMapper mapper = JavalinJackson.defaultMapper();
            JsonNode json = mapper.readTree(response);

            if(json.get("status").asInt() != 201) {
                JsonNode data = json.get("data");
                throw new Exception(data.get("message").asText());
            }

            // Renvoyer la candidature avec un message de succès
            ctx.status(201).json(newCandidature).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de récupérer une candidature en particulier à
     * partir de son id. Elle marque automatiquement la candidature comme vue.
     * Elle nécessite un query parameter stpm qui est un booléen qui précise si c'est le STPM
     * qui a vu le signalement.
     * @param ctx représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            boolean isStpm = Boolean.parseBoolean(ctx.queryParam("stpm"));

            Candidature candidature = CandidatureDAO.findById(new ObjectId(id));

            if (candidature == null) {
                ctx.status(404).result("{\"message\": \"Aucune candidature avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            if(isStpm && candidature.getStatut().equals("en attente")) {
                candidature.setStatut("vue");
                CandidatureDAO.save(candidature);
            }

            // Renvoyer la candidature
            ctx.status(200).json(candidature).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de modifier les informations d'une candidature, connaissant son id.
     * Le body doit contenir tout l'objet de candidature avec les champ modifiés.
     * Assurez vous que la nouvelle information a le bon type.
     * La modification n'est possible que si la candidature n'a pas encore été vue ou traitée.
     * NB: Elle remplace complètement les champs tableaux de la base de données par ceux envoyés.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            // On réalise une vérification supplémentaire pour s'assurer que la candidature
            // n'est pas déjà traitée.
            Candidature candidature = CandidatureDAO.findById(new ObjectId(id));

            if(candidature == null) {
                ctx.status(404).result("{\"message\": \"Aucune candidature avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            if(!candidature.getStatut().equals("en attente")) {
                ctx.status(403).result("{\"message\": \"Cette candidature a déjà été vue apr le STPM. Vous ne pouvez pas la modifier.\"}").contentType("application/json");
                return;
            }

            Candidature modifiedCandidature = ctx.bodyAsClass(Candidature.class);

            CandidatureDAO.save(modifiedCandidature);

            ctx.status(200).json(modifiedCandidature).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de supprimer une candidature à partir de son id
     * @param ctx qui représente le contexte de la requête.
     */
    public void delete(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            CandidatureDAO.delete(new ObjectId(id));

            // Supprimer la notification
            String response = UseRequest.sendRequest(urlHead + "/notification/deleteByUrl/" + id + "?signalement=false", RequestType.DELETE, null);

            JsonNode json = JavalinJackson.defaultMapper().readTree(response);

            if(json.get("status").asInt() != 200) {
                JsonNode data = json.get("data");
                throw new Exception(data.get("message").asText());
            }

            // Renvoyer la réponse de succès
            ctx.status(200).result("{\"message\": \"Suppression réalisée avec succès.\"}").contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Marque une candidature comme acceptée. Elle inclut l'envoi de la notification au prestataire.
     * @param ctx qui représente le contexte de la requête.
     */
    public void markAsAccepted(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Candidature candidature = CandidatureDAO.findById(new ObjectId(id));

            if(candidature == null) {
                ctx.status(404).result("{\"message\": \"Aucune candidature avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            candidature.setStatut("acceptée");
            CandidatureDAO.save(candidature);

            // Envoyer une notification au prestataire.
            String body = "{" +
                    "\"message\": \"Votre candidature au projet " + candidature.getTitreProjet() + " a été acceptée. Veuillez consulter vos projets récents pour visualiser le projet créé.\"," +
                    "\"user\": \"" + candidature.getPrestataire() + "\"" +
                    "}";
            String response = UseRequest.sendRequest(urlHead + "/notification", RequestType.POST, body);

            ObjectMapper mapper = JavalinJackson.defaultMapper();
            JsonNode json = mapper.readTree(response);

            if(json.get("status").asInt() != 201) {
                JsonNode data = json.get("data");
                throw new Exception(data.get("message").asText());
            }

            // Renvoyer la réponse de succès
            ctx.status(200).json(candidature).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Marque une candidature comme rejetée. Elle inclut l'envoi de la notification au prestataire.
     * Le body doit contenir un champ raison qui donne la raison du rejet.
     * @param ctx qui représente le contexte de la requête.
     */
    public void markAsRejected(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Candidature candidature = CandidatureDAO.findById(new ObjectId(id));

            if(candidature == null) {
                ctx.status(404).result("{\"message\": \"Aucune candidature avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            candidature.setStatut("refusée");
            CandidatureDAO.save(candidature);

            // Prendre la raison du rejet dans le body et envoyer la notification
            ObjectMapper mapper = JavalinJackson.defaultMapper();
            JsonNode json = mapper.readTree(ctx.body());

            if(!json.has("raison")) {
                ctx.status(400).result("{\"message\": \"Une raison est nécessaire lors du rejet d'une candidature.\"}").contentType("application/json");
                return;
            }

            String body = "{" +
                    "\"message\": \"Merci pour votre intérêt pour les problèmes de la ville de Montréal. Nous avons le regret de vous annoncer " +
                    "que votre candidature au projet " + candidature.getTitreProjet() + " a été réjetée. La raison est la suivante : " + json.get("raison").asText() + (json.get("raison").asText().endsWith(".") ? "" : ".") + "\"," +
                    "\"user\": \"" + candidature.getPrestataire() + "\"" +
                    "}";
            String response = UseRequest.sendRequest(urlHead + "/notification", RequestType.POST, body);

            JsonNode json1 = mapper.readTree(response);

            if(json1.get("status").asInt() != 201) {
                JsonNode data = json1.get("data");
                throw new Exception(data.get("message").asText());
            }

            // Renvoyer la réponse de succès
            ctx.status(200).json(candidature).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

}
