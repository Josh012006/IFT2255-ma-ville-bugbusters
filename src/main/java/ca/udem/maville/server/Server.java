package ca.udem.maville.server;

import ca.udem.maville.server.controllers.*;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;


public class Server {

    private int port = 7070;
    Database database;

    public Server() {}
    public Server(int port) {
        this.port = port;
    }

    public void start() {
        // Création de la base de données
        database = new Database();

        // Création des instances de controllers
        String urlHead = "http://localhost:" + port + "/api";

        CandidatureController candidatureController = new CandidatureController(database, urlHead);
        SignalementController signalementController = new SignalementController(database, urlHead);
        ProjectController projectController = new ProjectController(database, urlHead);
        ProblemController problemController = new ProblemController(database, urlHead);
        NotificationController notificationController = new NotificationController(database, urlHead);
        ResidentController residentController = new ResidentController(database, urlHead);
        PrestataireController prestataireController = new PrestataireController(database, urlHead);

        // Intitialisation du serveur sur le port
        Javalin app = Javalin.create(config -> {
            config.router.contextPath = "/api";

            // Ajout des controllers
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
                    // getAll pour le résident
                    path("/getAll", () -> {
                        get(problemController::getAll);
                    });
                    path("/getCandidatures/{id}", () -> {
                        get(problemController::getAllCandidatures);
                    });
                    post(problemController::create);
                    path("/{id}", () -> {
                        get(problemController::get);
                        put(problemController::update);
                        patch(problemController::patch);
                        delete(problemController::delete);
                    });
                });

                path("/projet", () -> {
                    path("/getAll", () -> {
                        get(projectController::getAll);
                    });
                    path("/getByPrestataire/{user}", () -> {
                        get(projectController::getByPrestataire);
                    });
                    path("/getByType/{type}", () -> {
                        get(projectController::getByType);
                    });
                    path("/getByRegion/{region}", () -> {
                        get(projectController::getByRegion);
                    });
                    post(projectController::create);
                    path("/{id}", () -> {
                        get(projectController::get);
                        put(projectController::update);
                        patch(projectController::patch);
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
                    path("/getAll", () -> {
                        get(residentController::getAll);
                    });
                    path("/{id}", () -> {
                        get(residentController::get);
                        put(residentController::update);
                        patch(residentController::patch);
                    });
                });

                path("/prestataire", () -> {
                    path("/getAll", () -> {
                        get(prestataireController::getAll);
                    });
                    path("/{id}", () -> {
                        get(prestataireController::get);
                        put(prestataireController::update);
                        patch(prestataireController::patch);
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
