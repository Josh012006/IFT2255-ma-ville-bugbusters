package ca.udem.maville;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.controllers.CandidatureController;
import ca.udem.maville.server.dao.files.CandidatureDAO;
import ca.udem.maville.server.models.Candidature;
import io.javalin.http.Context;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CandidatureControllerTest {

    @Mock Context ctx;
    @Mock org.slf4j.Logger logger;

    CandidatureController controller;

    MockedStatic<CandidatureDAO> staticCandidatureDAO;
    MockedStatic<UseRequest> staticUseRequest;

    @BeforeEach
    void setUp() {
        controller = new CandidatureController("http://fake", logger);

        staticCandidatureDAO = mockStatic(CandidatureDAO.class);
        staticUseRequest = mockStatic(UseRequest.class);

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.contentType(anyString())).thenReturn(ctx);
        when(ctx.result(anyString())).thenReturn(ctx);

        staticUseRequest.when(() -> UseRequest.sendRequest(
        anyString(), any(), anyString()
        )).thenReturn("{\"status\":201}");


    }

    @AfterEach
    void tearDown() {
        staticCandidatureDAO.close();
        staticUseRequest.close();
    }

    @Test
    void create_validCandidature_returns201() throws Exception {
        Candidature fake = new Candidature(
            new ObjectId("64e4f3a2b5f3c8d1e2f4a6b7"),
            new ArrayList<>(List.of("Rue Sainte-Catherine", "Rue Sherbrooke")),
            "ENT-123456",
            new ObjectId("64e4f3a2b5f3c8d1e2f4a6b8"),
            "Prestataire Montréal Inc.",
            new ObjectId("64e4f3a2b5f3c8d1e2f4a6b9"),
            "Réfection du pavé du boulevard",
            "Remplacement des pavés fissurés, nivellement et réfection de la surface pour améliorer la sécurité.",
            "Réparation routière",
            new Date(),
            new Date(),
            12500.50
        );

        
        when(ctx.bodyAsClass(Candidature.class)).thenReturn(fake);

        
        controller.create(ctx);

        
        staticCandidatureDAO.verify(() -> CandidatureDAO.save(fake), times(1));

       
        verify(ctx).status(201);
        verify(ctx).contentType("application/json");
        verify(ctx).json(fake);
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        String idString = "64e4f3a2b5f3c8d1e2f4a6ba";
        ObjectId id = new ObjectId(idString);
        when(ctx.pathParam("id")).thenReturn(idString);
        staticCandidatureDAO.when(() -> CandidatureDAO.findById(id)).thenReturn(null);
        controller.getById(ctx);
        verify(ctx).status(404);
        verify(ctx).result(anyString());
        verify(ctx, never()).json(any(Candidature.class));
    }

    @Test
    void getById_found_returns200() throws Exception {
        String idString = "64e4f3a2b5f3c8d1e2f4a6bb";
        ObjectId id = new ObjectId(idString);
        when(ctx.pathParam("id")).thenReturn(idString);

        Candidature found = new Candidature(
            id,
            new ArrayList<>(List.of("Rue Saint-Denis")),
            "ENT-654321",
            new ObjectId("64e4f3a2b5f3c8d1e2f4a6bc"),
            "Prestataire Exemple",
            new ObjectId("64e4f3a2b5f3c8d1e2f4a6bd"),
            "Projet test",
            "Description test",
            "Type test",
            new Date(),
            new Date(),
            5000.0
        );
        staticCandidatureDAO.when(() -> CandidatureDAO.findById(id)).thenReturn(found);
        controller.getById(ctx);
        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(found);
    }


}
