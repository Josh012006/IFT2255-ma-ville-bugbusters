package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import io.javalin.http.Context;

public class ResidentController {

    public Database database;

    public ResidentController(Database database) {
        this.database = database;
    }

    public void getAll(Context ctx) {

    }

    public void get(Context ctx) {

    }
}
