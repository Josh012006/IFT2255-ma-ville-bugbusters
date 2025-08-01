package ca.udem.maville;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.controllers.ProjetController;
import ca.udem.maville.server.dao.files.ProblemDAO;
import ca.udem.maville.server.dao.files.ProjetDAO;
import ca.udem.maville.server.models.FicheProbleme;
import ca.udem.maville.server.models.Projet;
import ca.udem.maville.utils.RequestType;
import io.javalin.http.Context;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetControllerTest {

    @Mock Context ctx;
    @Mock org.slf4j.Logger logger;

    ProjetController controller;

    MockedStatic<ProjetDAO> staticProjetDAO;
    MockedStatic<UseRequest> staticUseRequest;

    @BeforeEach
    void setUp() {
        controller = new ProjetController("http://fake", logger);

        staticProjetDAO = mockStatic(ProjetDAO.class);
        staticUseRequest = mockStatic(UseRequest.class);

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.contentType(anyString())).thenReturn(ctx);
        when(ctx.result(anyString())).thenReturn(ctx);
    }

    @AfterEach
    void tearDown() {
        staticProjetDAO.close();
        staticUseRequest.close();
    }

    @Test
    void getAll_returns200() {
        List<Projet> projets = List.of(
            new Projet(new ObjectId(), "Projet 1", List.of("Rue A"), "Desc", "Type", new Date(), new Date(), new ObjectId(), new ObjectId(), "Prestataire 1", "Quartier", 1000.0, "moyenne"),
            new Projet(new ObjectId(), "Projet 2", List.of("Rue B"), "Desc", "Type", new Date(), new Date(), new ObjectId(), new ObjectId(), "Prestataire 2", "Quartier", 2000.0, "élevée")
        );

        staticProjetDAO.when(ProjetDAO::findAll).thenReturn(projets);

        controller.getAll(ctx);

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(projets);
    }


    @Test
    void getById_notFound_returns404() throws Exception {
        String idString = "64e4f3a2b5f3c8d1e2f4a6ab";
        ObjectId id = new ObjectId(idString);

        when(ctx.pathParam("id")).thenReturn(idString);
        staticProjetDAO.when(() -> ProjetDAO.findById(id)).thenReturn(null);

        controller.getById(ctx);

        verify(ctx).status(404);
        verify(ctx).result(anyString());
        verify(ctx, never()).json(any(Projet.class));
    }

    @Test
    void getById_found_returns200() throws Exception {
        String idString = "64e4f3a2b5f3c8d1e2f4a6ab";
        ObjectId id = new ObjectId(idString);

        Projet projet = new Projet(
                id,
                "Titre",
                List.of("Rue1", "Rue2"),
                "Desc",
                "Type",
                new Date(),
                new Date(),
                new ObjectId(),
                new ObjectId(),
                "Prestataire",
                "Quartier",
                1000.0,
                "haute"
        );

        when(ctx.pathParam("id")).thenReturn(idString);
        staticProjetDAO.when(() -> ProjetDAO.findById(id)).thenReturn(projet);

        controller.getById(ctx);

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(projet);
    }

}
