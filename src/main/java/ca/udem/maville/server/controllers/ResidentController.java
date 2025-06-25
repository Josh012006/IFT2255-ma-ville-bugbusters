package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;

public class ResidentController {

    public Database database;

    public ResidentController(Database database) {
        this.database = database;
    }
}
