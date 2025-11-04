package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.service.AdminDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/dashboard")
// Enables Cross-Origin Resource Sharing (CORS) for all endpoints in this controller,
// allowing requests from any origin. This is useful for development with separate frontends.
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS})
public class AdminDashboardController {
    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("counts")
    public ResponseEntity<?> totalPendingMessVerification(){
        Map<String, Integer> counts = new HashMap<>();
        counts.put("pending-mess-verification", adminDashboardService.totalPendingMessVerification());
        counts.put("total-mess", adminDashboardService.totalMess());
        counts.put("total-customers", adminDashboardService.totalCustomers());
        return new ResponseEntity<>(counts, HttpStatus.OK);
    }
}
