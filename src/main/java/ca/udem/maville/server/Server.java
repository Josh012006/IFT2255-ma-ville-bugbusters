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
        database = new Database();

        // Intitialisation du serveur sur le port
        Javalin app = Javalin.create(config -> {
            config.router.contextPath = "/api";

            // Ajout des controllers
            config.router.apiBuilder(() -> {

                path("/candidature", () -> {
                    path("/getAll/{id}", () -> {
                        get(CandidatureController::getAll);
                    });
                    post(CandidatureController::create);
                    path("/{id}", () -> {
                        get(CandidatureController::get);
                        patch(CandidatureController::patch);
                        put(CandidatureController::update);
                        delete(CandidatureController::delete);
                    });
                });

                path("/signalement", () -> {
                    path("/getAll/{id}", () -> {
                        get(SignalementController::getAll);
                    });
                    post(SignalementController::create);
                    path("/{id}", () -> {
                        get(SignalementController::get);
                        put(SignalementController::update);
                        patch(SignalementController::patch);
                        delete(SignalementController::delete);
                    });
                });

                path("/probleme", () -> {
                    path("/getAll/{id}", () -> {
                        get(ProblemController::getAll);
                    });
                    path("/getCandidatures/{id}", () -> {
                        get(ProblemController::getAllCandidatures);
                    });
                    post(ProblemController::create);
                    path("/{id}", () -> {
                        get(ProblemController::get);
                        put(ProblemController::update);
                        patch(ProblemController::patch);
                        delete(ProblemController::delete);
                    });
                });

                path("/projet", () -> {
                    path("/getAll", () -> {
                        get(ProjectController::getAll);
                    });
                    path("/getByPrestataire/{id}", () -> {
                        get(ProjectController::getByPrestataire);
                    });
                    path("/getByType/{type}", () -> {
                        get(ProjectController::getByType);
                    });
                    path("/getByRegion/{region}", () -> {
                        get(ProjectController::getByRegion);
                    });
                    post(ProjectController::create);
                    path("/{id}", () -> {
                        get(ProjectController::get);
                        put(ProjectController::update);
                        patch(ProjectController::patch);
                        delete(ProjectController::delete);
                    });
                });


                path("/notification", () -> {
                    path("/getAll/{id}", () -> {
                        get(NotificationController::getAll);
                    });
                    post(NotificationController::create);
                });

                path("/resident", () -> {
                    path("/getAll", () -> {
                        get(ResidentController::getAll);
                    });
                    path("/{id}", () -> {
                        get(ResidentController::get);
                    });
                });

                path("/prestataire", () -> {
                    path("/getAll", () -> {
                        get(PrestataireController::getAll);
                    });
                    path("/{id}", () -> {
                        get(PrestataireController::get);
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
