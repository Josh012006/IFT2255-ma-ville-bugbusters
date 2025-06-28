package ca.udem.maville.server;

import ca.udem.maville.server.controllers.*;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;


public class Server {

    private int port = 7070;
    Database database;
    CandidatureController candidatureController;
    SignalementController signalementController;
    ProjectController projectController;
    ProblemController problemController;
    NotificationController notificationController;
    ResidentController residentController;
    PrestataireController prestataireController;

    public Server() {}
    public Server(int port) {
        this.port = port;
    }

    public void start() {

        System.out.println("Server started.");

        // Création de la base de données
        database = new Database();

        // Création des instances de controllers
        String urlHead = "http://localhost:" + port + "/api";

        candidatureController = new CandidatureController(database, urlHead);
        signalementController = new SignalementController(database, urlHead);
        projectController = new ProjectController(database, urlHead);
        problemController = new ProblemController(database, urlHead);
        notificationController = new NotificationController(database, urlHead);
        residentController = new ResidentController(database, urlHead);
        prestataireController = new PrestataireController(database, urlHead);

        // Intitialisation du serveur sur le port
        Javalin app = Javalin.create(config -> {
            config.router.contextPath = "/api";

            // Ajout des controllers
            // Pour chacun, voir la JavaDoc au dessus de la fonction dans
            // la définition du controller pour voir les éléments requis.
            config.router.apiBuilder(() -> {

                path("/candidature", () -> {
                    path("/getAll/{user}", () -> {
                        get(candidatureController::getAll);
                    });
                    post(candidatureController::create);
                    path("/{id}", () -> {
                        get(candidatureController::get);
                        patch(candidatureController::patch);
                        put(candidatureController::update);
                        delete(candidatureController::delete);
                    });
                });

                path("/signalement", () -> {
                    path("/getAll/{user}", () -> {
                        get(signalementController::getAll);
                    });
                    post(signalementController::create);
                    path("/{id}", () -> {
                        get(signalementController::get);
                        put(signalementController::update);
                        patch(signalementController::patch);
                        delete(signalementController::delete);
                    });
                });

                path("/probleme", () -> {
                    path("/getInteresting/{user}", () -> {
                        get(problemController::getAll);
                    });
                    post(problemController::create);
                    path("/{id}", () -> {
                        get(problemController::get);
                        patch(problemController::patch);
                    });
                });

                path("/projet", () -> {
                    path("/getAll", () -> {
                        get(projectController::getAll);
                    });
                    path("/getByPrestataire/{user}", () -> {
                        get(projectController::getByPrestataire);
                    });
                    post(projectController::create);
                    path("/{id}", () -> {
                        get(projectController::get);
                        patch(projectController::patch);
                        put(projectController::update);
                        delete(projectController::delete);
                    });
                });


                path("/notification", () -> {
                    path("/getAll/{user}", () -> {
                        get(notificationController::getAll);
                    });
                    path("/{user}", () -> {
                        post(notificationController::create);
                    });
                });

                path("/resident", () -> {
                    path("/getByRegion/{region}", () -> {
                        get(residentController::getByRegion);
                    });
                    path("/{id}", () -> {
                        get(residentController::get);
                        patch(residentController::patch);
                        put(residentController::update);
                    });
                });

                path("/prestataire", () -> {
                    path("/getInterested/{region}/{type}", () -> {
                        get(prestataireController::getInterested);
                    });
                    path("/{id}", () -> {
                        get(prestataireController::get);
                        patch(prestataireController::patch);
                        put(prestataireController::update);
                    });
                });

            });
        }).start(this.port);

        // Appelle aussi serverStopping et serverStopped
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));

        app.events(event -> {
            event.serverStopping(() -> {
                // Nettoyage ici
                System.out.println("Nettoyage avant l'arrêt...");
            });
            event.serverStopped(() -> {
                System.out.println("Serveur arrêté.");
            });
        });

    }
}
