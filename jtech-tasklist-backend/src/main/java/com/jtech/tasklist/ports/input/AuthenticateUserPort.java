package com.jtech.tasklist.ports.input;

public interface AuthenticateUserPort {
    record AuthResult(String accessToken, String refreshToken) {}
    AuthResult execute(String email, String password);
}
