package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import io.javalin.http.Context;

public class NotificationController {
    public Database database;
    public String urlHead;

    public NotificationController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void getAll(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void create(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
