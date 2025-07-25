package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Notification;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification saveNotification(Notification notification);
    List<Notification> getPendingNotifications();
    List<Notification> getNotificationsByDate(LocalDate date);
    List<Notification> getAllNotifications();
    Optional<Notification> getNotificationById(Long id);
    void deleteNotification(Long id);
}
