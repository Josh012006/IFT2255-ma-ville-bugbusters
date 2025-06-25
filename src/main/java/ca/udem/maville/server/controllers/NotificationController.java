package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;

public class NotificationController {
    public Database database;

    public NotificationController(Database database) {
        this.database = database;
    }
}
