package com.collabrix.notificationservice.testsupport;

import com.collabrix.notificationservice.kafka.events.AuthNotificationEvent;
import com.collabrix.notificationservice.kafka.events.ChatMessageNotificationEvent;
import com.collabrix.notificationservice.kafka.events.CommentNotificationEvent;
import com.collabrix.notificationservice.kafka.events.TaskNotificationEvent;
import com.collabrix.notificationservice.kafka.events.WorkspaceNotificationEvent;
import com.collabrix.notificationservice.model.entities.Notification;
import com.collabrix.notificationservice.model.enums.NotificationChannel;
import com.collabrix.notificationservice.model.enums.NotificationPriority;
import com.collabrix.notificationservice.model.enums.NotificationType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public final class NotificationTestFixtures {

    private NotificationTestFixtures() {
    }

    public static Notification notification(String recipientId) {
        return Notification.builder()
                .id(UUID.randomUUID().toString())
                .recipientId(recipientId)
                .recipientEmail("user@collabrix.test")
                .title("Task assigned")
                .body("A task was assigned to you.")
                .type(NotificationType.TASK_ASSIGNED)
                .channel(NotificationChannel.IN_APP)
                .priority(NotificationPriority.MEDIUM)
                .metadata(Map.of("taskId", "task-1"))
                .createdAt(Instant.parse("2026-04-15T12:00:00Z"))
                .updatedAt(Instant.parse("2026-04-15T12:05:00Z"))
                .read(false)
                .retryCount(0)
                .maxRetries(3)
                .build();
    }

    public static AuthNotificationEvent authEvent() {
        return new AuthNotificationEvent(
                "EMAIL_CONFIRMATION",
                UUID.randomUUID().toString(),
                "user@collabrix.test",
                "john",
                "token-123",
                "http://collabrix.test/confirm?token=token-123",
                Instant.now()
        );
    }

    public static TaskNotificationEvent taskEvent(String eventType) {
        return new TaskNotificationEvent(
                eventType,
                UUID.randomUUID().toString(),
                null,
                "Mary",
                UUID.randomUUID().toString(),
                "John",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Platform",
                UUID.randomUUID().toString(),
                "Prepare release",
                "TODO",
                LocalDate.now().plusDays(1),
                Instant.now()
        );
    }

    public static CommentNotificationEvent commentEvent(String eventType) {
        return new CommentNotificationEvent(
                eventType,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "john_dev",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Prepare release",
                "Review completed",
                "mary_dev",
                Instant.now()
        );
    }

    public static ChatMessageNotificationEvent chatMessageEvent(String eventType) {
        return new ChatMessageNotificationEvent(
                eventType,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "hello from chat",
                Instant.now()
        );
    }

    public static WorkspaceNotificationEvent workspaceEvent(String eventType) {
        return new WorkspaceNotificationEvent(
                eventType,
                UUID.randomUUID().toString(),
                null,
                "Mary",
                UUID.randomUUID().toString(),
                "Kate",
                UUID.randomUUID().toString(),
                "John",
                UUID.randomUUID().toString(),
                "Platform",
                UUID.randomUUID().toString(),
                "ADMIN",
                Instant.now()
        );
    }
}
