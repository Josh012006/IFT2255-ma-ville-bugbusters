package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;

public class ProblemController {

    public Database database;

    public ProblemController(Database database) {
        this.database = database;
    }
}
