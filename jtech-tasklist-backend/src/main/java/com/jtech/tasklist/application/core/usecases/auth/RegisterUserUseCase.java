package com.jtech.tasklist.application.core.usecases.auth;

import com.jtech.tasklist.application.core.domains.User;
import com.jtech.tasklist.application.core.exceptions.EmailAlreadyExistsException;
import com.jtech.tasklist.ports.input.RegisterUserPort;
import com.jtech.tasklist.ports.output.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegisterUserUseCase implements RegisterUserPort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(User user) {
        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        User userToSave = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        return userRepositoryPort.save(userToSave);
    }
}
