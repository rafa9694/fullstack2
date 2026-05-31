package com.jtech.tasklist.application.core.usecases.auth;

import com.jtech.tasklist.application.core.domains.User;
import com.jtech.tasklist.ports.input.AuthenticateUserPort;
import com.jtech.tasklist.ports.output.JwtServicePort;
import com.jtech.tasklist.ports.output.UserRepositoryPort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase implements AuthenticateUserPort {

    private final UserRepositoryPort userRepositoryPort;
    private final JwtServicePort jwtServicePort;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateUserUseCase(UserRepositoryPort userRepositoryPort,
                                   JwtServicePort jwtServicePort,
                                   PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.jwtServicePort = jwtServicePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResult execute(String email, String password) {
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String accessToken = jwtServicePort.generateAccessToken(user.getEmail());
        String refreshToken = jwtServicePort.generateRefreshToken(user.getEmail());

        return new AuthResult(accessToken, refreshToken);
    }
}
