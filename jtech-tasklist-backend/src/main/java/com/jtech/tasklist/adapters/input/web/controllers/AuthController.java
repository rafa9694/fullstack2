package com.jtech.tasklist.adapters.input.web.controllers;

import com.jtech.tasklist.adapters.input.web.dto.request.LoginRequest;
import com.jtech.tasklist.adapters.input.web.dto.request.RefreshTokenRequest;
import com.jtech.tasklist.adapters.input.web.dto.request.RegisterRequest;
import com.jtech.tasklist.adapters.input.web.dto.response.AuthResponse;
import com.jtech.tasklist.adapters.input.web.dto.response.UserResponse;
import com.jtech.tasklist.application.core.domains.User;
import com.jtech.tasklist.ports.input.AuthenticateUserPort;
import com.jtech.tasklist.ports.input.RefreshTokenPort;
import com.jtech.tasklist.ports.input.RegisterUserPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de registro e login")
public class AuthController {

    private final RegisterUserPort registerUserPort;
    private final AuthenticateUserPort authenticateUserPort;
    private final RefreshTokenPort refreshTokenPort;

    public AuthController(RegisterUserPort registerUserPort,
                          AuthenticateUserPort authenticateUserPort,
                          RefreshTokenPort refreshTokenPort) {
        this.registerUserPort = registerUserPort;
        this.authenticateUserPort = authenticateUserPort;
        this.refreshTokenPort = refreshTokenPort;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar novo usuário")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = registerUserPort.execute(User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build());
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        var result = authenticateUserPort.execute(request.email(), request.password());
        return new AuthResponse(result.accessToken(), result.refreshToken());
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token de acesso")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        var result = refreshTokenPort.execute(request.refreshToken());
        return new AuthResponse(result.accessToken(), result.refreshToken());
    }
}
