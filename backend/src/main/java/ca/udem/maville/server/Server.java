package ca.udem.maville.server;

import ca.udem.maville.server.controllers.*;
import ca.udem.maville.server.controllers.users.NotificationController;
import ca.udem.maville.server.controllers.users.PrestataireController;
import ca.udem.maville.server.controllers.users.ResidentController;
import ca.udem.maville.server.controllers.users.STPMController;
import ca.udem.maville.utils.AdaptedGson;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Server {

    private int port = 7070;
    private Javalin app;

    static final Logger logger = LoggerFactory.getLogger(Server.class);

    CandidatureController candidatureController;
    SignalementController signalementController;
    ProjetController projetController;
    ProblemController problemController;
    NotificationController notificationController;
    ResidentController residentController;
    PrestataireController prestataireController;
    STPMController stpmController;

    public Server() {}
    public Server(int port) {
        this.port = port;
    }

    public void start() {

        // Création des instances de controllers
        String urlHead = "http://localhost:" + port + "/api";

        candidatureController = new CandidatureController(urlHead, logger);
        signalementController = new SignalementController(urlHead, logger);
        projetController = new ProjetController(urlHead, logger);
        problemController = new ProblemController(urlHead, logger);
        notificationController = new NotificationController(urlHead, logger);
        residentController = new ResidentController(urlHead, logger);
        prestataireController = new PrestataireController(urlHead, logger);
        stpmController = new STPMController(urlHead, logger);

        // Intitialisation du serveur sur le port
        app = Javalin.create(config -> {
            config.router.contextPath = "/api";

            config.jsonMapper(new AdaptedGson.GsonMapper());

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
                        get(projetController::getAll);
                    });
                    path("/getByPrestataire/{user}", () -> {
                        get(projetController::getByPrestataire);
                    });
                    post(projetController::create);
                    path("/{id}", () -> {
                        get(projetController::get);
                        patch(projetController::patch);
                        put(projetController::update);
                        delete(projetController::delete);
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

                // Todo: Ajouter les routes pour STPM et corriger les routes pour les autres

            });
        }).start(this.port);

        app.before(ctx -> {
            Logger logger = LoggerFactory.getLogger("HTTP");

            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Requête reçue ---\n");
            sb.append("Méthode: ").append(ctx.method()).append("\n");
            sb.append("URL: ").append(ctx.fullUrl()).append("\n");
            sb.append("Headers:\n");

            ctx.headerMap().forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));

            if (!ctx.body().isEmpty()) {
                sb.append("Body:\n").append(ctx.body()).append("\n");
            }

            sb.append("---------------------\n");

            logger.info(sb.toString());
        });

        app.after(ctx -> {
            Logger logger = LoggerFactory.getLogger("HTTP");

            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Réponse envoyée ---\n");
            sb.append("Status: ").append(ctx.status()).append("\n");

            // Tu peux logger ctx.result() si tu renvoies un string, ou encoder en JSON si besoin
            if (ctx.result() != null) {
                sb.append("Body:\n").append(ctx.result()).append("\n");
            }

            sb.append("------------------------\n");

            logger.info(sb.toString());
        });


        // Appelle aussi serverStopping et serverStopped
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));

    }

    public void stop() {
        app.stop();
    }
}
