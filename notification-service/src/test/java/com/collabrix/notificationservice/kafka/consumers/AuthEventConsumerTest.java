package com.collabrix.notificationservice.kafka.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.collabrix.notificationservice.kafka.events.AuthNotificationEvent;
import com.collabrix.notificationservice.mail.MailService;
import com.collabrix.notificationservice.model.entities.Notification;
import com.collabrix.notificationservice.model.enums.NotificationChannel;
import com.collabrix.notificationservice.model.enums.NotificationPriority;
import com.collabrix.notificationservice.model.enums.NotificationType;
import com.collabrix.notificationservice.service.NotificationFactory;
import com.collabrix.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.collabrix.notificationservice.testsupport.NotificationTestFixtures.authEvent;
import static com.collabrix.notificationservice.testsupport.NotificationTestFixtures.notification;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthEventConsumerTest {

    @Mock
    private NotificationFactory notificationFactory;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MailService mailService;

    private AuthEventConsumer authEventConsumer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        authEventConsumer = new AuthEventConsumer(objectMapper, notificationFactory, notificationService, mailService, "auth-events");
    }

    @Test
    void consume_emailConfirmationPayload_persistsNotificationAndSendsEmail() throws Exception {
        AuthNotificationEvent event = authEvent();
        Notification notification = Notification.builder()
                .id("notification-1")
                .recipientId(event.recipientId())
                .recipientEmail(event.recipientEmail())
                .title("Confirm your Collabrix account")
                .body("Finish your registration by confirming your email address.")
                .type(NotificationType.EMAIL_CONFIRMATION)
                .channel(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .build();

        when(notificationFactory.createAuthNotification(any())).thenReturn(notification);
        when(notificationService.save(notification)).thenReturn(notification);

        authEventConsumer.consume(objectMapper.writeValueAsString(event));

        verify(notificationService).save(notification);
        verify(mailService).sendConfirmationEmail(notification, event.confirmationUrl());
    }

    @Test
    void consume_invalidPayload_swallowsErrorWithoutPersisting() {
        authEventConsumer.consume("{not-json");

        verify(notificationService, never()).save(any());
        verify(mailService, never()).sendConfirmationEmail(any(), any());
    }
}
