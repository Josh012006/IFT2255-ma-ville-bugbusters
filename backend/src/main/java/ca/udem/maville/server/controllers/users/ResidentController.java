package ca.udem.maville.server.controllers.users;

import ca.udem.maville.server.dao.files.users.ResidentDAO;
import ca.udem.maville.server.models.users.Resident;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.*;


/**
 * La controller qui gère les différentes interactions du client avec le serveur
 * en tout ce qui concerne les résidents.
 */
public class ResidentController {

    public String urlHead;
    public Logger logger;


    public ResidentController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }


    /**
     * Cette route permet de récupérer tous les résidents qui pourraient être intéressés
     * par les changements sur un projet. Elle a besoin de deux query parameters :
     * Le paramètre quartier contient le nom du quartier où le problème est situé.
     * Le paramètre rues qui contient les rues affectées par le projet en question séparées bien sûr par des virgules.
     * @param ctx qui représente le contexte de la requête
     */
    public void getConcerned(Context ctx) {
        try {
            String quartier = ctx.queryParam("quartier");
            String rues = ctx.queryParam("rues");

            if(quartier == null || rues == null || quartier.isEmpty() || rues.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Les query parameters quartier et rues sont requis.\"}").contentType("application/json");
                return;
            }

            String[] ruesTab = rues.split(",");
            List<Resident> concerned = ResidentDAO.findToNotify(quartier, ruesTab);

            List<String> concernedIds = new ArrayList<>();
            for(Resident resident : concerned) {
                concernedIds.add(resident.getId().toHexString());
            }

            ctx.status(200).json(concernedIds).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette fonction permet de récupérer tous les résidents
     * de la base de données. Il est utile au niveau du choix de profil.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            List<Resident> residents = ResidentDAO.findAll();

            ctx.status(200).json(residents).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer les informations sur un résident donné
     * en fonction de son id.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Resident resident = ResidentDAO.findById(new ObjectId(id));

            if(resident == null) {
                ctx.status(404).result("{\"message\": \"Aucun résident avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            // Renvoyer le résident
            ctx.status(200).json(resident).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier les préférences de notifications (abonnements)
     * d'un résident, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * NB: Elle remplace complètement les champs tableaux de la base de données par ceux envoyés.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Resident resident = ResidentDAO.findById(new ObjectId(id));

            if(resident == null) {
                ctx.status(404).result("{\"message\": \"Aucun résident avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            ObjectMapper mapper = JavalinJackson.defaultMapper();

            JsonNode json = mapper.readTree(ctx.body());

            JsonNode json1 = json.get("abonnementsQuartier");
            if(json1 != null && json1.isArray()) {
                List<String> abonnementsQuartier = mapper.readerForListOf(String.class).readValue(json1);
                resident.setAbonnementsQuartier(abonnementsQuartier);
            }

            JsonNode json2 = json.get("abonnementsRue");
            if(json2 != null && json2.isArray()) {
                List<String> abonnementsRue = mapper.readerForListOf(String.class).readValue(json2);
                resident.setAbonnementsRue(abonnementsRue);
            }

            ResidentDAO.save(resident);

            ctx.status(200).json(resident).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette fonction initialise la base de données avec les résidents.
     * Elle sera lancée une seule fois au premier test du serveur.
     */
    public void initializeResidents() {
        try {
            ArrayList<String> abonnementsQuartier1 = new ArrayList<>();
            abonnementsQuartier1.add("Ville-Marie");
            ArrayList<String> abonnementsRue1 = new ArrayList<>();
            abonnementsRue1.add("123 rue Ontario");
            Resident resident1 = new Resident(new ObjectId(), "John Doe", "johndoe@example.com",
                    abonnementsQuartier1, abonnementsRue1, "123 rue Ontario", "H2X 1Y4",
                    "Ville-Marie", new GregorianCalendar(2003, Calendar.SEPTEMBER, 25).getTime());
            ResidentDAO.save(resident1);

            ArrayList<String> abonnementsQuartier2 = new ArrayList<>();
            abonnementsQuartier2.add("Le Plateau-Mont-Royal");
            ArrayList<String> abonnementsRue2 = new ArrayList<>();
            abonnementsRue2.add("456 avenue du Mont-Royal");
            Resident resident2 = new Resident(new ObjectId(), "Jane Smith", "janesmith@example.com",
                    abonnementsQuartier2, abonnementsRue2, "456 avenue du Mont-Royal", "H2H 1J5",
                    "Le Plateau-Mont-Royal", new GregorianCalendar(1995, Calendar.JUNE, 15).getTime());
            ResidentDAO.save(resident2);

            ArrayList<String> abonnementsQuartier3 = new ArrayList<>();
            abonnementsQuartier3.add("Rosemont–La Petite-Patrie");
            ArrayList<String> abonnementsRue3 = new ArrayList<>();
            abonnementsRue3.add("789 rue Beaubien Est");
            Resident resident3 = new Resident(new ObjectId(), "Carlos Méndez", "carlosm@example.com",
                    abonnementsQuartier3, abonnementsRue3, "789 rue Beaubien Est", "H2S 2G1",
                    "Rosemont–La Petite-Patrie", new GregorianCalendar(1988, Calendar.MARCH, 10).getTime());
            ResidentDAO.save(resident3);

            ArrayList<String> abonnementsQuartier4 = new ArrayList<>();
            abonnementsQuartier4.add("Ville-Marie");
            ArrayList<String> abonnementsRue4 = new ArrayList<>();
            abonnementsRue4.add("456 rue Sainte-Catherine Est");
            Resident resident4 = new Resident(new ObjectId(), "Élise Tremblay", "elise.tremblay@example.com",
                    abonnementsQuartier4, abonnementsRue4, "456 rue Sainte-Catherine Est", "H2L 2C5",
                    "Ville-Marie", new GregorianCalendar(1992, Calendar.JUNE, 12).getTime());
            ResidentDAO.save(resident4);

            ArrayList<String> abonnementsQuartier5 = new ArrayList<>();
            abonnementsQuartier5.add("Plateau-Mont-Royal");
            ArrayList<String> abonnementsRue5 = new ArrayList<>();
            abonnementsRue5.add("789 avenue du Mont-Royal Est");
            Resident resident5 = new Resident(new ObjectId(), "Mathieu Gagnon", "mathieu.gagnon@example.com",
                    abonnementsQuartier5, abonnementsRue5, "789 avenue du Mont-Royal Est", "H2J 1X3",
                    "Plateau-Mont-Royal", new GregorianCalendar(2000, Calendar.NOVEMBER, 5).getTime());
            ResidentDAO.save(resident5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
