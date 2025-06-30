package ca.udem.maville.client;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.*;
import com.google.gson.*;

import java.util.*;

public class Projet {

    private String id;
    private String ruesAffectees;
    private ArrayList<String> abonnes;
    private String titreProjet;
    private String description;
    private String typeTravaux;
    private String statut;
    private Date dateDebut;
    private Date dateFin;
    private String ficheProbleme;
    private String prestataire;
    private String nomPrestataire;
    private String quartier;
    private double cout;

    // Constructeur
    public Projet(String id, String titreProjet, String ruesAffectees, String description, String typeTravaux, Date dateDebut, Date dateFin,
                  String ficheProbleme, String prestataire, String nomPrestataire, String quartier, double cout) {
        this.id = id;
        this.ruesAffectees = ruesAffectees;
        this.abonnes = new ArrayList<>();
        this.titreProjet = titreProjet;
        this.description = description;
        this.typeTravaux = typeTravaux;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.ficheProbleme = ficheProbleme;
        this.quartier = quartier;
        this.cout = cout;
        this.prestataire = prestataire;
        this.nomPrestataire = nomPrestataire;
        this.statut = "enCours";
    }


    public static void afficherProjet(JsonObject projet) {

        Gson gson = AdaptedGson.getGsonInstance();
        Projet p = gson.fromJson(projet, Projet.class);

        System.out.println("Projet: " + p.getTitreProjet());
        System.out.println("Prestataire: " + p.getNomPrestataire());
        System.out.println("Quartier: " + p.getQuartier());
        System.out.println("Rues affectées: " + p.getRuesAffectees());
        System.out.println("Période de réalisation: Du " + DateManagement.formatIsoDate(p.getDateDebut().toInstant().toString()) + " au " + DateManagement.formatIsoDate(p.getDateFin().toInstant().toString()));
        System.out.println("Type de travaux: " + p.getTypeTravaux());
        System.out.println("Statut: " + p.getStatut());
        System.out.println("Description: " + p.getDescription() + "\n");

    }


    public static void consulterProjets() {

        Scanner s = new Scanner(System.in);

        System.out.println("\nSouhaitez-vous filtrer les projets ?");
        System.out.println("1. Par type de travaux");
        System.out.println("2. Par quartier");
        System.out.println("3. Voir tous les projets");
        System.out.print("Choix: ");
        String filtreChoisi = s.nextLine();

        String response = UseRequest.sendRequest(MaVille.urlHead + "/projet/getAll", RequestType.GET, null);
        if(response == null) {
            System.out.println("\nUne erreur est survenue lors de la récupération des projets! Veuillez réessayer plus tard.");
            return;
        }
        JsonElement elem = JsonParser.parseString(response);
        JsonObject json = elem.getAsJsonObject();

        if(json.get("status").getAsInt() != 200) {
            System.out.println("\nUne erreur est survenue lors de la récupération des projets! Veuillez réessayer plus tard.");
            return;
        }

        JsonArray projets = json.get("data").getAsJsonArray();

        if(projets.isEmpty()) {
            System.out.println("\nAucun projet trouvé.");
            return;
        }


        try {
            switch (filtreChoisi) {
                case "1":

                    System.out.println("\nVeuillez choisir le type de travaux que vous cherchez:");

                    ArrayList<TypesTravaux> tab = new ArrayList<>(Arrays.asList(TypesTravaux.values()));

                    for (TypesTravaux t :tab ){
                        System.out.println( tab.indexOf(t) + ". " + t.getLabel());
                    }

                    System.out.print("Choix: ");

                    String choice = s.nextLine();
                    int choix = Integer.parseInt(choice);

                    String type = tab.get(choix).getLabel();


                    ArrayList<JsonObject> toShow = new ArrayList<>();

                    for(JsonElement p : projets) {
                        JsonObject pObj = p.getAsJsonObject();
                        if(pObj.get("typeTravaux").getAsString().equals(type)) {
                            toShow.add(pObj);
                        }
                    }

                    if(toShow.isEmpty()) {
                        System.out.println("\nAucun projet de ce type trouvé.");
                    } else {
                        System.out.println("\n----- Projets trouvés -----\n");
                        for(JsonObject p : toShow) {
                            afficherProjet(p);
                        }
                    }

                    break;


                case "2":
                    System.out.println("\nVeuillez choisir le Quartier Recherchez:");

                    ArrayList<Quartier> tab1 = new ArrayList<>(Arrays.asList(Quartier.values()));

                    for (Quartier q : tab1 ){
                        System.out.println( tab1.indexOf(q) + ". " + q.getLabel());
                    }

                    System.out.print("Choix : ");

                    String choice1 = s.nextLine();
                    int choix1 = Integer.parseInt(choice1);

                    String quartier = tab1.get(choix1).getLabel();


                    ArrayList<JsonObject> toShow1 = new ArrayList<>();

                    for(JsonElement p : projets) {
                        JsonObject pObj = p.getAsJsonObject();
                        if(pObj.get("quartier").getAsString().equals(quartier)) {
                            toShow1.add(pObj);
                        }
                    }

                    if(toShow1.isEmpty()) {
                        System.out.println("\nAucun projet trouvé dans ce quartier.");
                    } else {
                        System.out.println("\n----- Projets trouvés -----\n");
                        for(JsonObject p : toShow1) {
                            afficherProjet(p);
                        }
                    }

                    break;

                case "3":

                    System.out.println("\n----- Projets trouvés -----\n");

                    for(JsonElement p : projets) {
                        JsonObject pObj = p.getAsJsonObject();
                        afficherProjet(pObj);
                    }
                    break;

                default:
                    System.out.println("Choix invalide. Veuillez recommencer la procédure.");
            }
        } catch (Exception e) {
            System.out.println("Choix invalide. Veuillez recommencer la procédure.");
        }

    }

   

    // Getters
    public String getTitreProjet() { return titreProjet; }

    public String getNomPrestataire() { return nomPrestataire; }

    public double getCout() { return cout; }

    public String getID() { return id; }

    public String getRuesAffectees() { return ruesAffectees; }

    public String getQuartier() { return quartier; }

    public String getDescription() { return description; }

    public String getTypeTravaux() { return typeTravaux; }

    public String getStatut() { return statut; }

    public Date getDateDebut() { return dateDebut; }

    public Date getDateFin() { return dateFin; }

    public String getFicheProbleme() { return ficheProbleme; }

    // Setters
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void addAbonne(String idAbonne) {
        abonnes.add(idAbonne);
    }
}
