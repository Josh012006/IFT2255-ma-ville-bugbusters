package ca.udem.maville.client.users;
import ca.udem.maville.client.MaVille;
import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.AdaptedGson;
import ca.udem.maville.utils.DateManagement;
import ca.udem.maville.utils.RequestType;
import com.google.gson.*;

import java.util.*;

public abstract class Utilisateur  {

    protected String id ;
    protected String nom;
    protected String adresseCourriel;
    protected ArrayList<String> notifications; 

    public Utilisateur(String id, String nom, String adresseCourriel) {
        this.id = id;
        this.nom = nom;
        this.adresseCourriel = adresseCourriel;
        this.notifications = new ArrayList<>();
    }


    public void consulterNotifications() {

        String userType = "";
        if(this.getClass() == Resident.class) {
            userType = "resident";
        } else {
            userType = "prestataire";
        }
        String url = MaVille.urlHead + "/notification/getAll/" + this.id + "?userType=" + userType;
        String response = UseRequest.sendRequest(url, RequestType.GET, null);

        if (response == null) {
            System.out.println("\nUne erreur est survenue lors de la récupération de vos notifications! Veuillez réessayer plus tard.");
            return;
        }

        JsonElement elem = JsonParser.parseString(response);
        JsonObject json = elem.getAsJsonObject();

        if(json.get("status").getAsInt() != 200) {
            System.out.println("\nUne erreur est survenue lors de la récupération de vos notifications! Veuillez réessayer plus tard.");
            return;
        }

        JsonArray notifications = json.get("data").getAsJsonArray();

        if (notifications.isEmpty()) {
            System.out.println("\nVous n'avez aucune notification pour le moment.");
            return;
        }

        System.out.println("\n----- Vos notifications -----");
        for (int i = notifications.size() - 1; i >= 0; i--) {
            JsonElement notifElem = notifications.get(i);
            Gson gson = AdaptedGson.getGsonInstance();
            Notification notif = gson.fromJson(notifElem.getAsJsonObject(), Notification.class);
            System.out.println("- [" + DateManagement.formatIsoDate(notif.getDateNotification().toInstant().toString()) + "] " + notif.getMessage());
        }

    }

    // Getters
    public String getID() { return id;}
    public String getNom() { return nom; }
    public String getAdresseCourriel() { return adresseCourriel; }
}
