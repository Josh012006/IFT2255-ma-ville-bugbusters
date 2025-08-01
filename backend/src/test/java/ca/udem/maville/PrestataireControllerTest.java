package ca.udem.maville;

import ca.udem.maville.server.controllers.users.PrestataireController;
import ca.udem.maville.server.dao.files.users.PrestataireDAO;
import ca.udem.maville.server.models.users.Prestataire;
import io.javalin.http.Context;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestataireControllerTest {

    @Mock Context ctx;
    @Mock org.slf4j.Logger logger;

    PrestataireController controller;

    MockedStatic<PrestataireDAO> staticPrestataireDAO;

    @BeforeEach
    void setUp() {
        controller = new PrestataireController("http://fake", logger);
        staticPrestataireDAO = mockStatic(PrestataireDAO.class);

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.contentType(anyString())).thenReturn(ctx);
        when(ctx.result(anyString())).thenReturn(ctx);
    }

    @AfterEach
    void tearDown() {
        staticPrestataireDAO.close();
    }

    @Test
    void getAll_returns200() {
        List<Prestataire> list = List.of(
            new Prestataire(new ObjectId(), "Groupe A", "a@ex.com", new ArrayList<>(), "NEQ1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
            new Prestataire(new ObjectId(), "Groupe B", "b@ex.com", new ArrayList<>(), "NEQ2", new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );

        staticPrestataireDAO.when(PrestataireDAO::findAll).thenReturn(list);

        controller.getAll(ctx);

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(list);
    }

    @Test
    void getById_found_returns200() {
        ObjectId id = new ObjectId();
        Prestataire p = new Prestataire(id, "Groupe A", "a@ex.com", new ArrayList<>(), "NEQ1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(ctx.pathParam("id")).thenReturn(id.toHexString());
        staticPrestataireDAO.when(() -> PrestataireDAO.findById(id)).thenReturn(p);

        controller.getById(ctx);

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(p);
    }

    @Test
    void getById_notFound_returns404() {
        ObjectId id = new ObjectId();
        when(ctx.pathParam("id")).thenReturn(id.toHexString());
        staticPrestataireDAO.when(() -> PrestataireDAO.findById(id)).thenReturn(null);

        controller.getById(ctx);

        verify(ctx).status(404);
        verify(ctx).result(anyString());
        verify(ctx, never()).json(any());
    }


    @Test
    void patch_updatesSubscriptions_returns200() {
        ObjectId id = new ObjectId();
        Prestataire p = new Prestataire(id, "Groupe A", "a@ex.com", new ArrayList<>(), "NEQ1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(ctx.pathParam("id")).thenReturn(id.toHexString());
        when(ctx.body()).thenReturn("{\n  \"abonnementsQuartier\": [\"Verdun\", \"Ville-Marie\"],\n  \"abonnementsType\": [\"Travaux Routiers\", \"Entretien Urbain\"]\n}");

        staticPrestataireDAO.when(() -> PrestataireDAO.findById(id)).thenReturn(p);

        controller.patch(ctx);

        staticPrestataireDAO.verify(() -> PrestataireDAO.save(p), times(1));

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(p);

        assertEquals(List.of("Verdun", "Ville-Marie"), p.getAbonnementsQuartier());
        assertEquals(List.of("Travaux Routiers", "Entretien Urbain"), p.getAbonnementsType());
    }

    @Test
    void patch_notFound_returns404() {
        ObjectId id = new ObjectId();
        when(ctx.pathParam("id")).thenReturn(id.toHexString());
        staticPrestataireDAO.when(() -> PrestataireDAO.findById(id)).thenReturn(null);

        controller.patch(ctx);

        verify(ctx).status(404);
        verify(ctx).result(anyString());
        verify(ctx, never()).json(any());
    }
}
