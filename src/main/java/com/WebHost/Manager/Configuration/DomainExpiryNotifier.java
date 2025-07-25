package com.WebHost.Manager.Configuration;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Domain;
import com.WebHost.Manager.Model.Notification;
import com.WebHost.Manager.Repository.DomainRepository;
import com.WebHost.Manager.Repository.NotificationRepository;
import com.WebHost.Manager.Service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DomainExpiryNotifier {

    private final DomainRepository domainRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public DomainExpiryNotifier(DomainRepository domainRepository,
                                NotificationRepository notificationRepository,
                                EmailService emailService) {
        this.domainRepository = domainRepository;
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    // Run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * *")
    public void notifyExpiringDomains() {
        LocalDate today = LocalDate.now();
        LocalDate expiryThreshold = today.plusDays(7);

        List<Domain> expiringDomains = domainRepository.findByExpiryDate(expiryThreshold);

        for (Domain domain : expiringDomains) {
            Client client = domain.getClient();

            // Check if already notified
            boolean alreadyNotified = notificationRepository.findAll()
                    .stream()
                    .anyMatch(n -> n.getClient().getId().equals(client.getId())
                            && n.getScheduledDate().equals(today)
                            && n.getMessage().contains(domain.getDomainName())
                            && n.isSentStatus());

            if (alreadyNotified) continue;

            String message = "Your domain '" + domain.getDomainName() +
                    "' is expiring on " + domain.getExpiryDate() + ".";

            // Send email
            emailService.sendEmail(client.getContactEmail(), "Domain Expiry Notification", message);

            // Save notification
            Notification notification = new Notification();
            notification.setClient(client);
            notification.setMessage(message);
            notification.setScheduledDate(today);
            notification.setSentStatus(true);
            notificationRepository.save(notification);
        }
    }
}

