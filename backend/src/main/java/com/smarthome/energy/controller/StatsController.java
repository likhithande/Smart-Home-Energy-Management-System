package com.smarthome.energy.controller;

import com.smarthome.energy.entity.User;
import com.smarthome.energy.service.StatsService;
import com.smarthome.energy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;
    private final UserService userService;

    public StatsController(StatsService statsService, UserService userService) {
        this.statsService = statsService;
        this.userService = userService;
    }

    private Optional<User> findUser(String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestParam("email") String email) {
        return findUser(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(statsService.computeStats(user)))
                .orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
    }
}
