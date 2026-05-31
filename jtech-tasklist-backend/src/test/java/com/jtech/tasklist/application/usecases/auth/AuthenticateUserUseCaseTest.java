package com.jtech.tasklist.application.usecases.auth;

import com.jtech.tasklist.application.core.domains.User;
import com.jtech.tasklist.application.core.usecases.auth.AuthenticateUserUseCase;
import com.jtech.tasklist.ports.input.AuthenticateUserPort.AuthResult;
import com.jtech.tasklist.ports.output.JwtServicePort;
import com.jtech.tasklist.ports.output.UserRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticateUserUseCase")
class AuthenticateUserUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private JwtServicePort jwtServicePort;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private AuthenticateUserUseCase useCase;

    @Test
    @DisplayName("deve autenticar com sucesso e retornar tokens")
    void shouldAuthenticateSuccessfully() {
        User user = User.builder().id(1L).email("user@test.com").password("hashed").build();
        when(userRepositoryPort.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPassword", "hashed")).thenReturn(true);
        when(jwtServicePort.generateAccessToken("user@test.com")).thenReturn("access_token");
        when(jwtServicePort.generateRefreshToken("user@test.com")).thenReturn("refresh_token");

        AuthResult result = useCase.execute("user@test.com", "rawPassword");

        assertThat(result.accessToken()).isEqualTo("access_token");
        assertThat(result.refreshToken()).isEqualTo("refresh_token");
    }

    @Test
    @DisplayName("deve lançar exceção quando usuário não existe")
    void shouldThrowWhenUserNotFound() {
        when(userRepositoryPort.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("notfound@test.com", "pass"))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("deve lançar exceção quando senha incorreta")
    void shouldThrowWhenPasswordIncorrect() {
        User user = User.builder().email("user@test.com").password("hashed").build();
        when(userRepositoryPort.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("user@test.com", "wrongPassword"))
                .isInstanceOf(BadCredentialsException.class);
    }
}
