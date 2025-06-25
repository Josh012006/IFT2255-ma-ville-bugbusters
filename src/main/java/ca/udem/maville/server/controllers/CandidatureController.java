package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import io.javalin.http.Context;

public class CandidatureController {
    public Database database;

    public CandidatureController(Database database) {
        this.database = database;
    }

    // A method to get all the candidatures of a prestataire
    public void getAll(Context ctx) {
        String id = ctx.pathParam("id");

    }

    public void create(Context ctx) {

    }

    public void get(Context ctx) {

    }

    public void patch(Context ctx) {

    }

    public void update(Context ctx) {

    }

    public void delete(Context ctx) {

    }
}
