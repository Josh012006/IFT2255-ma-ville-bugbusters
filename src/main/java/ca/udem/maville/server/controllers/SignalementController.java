package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;

public class SignalementController {

    public Database database;

    public SignalementController(Database database) {
        this.database = database;
    }
}
