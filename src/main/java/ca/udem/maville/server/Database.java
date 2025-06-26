package ca.udem.maville.server;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.RandomGeneration;
import ca.udem.maville.utils.RequestType;
import ca.udem.maville.utils.TypesTravaux;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        // Todo : Ajouter 3 résidents, 3 prestataires, 3 problèmes et 3 candidatures à des projets de travaux.

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

                String prestataire = obj.get("organizationname").getAsString();
                project.addProperty("prestataire", prestataire);

                String ruesAffectees = obj.get("occupancy_name").getAsString();
                project.addProperty("ruesAffectees", ruesAffectees);

                // Gérer le cas du champ typeTravaux
                String reason = obj.get("reason_category").getAsString();
                TypesTravaux typeTravail = getTypeTravail(reason, TypesTravaux.values());
                project.addProperty("typeTravaux", typeTravail.name());


                // Générer le coût de manière aléatoire entre 2 000 000 $  et 9 700 000 $
                double randomDouble = RandomGeneration.getRandomUniformDouble(2, 9.7);
                int price = (int) ((Math.round(randomDouble * 100.0) / 100.0) * 1000000); // Prix final

                project.addProperty("cout", price);

                // Gérer le titre du projet et sa description
                String title = "Projet de type " + typeTravail.getLabel() + " offert par " + prestataire;
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
