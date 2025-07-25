package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.ActivityLog;

import java.util.List;

public interface ActivityLogService {
    List<ActivityLog> getAllLogs();
    List<ActivityLog> getLogsByUser(String userId);

}
