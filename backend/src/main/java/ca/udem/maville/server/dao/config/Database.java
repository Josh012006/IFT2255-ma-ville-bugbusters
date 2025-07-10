package ca.udem.maville.server.dao.config;

import ca.udem.maville.client.Candidature;
import ca.udem.maville.client.FicheProbleme;
import ca.udem.maville.client.Projet;
import ca.udem.maville.client.Signalement;
import ca.udem.maville.client.users.Prestataire;
import ca.udem.maville.client.users.Resident;
import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Server;
import ca.udem.maville.utils.*;
import com.google.gson.*;

import java.util.*;

public class Database {

    public final Map<String, String> residents = new HashMap<>();
    public final Map<String, String> prestataires = new HashMap<>();

    public final Map<String, String> candidatures = new HashMap<>();
    public final Map<String, String> signalements = new HashMap<>();
    public final Map<String, String> problemes = new HashMap<>();

    public final Map<String, String> projets = new HashMap<>();

    public final Map<String, String> notifications = new HashMap<>();



    public Database() {
        initialize();
    }

    private void initialize() {
        // Initialiser le projet avec des candidatures, des residents, des prestataires, des projets et des signalements

        Gson gson = AdaptedGson.getGsonInstance();

        // Initialisation des résidents
        Resident resident1 = new Resident("7e57d004-2b97-0e7a-b45f-5387367791cd", "John Doe", "johndoe@example.com",
                "123 rue Ontario", "H2X 1Y4", "Ville-Marie", DateManagement.getDateIso(2003, 8, 25));

        Resident resident2 = new Resident(UniqueID.generateUniqueID(), "Alice", "alice@example.com",
                "456 avenue du Parc", "H2W 2N2", "Le Plateau-Mont-Royal", DateManagement.getDateIso(2006, 2, 8));

        Resident resident3 = new Resident(UniqueID.generateUniqueID(), "Clara", "clara@example.com",
                "789 boul. Saint-Laurent", "H2Y 3Z3", "Ville‑Marie", DateManagement.getDateIso(1998, 6, 9));


        // Initialisation des prestataires
        ArrayList<String> quartiers = new ArrayList<>();
        for(Quartier q : Quartier.values()) {
            quartiers.add(q.getLabel());
        }

        ArrayList<String> types = new ArrayList<>();
        for(TypesTravaux t : TypesTravaux.values()) {
            types.add(t.getLabel());
        }

        Prestataire prestataire1 = new Prestataire("a3d78c70b0f84b8dbff1913b5d213e38", "Excava-Pro Inc.", "contact@excavapro.ca",
                "NEQ1234567890", quartiers, types);

        Prestataire prestataire2 = new Prestataire(UniqueID.generateUniqueID(), "Toitures Montréal", "info@toituresmtl.ca",
                "NEQ0987654321", new ArrayList<>(Arrays.asList("Rosemont–La Petite-Patrie", "Le Plateau-Mont-Royal", "Pierrefonds–Roxboro")), new ArrayList<>(Arrays.asList("Travaux Gaz Ou Electricité", "Travaux Résidentiel")));

        Prestataire prestataire3 = new Prestataire(UniqueID.generateUniqueID(), "Rénovations Prestige", "prestige@reno.ca",
                "NEQ5678901234", new ArrayList<>(Arrays.asList("Outremont", "Côte-des-Neiges–Notre-Dame-de-Grâce")), new ArrayList<>(Arrays.asList("Travaux De Signalisation Et Eclairage", "Entretien Des Réseaux de Télécommunication", "Travaux Gaz Ou Electricité")));


        // Initialiser des signalements
        Signalement signal1 = new Signalement("Construction Ou Rénovation", "123 rue Ontario", "Le bâtiment présente des fissures importantes dans les murs, des infiltrations d'eau, ou une fondation affaissée. Cela compromet la stabilité et la sécurité de l'édifice.",
                "7e57d004-2b97-0e7a-b45f-5387367791cd", "Ville-Marie", "b9e45c5f-72ac-4e8c-b38e-36f22c92db3e");

        Signalement signal2 = new Signalement("Travaux Liés Aux Transports En Commun", "123 rue Ontario", "L'abribus est endommagé, un quai est fissuré, un escalier ou un ascenseur est hors service, ou la signalisation est défectueuse.",
                "7e57d004-2b97-0e7a-b45f-5387367791cd", "Ville-Marie", "3fa31d9c-2d4f-4e8a-81c6-f6a2c8497e97");
        signal2.setStatut("traité");

        Signalement signal3 = new Signalement("Travaux Routiers", "123 rue Ontario", "Des cavités ou trous apparaissent sur la chaussée, causant des risques pour les automobilistes et cyclistes, souvent dus à l'usure saisonnière.",
                "7e57d004-2b97-0e7a-b45f-5387367791cd", "Ville-Marie", "5d0c3f0f-b47a-4e1f-b8a7-2e8d9db14d0a");
        signal3.setStatut("traité");

        Signalement signal4 = new Signalement("Entretien Urbain", resident2.getAdresse(), "Des ordures, encombrants, branches ou débris de construction sont laissés sur le domaine public (rue, trottoir, terrain vague), créant des risques pour la santé ou la circulation.",
                resident2.getID(), resident2.getQuartier(), "8e3b2c66-d50d-49c3-9d3f-110e85de2351");

        Signalement signal5 = new Signalement("Travaux Routiers", "123 rue Ontario", "Des trous ou affaissements se forment sur la surface de la route, représentant un danger pour les automobilistes et les cyclistes. Ce phénomène est généralement attribuable à l'usure liée aux variations saisonnières et aux cycles de gel et de dégel.",
                resident3.getID(), resident3.getQuartier(), "7f51f182-b84f-4a7b-8192-e6b3c92d1bb9");
        signal5.setStatut("traité");

        Signalement signal6 = new Signalement("Travaux De Signalisation Et Eclairage", resident2.getAdresse(), "Plusieurs lampadaires sont hors service ou offrent un éclairage insuffisant, ce qui nuit à la visibilité et augmente les risques d'accidents, particulièrement la nuit ou par mauvais temps.",
                resident2.getID(), resident2.getQuartier(), "89e79fc6-c4ea-43f0-89e0-7dba26fa3e17");
        signal6.setStatut("traité");



        Signalement signal7 = new Signalement("Entretien Urbain", resident2.getAdresse(), "Bancs publics, poubelles, supports à vélo, panneaux ou clôtures sont brisés, déplacés ou absents.",
                resident2.getID(), resident2.getQuartier(), "3f12d12d-53a1-41a4-89ad-f7062736d650");
        signal7.setStatut("traité");

        Signalement signal13 = new Signalement("Travaux Souterrains", resident2.getAdresse(), "Une canalisation d'égout est obstruée par des racines, des débris ou des graisses, ou elle s'est effondrée en raison de son ancienneté.",
                resident2.getID(), resident2.getQuartier(), UniqueID.generateUniqueID());
        signal13.setStatut("traité");

        Signalement signal14 = new Signalement("Entretien Urbain", resident2.getAdresse(), "Des arbres, haies ou plantes débordent sur la chaussée, obstruent la visibilité ou endommagent les infrastructures (trottoirs, clôtures...).",
                resident2.getID(), resident2.getQuartier(), UniqueID.generateUniqueID());
        signal14.setStatut("traité");

        Signalement signal8 = new Signalement("Travaux Routiers", resident3.getAdresse(), "Une conduite d'eau ou d'égout s'est rompue sous la chaussée, causant une fuite, un affaissement ou une inondation.",
                resident3.getID(), resident3.getQuartier(), "01e05d6a-50c9-4477-b7bb-778ad587c37d");
        signal8.setStatut("traité");

        Signalement signal9 = new Signalement("Travaux Liés Aux Transports En Commun", resident3.getAdresse(), "Les rails, aiguillages ou câbles de métro, tramway ou train léger sont défectueux ou trop usés.",
                resident3.getID(), resident3.getQuartier(), "f4de27b7-b40c-4f63-8a0f-81f3a05394f3");
        signal9.setStatut("traité");

        Signalement signal10 = new Signalement("Construction Ou Rénovation", "123 rue Ontario", "Un feu ou un court-circuit a endommagé une partie du bâtiment, affectant à la fois la structure, les circuits électriques et les matériaux de finition.",
                "7e57d004-2b97-0e7a-b45f-5387367791cd", "Ville-Marie", UniqueID.generateUniqueID());
        signal10.setStatut("traité");

        Signalement signal11 = new Signalement("Entretien Des Réseaux de Télécommunication", "123 rue Ontario", "Des interruptions fréquentes ou une qualité de connexion dégradée sont constatées sur une zone couverte par le réseau télécom, dues à des défauts dans les équipements ou dans les câbles.",
                "7e57d004-2b97-0e7a-b45f-5387367791cd", "Ville-Marie", UniqueID.generateUniqueID());
        signal11.setStatut("traité");

        Signalement signal12 = new Signalement("Travaux Souterrains", "123 rue Ontario", "Une conduite d'eau souterraine est fissurée ou rompue, causant une perte d'eau importante, un affaissement du sol ou une baisse de pression pour les résidents.",
                "7e57d004-2b97-0e7a-b45f-5387367791cd", "Ville-Marie", UniqueID.generateUniqueID());
        signal12.setStatut("traité");

        // Initialiser des problèmes
        FicheProbleme prob1 = new FicheProbleme("ae385f19-cbd3-4b87-bc82-dae631c005a7", signal2.getTypeProbleme(), signal2.getLocalisation(), signal2.getDescription(), 1, signal2.getQuartier());
        prob1.addResident(resident1.getID());
        prob1.addSignalement(signal2.getID());
        FicheProbleme prob2 = new FicheProbleme("d1c6a279-4d6e-43f0-a9f4-f91e33e4df0c", signal3.getTypeProbleme(), signal3.getLocalisation(), signal3.getDescription(), 2, signal3.getQuartier());
        prob2.addResident(resident1.getID());
        prob2.addResident(resident3.getID());
        prob2.addSignalement(signal3.getID());
        prob2.addSignalement(signal5.getID());
        FicheProbleme prob3 = new FicheProbleme("6b3dd5df-f7b2-4a14-993e-ccf6a1860a33", signal6.getTypeProbleme(), signal6.getLocalisation(), signal6.getDescription(), 2, signal6.getQuartier());
        prob3.addResident(resident2.getID());
        prob3.addSignalement(signal6.getID());

        FicheProbleme prob4 = new FicheProbleme("17c9b6b8-0b4a-4c4a-92f0-4d2474fa3a17", signal7.getTypeProbleme(), signal7.getLocalisation(), signal7.getDescription(), 0, signal7.getQuartier());
        prob4.addResident(signal7.getResident());
        prob4.addSignalement(signal7.getID());
        FicheProbleme prob5 = new FicheProbleme("67884e2a-ec8a-4d2f-b6ad-2c2307451fa9", signal8.getTypeProbleme(), signal8.getLocalisation(), signal8.getDescription(), 1, signal8.getQuartier());
        prob5.addResident(signal8.getResident());
        prob5.addSignalement(signal8.getID());
        FicheProbleme prob6 = new FicheProbleme("ef05e11b-2b91-48d2-bd91-3a15b10c9b55", signal9.getTypeProbleme(), signal9.getLocalisation(), signal9.getDescription(), 2, signal9.getQuartier());
        prob6.addResident(signal9.getResident());
        prob6.addSignalement(signal9.getID());
        FicheProbleme prob11 = new FicheProbleme(UniqueID.generateUniqueID(), signal12.getTypeProbleme(), signal12.getLocalisation(), signal12.getDescription(), 2, signal12.getQuartier());
        prob11.addResident(signal12.getResident());
        prob11.addSignalement(signal12.getID());

        FicheProbleme prob7 = new FicheProbleme(UniqueID.generateUniqueID(), signal13.getTypeProbleme(), signal13.getLocalisation(), signal13.getDescription(), 2, signal13.getQuartier());
        prob7.addResident(signal13.getResident());
        prob7.addSignalement(signal13.getID());
        FicheProbleme prob8 = new FicheProbleme(UniqueID.generateUniqueID(), signal14.getTypeProbleme(), signal14.getLocalisation(), signal14.getDescription(), 0, signal14.getQuartier());
        prob8.addResident(signal14.getResident());
        prob8.addSignalement(signal14.getID());
        FicheProbleme prob9 = new FicheProbleme(UniqueID.generateUniqueID(), signal10.getTypeProbleme(), signal10.getLocalisation(), signal10.getDescription(), 2, signal10.getQuartier());
        prob9.addResident(signal10.getResident());
        prob9.addSignalement(signal10.getID());
        FicheProbleme prob10 = new FicheProbleme(UniqueID.generateUniqueID(), signal11.getTypeProbleme(), signal11.getLocalisation(), signal11.getDescription(), 1, signal11.getQuartier());
        prob10.addResident(signal11.getResident());
        prob10.addSignalement(signal11.getID());

        // Initialiser des candidatures
        Candidature cand1 = new Candidature(UniqueID.generateUniqueID(), prob1.getLocalisation(), prestataire1.getNumeroEntreprise(), prestataire1.getID(), prestataire1.getNom(), prob1.getID(), "Réhabilitation de l'abribus et du quai - Ontario", "Réparation complète de l'abribus, du quai fissuré et remplacement de la signalisation défectueuse.",
                prob1.getTypeTravaux(), DateManagement.getDateIso(2025, 7, 15), DateManagement.getDateIso(2025, 8, 5), 18500.75, prob1.getQuartier());
        cand1.setStatut("accepté");
        Candidature cand2 = new Candidature(UniqueID.generateUniqueID(), prob2.getLocalisation(), prestataire1.getNumeroEntreprise(), prestataire1.getID(), prestataire1.getNom(), prob2.getID(), "Réfection localisée de la chaussée - rue Ontario", "Bouchage des cavités et resurfaçage partiel de la chaussée pour sécuriser la circulation.",
                prob2.getTypeTravaux(), DateManagement.getDateIso(2025, 6, 20), DateManagement.getDateIso(2025, 6, 27), 7400, prob2.getQuartier());
        cand2.setStatut("accepté");
        Candidature cand3 = new Candidature(UniqueID.generateUniqueID(), prob3.getLocalisation(), prestataire1.getNumeroEntreprise(), prestataire1.getID(), prestataire1.getNom(), prob3.getID(), "Remise en état de l'éclairage public", "Remplacement des lampadaires défectueux et mise à niveau des luminaires pour améliorer la visibilité nocturne.",
                prob3.getTypeTravaux(), DateManagement.getDateIso(2025, 7, 1), DateManagement.getDateIso(2025, 7, 10), 12200.45, prob3.getQuartier());
        cand3.setStatut("accepté");
        Candidature cand4 = new Candidature(UniqueID.generateUniqueID(), prob4.getLocalisation(), prestataire1.getNumeroEntreprise(), prestataire1.getID(), prestataire1.getNom(), prob4.getID(), "Réparation du mobilier urbain - secteur résidentiel", "Réparation et remplacement de bancs, poubelles, panneaux et supports à vélo endommagés dans le quartier.",
                prob4.getTypeTravaux(), DateManagement.getDateIso(2025, 6, 25), DateManagement.getDateIso(2025, 7, 2), 5300, prob4.getQuartier());
        cand4.setStatut("accepté");
        Candidature cand5 = new Candidature(UniqueID.generateUniqueID(), prob5.getLocalisation(), prestataire1.getNumeroEntreprise(), prestataire1.getID(), prestataire1.getNom(), prob5.getID(), "Réparation urgente de conduite sous chaussée", "Réparation d'une conduite d'eau rompue sous la chaussée et reconstruction de la portion affaissée.",
                prob5.getTypeTravaux(), DateManagement.getDateIso(2025, 6, 28), DateManagement.getDateIso(2025, 7, 8), 28750.6, prob5.getQuartier());
        cand5.setStatut("accepté");
        Candidature cand6 = new Candidature(UniqueID.generateUniqueID(), prob6.getLocalisation(), prestataire1.getNumeroEntreprise(), prestataire1.getID(), prestataire1.getNom(), prob6.getID(), "Modernisation des rails de tramway", "Remplacement des rails et câblages électriques défectueux sur une portion du réseau de transport léger.",
                prob6.getTypeTravaux(), DateManagement.getDateIso(2025, 7, 12), DateManagement.getDateIso(2025, 8, 3), 89500.9, prob6.getQuartier());
        Candidature cand7 = new Candidature(UniqueID.generateUniqueID(), prob11.getLocalisation(), prestataire1.getNumeroEntreprise(), prestataire1.getID(), prestataire1.getNom(), prob11.getID(), "Réfection d'une conduite d'eau souterraine - Ontario", "Excavation, remplacement de la conduite fissurée et stabilisation du sol pour éviter un affaissement.",
                prob11.getTypeTravaux(), DateManagement.getDateIso(2025, 7, 5), DateManagement.getDateIso(2025, 7, 15), 19480.25, prob11.getQuartier());
        cand7.setStatut("refusé");

        // Inititaliser des projets
        Projet projet1 = new Projet(UniqueID.generateUniqueID(), cand1.getTitreProjet(), cand1.getRuesAffectees(), cand1.getDescription(), cand1.getTypeTravaux(), cand1.getDateDebut(), cand1.getDateFin(),
                cand1.getFicheProbleme(), cand1.getPrestataire(), cand1.getNomPrestataire(), prob1.getQuartier(), cand1.getCoutEstime(), prob1.getPriorite());
        projet1.addAbonne(resident1.getID());
        projet1.addAbonne(resident3.getID());
        Projet projet2 = new Projet(UniqueID.generateUniqueID(), cand2.getTitreProjet(), cand2.getRuesAffectees(), cand2.getDescription(), cand2.getTypeTravaux(), cand2.getDateDebut(), cand2.getDateFin(),
                cand2.getFicheProbleme(), cand2.getPrestataire(), cand2.getNomPrestataire(), prob2.getQuartier(), cand2.getCoutEstime(), prob2.getPriorite());
        projet2.addAbonne(resident1.getID());
        projet2.addAbonne(resident3.getID());
        Projet projet3 = new Projet(UniqueID.generateUniqueID(), cand3.getTitreProjet(), cand3.getRuesAffectees(), cand3.getDescription(), cand3.getTypeTravaux(), cand3.getDateDebut(), cand3.getDateFin(),
                cand3.getFicheProbleme(), cand3.getPrestataire(), cand3.getNomPrestataire(), prob3.getQuartier(), cand3.getCoutEstime(), prob3.getPriorite());
        projet3.addAbonne(resident2.getID());
        Projet projet4 = new Projet(UniqueID.generateUniqueID(), cand4.getTitreProjet(), cand4.getRuesAffectees(), cand4.getDescription(), cand4.getTypeTravaux(), cand4.getDateDebut(), cand4.getDateFin(),
                cand4.getFicheProbleme(), cand4.getPrestataire(), cand4.getNomPrestataire(), prob4.getQuartier(), cand4.getCoutEstime(), prob4.getPriorite());
        projet4.setStatut("suspendu");
        projet4.addAbonne(resident2.getID());
        Projet projet5 = new Projet(UniqueID.generateUniqueID(), cand5.getTitreProjet(), cand5.getRuesAffectees(), cand5.getDescription(), cand5.getTypeTravaux(), cand5.getDateDebut(), cand5.getDateFin(),
                cand5.getFicheProbleme(), cand5.getPrestataire(), cand5.getNomPrestataire(), prob5.getQuartier(), cand5.getCoutEstime(), prob5.getPriorite());
        projet5.setStatut("annulé");
        projet5.addAbonne(resident1.getID());
        projet5.addAbonne(resident3.getID());


        // Relier les objets à leurs utilisateurs
        resident1.addSignalement(signal1.getID());
        resident1.addSignalement(signal2.getID());
        resident1.addSignalement(signal3.getID());
        resident1.addSignalement(signal10.getID());
        resident1.addSignalement(signal11.getID());
        resident1.addSignalement(signal12.getID());
        resident1.addAbonnement(projet1.getID());
        resident1.addAbonnement(projet2.getID());
        resident1.addAbonnement(projet5.getID());

        resident2.addSignalement(signal4.getID());
        resident2.addSignalement(signal6.getID());
        resident2.addSignalement(signal7.getID());
        resident2.addSignalement(signal13.getID());
        resident2.addSignalement(signal14.getID());
        resident2.addAbonnement(projet3.getID());
        resident2.addAbonnement(projet4.getID());

        resident3.addSignalement(signal5.getID());
        resident3.addSignalement(signal8.getID());
        resident3.addSignalement(signal9.getID());
        resident3.addAbonnement(projet1.getID());
        resident3.addAbonnement(projet2.getID());
        resident3.addAbonnement(projet5.getID());


        prestataire1.addCandidature(cand1.getID());
        prestataire1.addCandidature(cand2.getID());
        prestataire1.addCandidature(cand3.getID());
        prestataire1.addCandidature(cand4.getID());
        prestataire1.addCandidature(cand5.getID());
        prestataire1.addCandidature(cand6.getID());
        prestataire1.addCandidature(cand7.getID());

        // Ajouter tous les objets aux bases de données
        Server.logger.info(gson.toJson(resident1));
        residents.put(resident1.getID(), gson.toJson(resident1));
        residents.put(resident2.getID(), gson.toJson(resident2));
        residents.put(resident3.getID(), gson.toJson(resident3));

        Server.logger.info(gson.toJson(prestataire1));
        prestataires.put(prestataire1.getID(), gson.toJson(prestataire1));
        prestataires.put(prestataire2.getID(), gson.toJson(prestataire2));
        prestataires.put(prestataire3.getID(), gson.toJson(prestataire3));

        Server.logger.info(gson.toJson(signal1));
        signalements.put(signal1.getID(), gson.toJson(signal1));
        signalements.put(signal2.getID(), gson.toJson(signal2));
        signalements.put(signal3.getID(), gson.toJson(signal3));
        signalements.put(signal4.getID(), gson.toJson(signal4));
        signalements.put(signal5.getID(), gson.toJson(signal5));
        signalements.put(signal6.getID(), gson.toJson(signal6));
        signalements.put(signal7.getID(), gson.toJson(signal7));
        signalements.put(signal8.getID(), gson.toJson(signal8));
        signalements.put(signal9.getID(), gson.toJson(signal9));
        signalements.put(signal10.getID(), gson.toJson(signal10));
        signalements.put(signal11.getID(), gson.toJson(signal11));
        signalements.put(signal12.getID(), gson.toJson(signal12));
        signalements.put(signal13.getID(), gson.toJson(signal13));
        signalements.put(signal14.getID(), gson.toJson(signal14));

        Server.logger.info(gson.toJson(prob1));
        problemes.put(prob1.getID(), gson.toJson(prob1));
        problemes.put(prob2.getID(), gson.toJson(prob2));
        problemes.put(prob3.getID(), gson.toJson(prob3));
        problemes.put(prob4.getID(), gson.toJson(prob4));
        problemes.put(prob5.getID(), gson.toJson(prob5));
        problemes.put(prob6.getID(), gson.toJson(prob6));
        problemes.put(prob7.getID(), gson.toJson(prob7));
        problemes.put(prob8.getID(), gson.toJson(prob8));
        problemes.put(prob9.getID(), gson.toJson(prob9));
        problemes.put(prob10.getID(), gson.toJson(prob10));
        problemes.put(prob11.getID(), gson.toJson(prob11));


        Server.logger.info(gson.toJson(cand1));
        candidatures.put(cand1.getID(), gson.toJson(cand1));
        candidatures.put(cand2.getID(), gson.toJson(cand2));
        candidatures.put(cand3.getID(), gson.toJson(cand3));
        candidatures.put(cand4.getID(), gson.toJson(cand4));
        candidatures.put(cand5.getID(), gson.toJson(cand5));
        candidatures.put(cand6.getID(), gson.toJson(cand6));
        candidatures.put(cand7.getID(), gson.toJson(cand7));

        Server.logger.info(gson.toJson(projet1));
        projets.put(projet1.getID(), gson.toJson(projet1));
        projets.put(projet2.getID(), gson.toJson(projet2));
        projets.put(projet3.getID(), gson.toJson(projet3));
        projets.put(projet4.getID(), gson.toJson(projet4));
        projets.put(projet5.getID(), gson.toJson(projet5));


        // Récupérer les travaux en cours et les ajouter aux projets
        String publicApi = "https://donnees.montreal.ca/api/3/action/datastore_search?resource_id=cc41b532-f12d-40fb-9f55-eb58c9a2b12b";
        String response = UseRequest.sendRequest(publicApi, RequestType.GET, null);

        JsonElement json = JsonParser.parseString(response);
        JsonObject jsonObject = json.getAsJsonObject();

        int statusCode = jsonObject.get("status").getAsInt();
        if (statusCode == 200) {
            JsonObject data = jsonObject.get("data").getAsJsonObject();

            // Traiter du résultat pour l'ajouter à la hashMap des projets
            JsonArray rawProjects = data.get("result").getAsJsonObject().get("records").getAsJsonArray();
            ArrayList<JsonObject> projects = normalize(rawProjects);

            for (JsonObject project : projects) {
                Server.logger.info(project.toString());
                projets.put(project.get("id").getAsString(), project.toString());
            }
        }
    }

