package com.collabrix.notificationservice.model.dto.response;

import com.collabrix.notificationservice.model.entities.Notification;
import com.collabrix.notificationservice.model.enums.NotificationChannel;
import com.collabrix.notificationservice.model.enums.NotificationPriority;
import com.collabrix.notificationservice.model.enums.NotificationType;

import java.time.Instant;
import java.util.Map;

public record NotificationResponseDTO(
        String id,
        String recipientId,
        String recipientEmail,
        NotificationType type,
        NotificationChannel channel,
        NotificationPriority priority,
        String title,
        String body,
        boolean read,
        Instant createdAt,
        Instant sentAt,
        Instant deliveredAt,
        Instant readAt,
        Map<String, String> metadata
) {
    public static NotificationResponseDTO fromEntity(Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getRecipientId(),
                notification.getRecipientEmail(),
                notification.getType(),
                notification.getChannel(),
                notification.getPriority(),
                notification.getTitle(),
                notification.getBody(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getSentAt(),
                notification.getDeliveredAt(),
                notification.getReadAt(),
                notification.getMetadata()
        );
    }
}
