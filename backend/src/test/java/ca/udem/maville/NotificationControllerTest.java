package ca.udem.maville;

import ca.udem.maville.server.controllers.users.NotificationController;
import ca.udem.maville.server.dao.files.users.NotificationDAO;
import ca.udem.maville.server.models.users.Notification;
import io.javalin.http.Context;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock Context ctx;
    @Mock org.slf4j.Logger logger;

    NotificationController controller;

    MockedStatic<NotificationDAO> staticNotificationDAO;

    @BeforeEach
    void setUp() {
        controller = new NotificationController("http://fake", logger);

        staticNotificationDAO = mockStatic(NotificationDAO.class);

        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.contentType(anyString())).thenReturn(ctx);
        when(ctx.result(anyString())).thenReturn(ctx);
    }

    @AfterEach
    void tearDown() {
        staticNotificationDAO.close();
    }

     @Test
     void getById_found_returns200() {
         String idString = "64e4f3a2b5f3c8d1e2f4a6bb";
         ObjectId id = new ObjectId(idString);

         Notification found = new Notification("Test message", id, null);

         when(ctx.pathParam("id")).thenReturn(idString);
         staticNotificationDAO.when(() -> NotificationDAO.findById(id)).thenReturn(found);

         controller.getById(ctx);

         staticNotificationDAO.verify(() -> NotificationDAO.save(any(Notification.class)), times(1));
         verify(ctx).status(200);
         verify(ctx).contentType("application/json");
         verify(ctx).json(found);
     }

    @Test
    void getAll_returns200() {
        String userId = "64e4f3a2b5f3c8d1e2f4a6bc";
        ObjectId userObjectId = new ObjectId(userId);

        List<Notification> notifications = List.of(
            new Notification("Notification 1", userObjectId, null),
            new Notification("Notification 2", userObjectId, "http://url")
        );

        when(ctx.pathParam("user")).thenReturn(userId);
        staticNotificationDAO.when(() -> NotificationDAO.findUserNotifications(userObjectId)).thenReturn(notifications);

        controller.getAll(ctx);

        verify(ctx).status(200);
        verify(ctx).contentType("application/json");
        verify(ctx).json(notifications);
    }

    @Test
    void create_validNotification_returns201() {
        String userId = "64e4f3a2b5f3c8d1e2f4a6bd";
        ObjectId userObjectId = new ObjectId(userId);

        String jsonBody = "{\"user\":\"" + userId + "\", \"message\":\"Test message\", \"url\":\"http://test.com\"}";

        when(ctx.body()).thenReturn(jsonBody);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);

        controller.create(ctx);

        staticNotificationDAO.verify(() -> NotificationDAO.save(captor.capture()), times(1));

        Notification savedNotif = captor.getValue();

        verify(ctx).status(201);
        verify(ctx).contentType("application/json");
        verify(ctx).json(savedNotif);

        Assertions.assertEquals("Test message", savedNotif.getMessage());
        Assertions.assertEquals(userObjectId, savedNotif.getUser());
        Assertions.assertEquals("http://test.com", savedNotif.getUrl());
    }

}
