package ca.udem.maville;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.controllers.ProblemController;
import ca.udem.maville.server.dao.files.ProblemDAO;
import ca.udem.maville.server.models.FicheProbleme;
import io.javalin.http.Context;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProblemControllerTest {

    @Mock Context ctx;
    @Mock org.slf4j.Logger logger;

    ProblemController controller;

    MockedStatic<ProblemDAO> staticProblemDAO;
    MockedStatic<UseRequest> staticUseRequest;

    @BeforeEach
    void setUp() {
        controller = new ProblemController("http://fake", logger);

        staticProblemDAO = mockStatic(ProblemDAO.class);
        staticUseRequest = mockStatic(UseRequest.class);

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.contentType(anyString())).thenReturn(ctx);
        when(ctx.result(anyString())).thenReturn(ctx);
    }

    @AfterEach
    void tearDown() {
        staticProblemDAO.close();
        staticUseRequest.close();
    }

    @Test
    void getAll_returnsAllProblems() throws Exception {
        List<FicheProbleme> problems = new ArrayList<>();
        problems.add(new FicheProbleme());
        problems.add(new FicheProbleme());

        staticProblemDAO.when(ProblemDAO::findAll).thenReturn(problems);

        controller.getAll(ctx);

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(problems);
    }
    
    @Test
    void getById_notFound_returns404() throws Exception {
        String idString = "64e4f3a2b5f3c8d1e2f4a6ab";
        ObjectId id = new ObjectId(idString);

        when(ctx.pathParam("id")).thenReturn(idString);
        staticProblemDAO.when(() -> ProblemDAO.findById(id)).thenReturn(null);

        controller.getById(ctx);

        verify(ctx).status(404);
        verify(ctx).result(anyString());
        verify(ctx, never()).json(any(FicheProbleme.class));
    }

    @Test
    void create_internalError_returns500() throws Exception {
        when(ctx.bodyAsClass(FicheProbleme.class)).thenThrow(new RuntimeException("Erreur interne"));

        controller.create(ctx);

        verify(ctx).status(500);
        verify(ctx).result(anyString());
        verify(ctx).contentType("application/json");
    }
}
