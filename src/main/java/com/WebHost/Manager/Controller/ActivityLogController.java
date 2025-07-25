package com.WebHost.Manager.Controller;

import com.WebHost.Manager.Configuration.JwtTokenProvider;
import com.WebHost.Manager.Model.ActivityLog;
import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Service.ActivityLogService;
import com.WebHost.Manager.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public ActivityLogController(ActivityLogService activityLogService, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.activityLogService = activityLogService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActivityLog>> getAllLogs(@RequestHeader("Authorization") String token) {
        // Extract email from JWT token
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> userOptional = userService.getUserByEmail(email);

        // Check if user exists; if not, return 401 Unauthorized
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // If valid user, return all logs
        return ResponseEntity.ok(activityLogService.getAllLogs());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityLog>> getLogsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(activityLogService.getLogsByUser(userId));
    }
}
