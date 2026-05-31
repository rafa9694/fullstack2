package com.jtech.tasklist.ports.input;

public interface RefreshTokenPort {
    record RefreshResult(String accessToken, String refreshToken) {}
    RefreshResult execute(String refreshToken);
}
