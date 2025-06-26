package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import io.javalin.http.Context;

public class ResidentController {

    public Database database;
    public String urlHead;

    public ResidentController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void getAll(Context ctx) {

    }

    public void get(Context ctx) {

    }

    public void patch(Context ctx) {

    }

    public void update(Context ctx) {

    }
}