    private ArrayList<JsonObject> normalize(JsonArray raw) {
        ArrayList<JsonObject> normalized = new ArrayList<>();

        for (JsonElement element : raw) {
            try {
                JsonObject obj = element.getAsJsonObject();

                // Formation d'un objet Projet propre à la conception de l'application
                // à partir des informations fournies par l'API
                JsonObject project = new JsonObject();

                project.addProperty("id", obj.get("id").getAsString());
                project.addProperty("quartier", obj.get("boroughid").getAsString());
                project.addProperty("statut", "enCours");
                project.add("abonnes", new JsonArray());

                String date_debut = obj.get("duration_start_date").getAsString();
                String date_fin = obj.get("duration_end_date").getAsString();
                project.addProperty("dateDebut", date_debut);
                project.addProperty("dateFin", date_fin);

                // Récupérer un id pour le prestataire et un nom d'organisation
                String nomPrestataire = obj.get("organizationname").getAsString();
                project.addProperty("nomPrestataire", nomPrestataire);
                String prestataire = UniqueID.generateUniqueID();
                project.addProperty("prestataire", prestataire);

                String ruesAffectees = obj.get("occupancy_name").getAsString();
                project.addProperty("ruesAffectees", ruesAffectees);

                // Gérer le cas du champ typeTravaux
                String reason = obj.get("reason_category").getAsString();
                TypesTravaux typeTravail = getTypeTravail(reason, TypesTravaux.values());
                project.addProperty("typeTravaux", typeTravail.getLabel());


                // Générer le coût de manière aléatoire entre 2 000 000 $  et 9 700 000 $
                double randomDouble = RandomGeneration.getRandomUniformDouble(2, 9.7);
                double price = (int) ((Math.round(randomDouble * 100.0) / 100.0) * 1000000); // Prix final

                project.addProperty("cout", price);

                // Gérer le titre du projet et sa description
                String title = "Projet de type " + typeTravail.getLabel() + " offert par " + nomPrestataire;
                String description = "Ce projet devrait affecter les rues " + ruesAffectees +
                        ". Mais pas d'inquiétude, ils ont pour but d'améliorer votre expérience de vie. " +
                        "Merci de votre confiance.";
                project.addProperty("titreProjet", title);
                project.addProperty("description", description);

                // Ajouter le projet formalisé à la liste des projets
                normalized.add(project);
            } catch (Exception e) {
                // do nothing just pass it
            }

        }

        return normalized;
    }

    private static TypesTravaux getTypeTravail(String reason, TypesTravaux[] typesTravaux) {
        if(reason.equals("Autre")) {
            // Si c'est Autre, en choisir un type de travail au hasard
            // avec une répartition uniforme
            int index = RandomGeneration.getRandomUniformInt(0, typesTravaux.length); // génère un entier entre 0 et array.length - 1
            return typesTravaux[index];
        } else {
            // Sinon, en regardant les résultats de la requête à l'API, on remarque
            // que le reste est pour la plupart des travaux de construction ou rénovation
            return TypesTravaux.constructionOuRenovation;
        }
    }
}
