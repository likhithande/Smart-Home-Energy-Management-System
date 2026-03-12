package com.smarthome.energy.service;

import com.smarthome.energy.dto.LoginRequest;
import com.smarthome.energy.dto.SignupRequest;
import com.smarthome.energy.dto.UserResponse;
import com.smarthome.energy.entity.User;
import com.smarthome.energy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse signup(SignupRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setFullname(request.getFullname());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setInterest(request.getInterest());
        user.setPasswordHash(hashPassword(request.getPassword()));

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public Optional<UserResponse> login(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> user.getPasswordHash().equals(hashPassword(request.getPassword())))
                .map(this::toResponse);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void resetPassword(String email, String password, String confirmPassword) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || confirmPassword == null) {
            throw new IllegalArgumentException("Password is required");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email is not registered"));

        user.setPasswordHash(hashPassword(password));
        userRepository.save(user);
    }

    private String hashPassword(String raw) {
        byte[] bytes = raw.getBytes(StandardCharsets.UTF_8);
        return DigestUtils.md5DigestAsHex(bytes);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullname(user.getFullname());
        response.setPhone(user.getPhone());
        response.setGender(user.getGender());
        response.setAddress(user.getAddress());
        response.setEmail(user.getEmail());
        response.setInterest(user.getInterest());
        return response;
    }
}
