package com.jtech.tasklist.ports.output;

public interface JwtServicePort {
    String generateAccessToken(String email);
    String generateRefreshToken(String email);
    String extractEmail(String token);
    boolean isTokenValid(String token, String email);
    boolean isTokenExpired(String token);
}
