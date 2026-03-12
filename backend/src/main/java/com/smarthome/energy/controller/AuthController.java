package com.smarthome.energy.controller;

import com.smarthome.energy.dto.LoginRequest;
import com.smarthome.energy.dto.ResetPasswordRequest;
import com.smarthome.energy.dto.SignupRequest;
import com.smarthome.energy.dto.UserResponse;
import com.smarthome.energy.service.OtpService;
import com.smarthome.energy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            String message = otpService.sendOtp(email);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/send-reset-otp")
    public ResponseEntity<String> sendResetOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (userService.findByEmail(email).isEmpty()) {
            return ResponseEntity.badRequest().body("Email is not registered");
        }

        try {
            String message = otpService.sendOtp(email);
            return ResponseEntity.ok(message);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        boolean isValid = otpService.verifyOtp(email, otp);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError() != null
                    ? bindingResult.getFieldError().getDefaultMessage()
                    : "Invalid signup request";
            return ResponseEntity.badRequest().body(message);
        }

        if (!otpService.isEmailVerified(request.getEmail())) {
            return ResponseEntity.badRequest().body("Please verify OTP before signup");
        }

        try {
            UserResponse response = userService.signup(request);
            otpService.clearVerification(request.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError() != null
                    ? bindingResult.getFieldError().getDefaultMessage()
                    : "Invalid login request";
            return ResponseEntity.badRequest().body(message);
        }

        Optional<UserResponse> user = userService.login(request);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError() != null
                    ? bindingResult.getFieldError().getDefaultMessage()
                    : "Invalid reset password request";
            return ResponseEntity.badRequest().body(message);
        }

        if (!otpService.isEmailVerified(request.getEmail())) {
            return ResponseEntity.badRequest().body("Please verify OTP before resetting password");
        }

        try {
            userService.resetPassword(request.getEmail(), request.getPassword(), request.getConfirmPassword());
            otpService.clearVerification(request.getEmail());
            return ResponseEntity.ok("Password reset successful");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
