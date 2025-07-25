package com.WebHost.Manager.Repository;

import com.WebHost.Manager.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByScheduledDate(LocalDate date);
    List<Notification> findBySentStatusFalse();
    List<Notification> findByScheduledDateAndSentStatus(LocalDate date, boolean sentStatus);
}

