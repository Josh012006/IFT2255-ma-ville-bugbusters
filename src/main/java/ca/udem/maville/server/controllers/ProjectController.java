package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;

public class ProjectController {

    public Database database;

    public ProjectController(Database database) {
        this.database = database;
    }
}
