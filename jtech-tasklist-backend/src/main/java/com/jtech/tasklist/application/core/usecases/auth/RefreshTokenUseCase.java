package com.jtech.tasklist.application.core.usecases.auth;

import com.jtech.tasklist.ports.input.RefreshTokenPort;
import com.jtech.tasklist.ports.output.JwtServicePort;
import com.jtech.tasklist.ports.output.UserRepositoryPort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenUseCase implements RefreshTokenPort {

    private final JwtServicePort jwtServicePort;
    private final UserRepositoryPort userRepositoryPort;

    public RefreshTokenUseCase(JwtServicePort jwtServicePort, UserRepositoryPort userRepositoryPort) {
        this.jwtServicePort = jwtServicePort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public RefreshResult execute(String refreshToken) {
        String email = jwtServicePort.extractEmail(refreshToken);

        userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Token inválido"));

        if (jwtServicePort.isTokenExpired(refreshToken)) {
            throw new BadCredentialsException("Refresh token expirado");
        }

        String newAccessToken = jwtServicePort.generateAccessToken(email);
        String newRefreshToken = jwtServicePort.generateRefreshToken(email);

        return new RefreshResult(newAccessToken, newRefreshToken);
    }
}
