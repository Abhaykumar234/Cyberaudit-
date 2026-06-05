package com.cyberaudit.service;

import com.cyberaudit.dto.LoginRequest;
import com.cyberaudit.dto.LoginResponse;
import com.cyberaudit.dto.RegisterRequest;
import com.cyberaudit.model.User;
import com.cyberaudit.model.enums.Role;
import com.cyberaudit.repository.UserRepository;
import com.cyberaudit.security.JwtService;
import com.cyberaudit.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditTrailService auditTrailService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            AuditTrailService auditTrailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.auditTrailService = auditTrailService;
    }

    public LoginResponse login(LoginRequest request, String sourceIp) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        user.setLastLogin(java.time.LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(principal);
        auditTrailService.log(principal.getUsername(), "USER_LOGIN", "User authenticated successfully",
                com.cyberaudit.model.enums.Severity.LOW, "AUTH", sourceIp, "auth");

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name(),
                user.getFullName());
    }

    @Transactional
    public LoginResponse register(RegisterRequest request, String sourceIp) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = userRepository.count() == 0 ? Role.SUPER_ADMIN : Role.VIEWER;

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRole(role);
        user.setEnabled(true);
        userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);
        String token = jwtService.generateToken(principal);
        auditTrailService.log(user.getUsername(), "USER_REGISTERED", "New user account created",
                com.cyberaudit.model.enums.Severity.MEDIUM, "AUTH", sourceIp, "auth");

        return new LoginResponse(token, user.getUsername(), user.getRole().name(), user.getFullName());
    }
}
