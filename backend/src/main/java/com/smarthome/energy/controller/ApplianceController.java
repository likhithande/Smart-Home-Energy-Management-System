package com.smarthome.energy.controller;

import com.smarthome.energy.dto.ApplianceDto;
import com.smarthome.energy.entity.User;
import com.smarthome.energy.service.ApplianceService;
import com.smarthome.energy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {

    private final ApplianceService applianceService;
    private final UserService userService;

    public ApplianceController(ApplianceService applianceService, UserService userService) {
        this.applianceService = applianceService;
        this.userService = userService;
    }

    private Optional<User> findUser(String email) {
        return userService.findByEmail(email);
    }

    @GetMapping
    public ResponseEntity<?> getAppliances(@RequestParam("email") String email) {
        return findUser(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(applianceService.getAppliancesForUser(user)))
                .orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
    }

    @PostMapping
    public ResponseEntity<?> saveAppliances(@RequestParam("email") String email,
                                            @Valid @RequestBody List<ApplianceDto> appliances) {
        return findUser(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(applianceService.saveAppliancesForUser(user, appliances)))
                .orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
    }
}
