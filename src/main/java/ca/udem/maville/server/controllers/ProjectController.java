package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import io.javalin.http.Context;

public class ProjectController {

    public Database database;
    public String urlHead;

    public ProjectController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void getAll(Context ctx) {

    }

    public void getByPrestataire(Context ctx) {

    }

    public void getByType(Context ctx) {

    }

    public void getByRegion(Context ctx) {

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
