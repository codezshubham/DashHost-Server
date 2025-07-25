package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.ActivityLog;
import com.WebHost.Manager.Repository.ActivityLogRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityLogServiceImpl implements ActivityLogService{

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public void log(String userId, String username, String role, String action, String details) {
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setRole(role);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);
        activityLogRepository.save(log);
    }

    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    public List<ActivityLog> getLogsByUser(String userId) {
        return activityLogRepository.findByUserId(userId);
    }
}

