package ca.udem.maville.server.controllers;

import ca.udem.maville.server.dao.files.CandidatureDAO;
import ca.udem.maville.server.models.Candidature;
import ca.udem.maville.utils.*;
import io.javalin.http.Context;

import org.bson.types.ObjectId;
import org.slf4j.Logger;


import java.util.List;

public class CandidatureController {
    public String urlHead;
    public Logger logger;

    public CandidatureController( String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer toutes les candidatures présentes
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

            List<candidatures> candidatures = CandidatureDAO.findPrestataireCandidatures(new ObjectId(idUser));

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
     * @param ctx représente le contexte de la requête
     */
    public void create(Context ctx) {
        try {
            // Récupérer les informations sur la nouvelle candidature
            Candidature newCandidature = ctx.bodyAsClass(Candidature.class);

            // S'assurer que le statut est le bon avant de sauvegarder
            newCandidature.setStatut("en attente");
            CandidatureDAO.save(newCandidature);

            // Todo: Envoyer une notification au STPM.

            // Renvoyer la candidature avec un message de succès
            ctx.status(201).json(newCandidature).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de récupérer une candidature en particulier à
     * partir de son id.
     * @param ctx représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Candidature candidature = CandidatureDA0.findById(new ObjectId(id));

            if (candidature == null) {
                ctx.status(404).result("{\"message\": \"Aucune candidature avec un tel ID trouvée.\"}").contentType("application/json");
                return;
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
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * La modification n'est possible que si la candidature n'a pas encore été vue ou traitée.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            // On réalise une vérification supplémentaire pour s'assurer que la candidature
            // n'est pas déjà traitée.
            Candidature candidature = CandidatureDAO.findById(id);

            if(!candidature.getStatut().equals("en attente")) {
                ctx.status(403).result("{\"message\": \"Cette candidature a déjà été vue. Vous ne pouvez pas la modifier.\"}").contentType("application/json");
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

            // Renvoyer la réponse de succès
            ctx.status(200).result("{\"message\": \"Suppression réalisée avec succès.\"}").contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
