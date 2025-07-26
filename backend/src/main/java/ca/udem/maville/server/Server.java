package ca.udem.maville.server;

import ca.udem.maville.server.controllers.*;
import ca.udem.maville.server.controllers.users.NotificationController;
import ca.udem.maville.server.controllers.users.PrestataireController;
import ca.udem.maville.server.controllers.users.ResidentController;
import ca.udem.maville.utils.CustomizedMapper;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
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

        // Les fonctions d'initialisation de la base de données. A lancer une seule fois.
        // Elle sont placées là juste pour marquer la trace qu'elles ont vraiment été implémentées.
        // Puisque les données sont persistantes, elle ont déjà été lancées comme dit une seule fois
        // lorsque nous avons fait la configuration.
        // Cela peut aussi être facilement remarqué au niveau de l'interface graphique par la présence des entités.
//        projetController.initializeProjetsFromAPI();
//        residentController.initializeResidents();
//        prestataireController.initializePrestataires();

        // Intitialisation du serveur sur le port
        app = Javalin.create(config -> {
            config.router.contextPath = "/api";

            // Configurer le cors pour accepter les requêtes du client
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.allowHost("http://localhost:5173");
                    // it.allowHost("http://localhost:3000"); // si tu utilises React
                    // it.allowHost("*"); // 👈 en dev uniquement, permet tout
                });
            });

            // Configurer le mapper pour ne pas avoir de problème pour passer automatiquement
            // de ObjectId à String et vice-versa.
            config.jsonMapper(
                new JavalinJackson()
                        .updateMapper(mapper -> {
                            mapper.registerModule(new CustomizedMapper.MongoModule());
                        })
            );

            // Ajout des controllers
            // Pour chacun, voir la JavaDoc au dessus de la fonction dans
            // la définition du controller pour voir les éléments requis.
            config.router.apiBuilder(() -> {

                path("/notification", () -> {
                    path("/getAll/{user}", () -> {
                        get(notificationController::getAll);
                    });
                    path("/hasNotifications/{user}", () -> {
                        get(notificationController::hasNotifications);
                    });
                    path("/{id}", () -> {
                        get(notificationController::getById);
                    });
                    post(notificationController::create);
                });

                path("/prestataire", () -> {
                    // Le path getConcerned nécessite deux query parameters
                    // quartier et type.
                    path("/getConcerned", () -> {
                        get(prestataireController::getConcerned);
                    });
                    path("/getAll", () -> {
                        get(prestataireController::getAll);
                    });
                    path("/{id}", () -> {
                        get(prestataireController::getById);
                        patch(prestataireController::patch);
                    });
                });

                path("/resident", () -> {
                    // Le path getConcerned nécessite deux query parameters
                    // quartier et rues. Notons que rues peut contenir plusieurs
                    // rues séparées par des virgules.
                    path("/getConcerned", () -> {
                        get(residentController::getConcerned);
                    });
                    path("/getAll", () -> {
                        get(residentController::getAll);
                    });
                    path("/{id}", () -> {
                        get(residentController::getById);
                        patch(residentController::patch);
                    });
                });

                path("/candidature", () -> {
                    path("/getAll", () -> {
                        get(candidatureController::getAll);
                    });
                    path("/getByPrestataire/{user}", () -> {
                        get(candidatureController::getByPrestataire);
                    });
                    post(candidatureController::create);
                    path("/{id}", () -> {
                        get(candidatureController::getById);
                        patch(candidatureController::patch);
                        delete(candidatureController::delete);
                    });
                    path("/markAsAccepted/{id}", () -> {
                        patch(candidatureController::markAsAccepted);
                    });
                    path("/markAsRejected/{id}", () -> {
                        patch(candidatureController::markAsRejected);
                    });
                });

                path("/probleme", () -> {
                    path("/getAll", () -> {
                        get(problemController::getAll);
                    });
                    post(problemController::create);
                    path("/{id}", () -> {
                        get(problemController::getById);
                    });
                    path("/addExisting/{id}", () -> {
                        patch(problemController::addExisting);
                    });
                    path("/getReporters/{id}", () -> {
                        get(problemController::getReporters);
                    });
                    path("/markAsProcessed/{id}", () -> {
                        patch(problemController::markAsProcessed);
                    });
                });

                path("/projet", () -> {
                    path("/getAll", () -> {
                        get(projetController::getAll);
                    });
                    path("/getByPrestataire/{user}", () -> {
                        get(projetController::getByPrestataire);
                    });
                    // Il nécessite un query parameter candidature
                    // qui représente l'id de la candidature qui a conduit au projet.
                    post(projetController::create);
                    path("/{id}", () -> {
                        get(projetController::getById);
                        patch(projetController::patch);
                    });
                });

                path("/signalement", () -> {
                    path("/getAll", () -> {
                        get(signalementController::getAll);
                    });
                    post(signalementController::create);
                    path("/getByResident/{user}", () -> {
                        get(signalementController::getByResident);
                    });
                    path("/{id}", () -> {
                        get(signalementController::getById);
                        patch(signalementController::patch);
                        delete(signalementController::delete);
                    });
                    // Il requiert un query parameter treated qui informe s'il
                    // existe déjà un projet pour ce signalement
                    path("/markAsProcessed/{id}", () -> {
                        patch(signalementController::markAsProcessed);
                    });
                });

            });
        }).start(this.port);

        logger.info("\n========== 🚀 Server started on port {} ==========\n", this.port);

        // La logique de formattage des logs du servers.
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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("\n========== 🛑 Server stopped ==========\n");
            app.stop();
        }));


    }

    public void stop() {
        app.stop();
    }
}
