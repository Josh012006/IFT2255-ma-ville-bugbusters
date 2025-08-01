package ca.udem.maville;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.controllers.SignalementController;
import ca.udem.maville.server.dao.files.SignalementDAO;
import ca.udem.maville.server.models.Signalement;
import ca.udem.maville.utils.RequestType;
import io.javalin.http.Context;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignalementControllerTest {

    @Mock Context ctx;
    @Mock org.slf4j.Logger logger;

    SignalementController controller;

    MockedStatic<SignalementDAO> staticSignalementDAO;
    MockedStatic<UseRequest> staticUseRequest;

    @BeforeEach
    void setUp() {
        controller = new SignalementController("http://fake/", logger);

        staticSignalementDAO = mockStatic(SignalementDAO.class);
        staticUseRequest = mockStatic(UseRequest.class);

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.contentType(anyString())).thenReturn(ctx);
        when(ctx.result(anyString())).thenReturn(ctx);
    }

    @AfterEach
    void tearDown() {
        staticSignalementDAO.close();
        staticUseRequest.close();
    }

    @Test
    void getAll_returns200() {
        List<Signalement> signalements = Arrays.asList(new Signalement(), new Signalement());

        staticSignalementDAO.when(SignalementDAO::findAll).thenReturn(signalements);

        controller.getAll(ctx);

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(signalements);
    }

    @Test
    void create_validSignalement_returns201() throws Exception {
        Signalement newSignalement = new Signalement();

        when(ctx.bodyAsClass(Signalement.class)).thenReturn(newSignalement);

        staticUseRequest.when(() -> UseRequest.sendRequest(anyString(), eq(RequestType.POST), anyString()))
                .thenReturn("{\"status\":201,\"data\":{}}");

        controller.create(ctx);

        staticSignalementDAO.verify(() -> SignalementDAO.save(newSignalement), times(1));

        verify(ctx).status(201);
        verify(ctx).contentType("application/json");
        verify(ctx).json(newSignalement);
    }

    @Test
    void getById_found_returns200() {
        String idString = "64e4f3a2b5f3c8d1e2f4a6bb";
        ObjectId id = new ObjectId(idString);
        Signalement signalement = new Signalement();

        when(ctx.pathParam("id")).thenReturn(idString);
        staticSignalementDAO.when(() -> SignalementDAO.findById(id)).thenReturn(signalement);

        controller.getById(ctx);

        staticSignalementDAO.verify(() -> SignalementDAO.findById(any()), times(1));

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(signalement);
    }
}