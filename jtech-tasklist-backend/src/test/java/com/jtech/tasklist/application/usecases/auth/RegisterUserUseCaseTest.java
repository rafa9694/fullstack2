package com.jtech.tasklist.application.usecases.auth;

import com.jtech.tasklist.application.core.domains.User;
import com.jtech.tasklist.application.core.exceptions.EmailAlreadyExistsException;
import com.jtech.tasklist.application.core.usecases.auth.RegisterUserUseCase;
import com.jtech.tasklist.ports.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserUseCase")
class RegisterUserUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private RegisterUserUseCase useCase;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .name("João Silva")
                .email("joao@example.com")
                .password("senha123")
                .build();
    }

    @Test
    @DisplayName("deve registrar usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        when(userRepositoryPort.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validUser.getPassword())).thenReturn("hashed_password");
        when(userRepositoryPort.save(any(User.class))).thenReturn(
                User.builder().id(1L).name(validUser.getName())
                        .email(validUser.getEmail()).password("hashed_password")
                        .createdAt(LocalDateTime.now()).build());

        User result = useCase.execute(validUser);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(validUser.getEmail());
        verify(passwordEncoder).encode(validUser.getPassword());
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    @DisplayName("deve lançar exceção quando email já existe")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepositoryPort.existsByEmail(validUser.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(validUser))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(validUser.getEmail());

        verify(userRepositoryPort, never()).save(any());
    }
}
