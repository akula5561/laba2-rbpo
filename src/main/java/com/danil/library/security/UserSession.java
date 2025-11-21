package com.danil.library.security;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Сущность для хранения информации о JWT-сессии пользователя.
 * Таблица: user_sessions
 */
@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // можно хранить username (у тебя логин = username)
    @Column(nullable = false)
    private String username;

    // идентификатор устройства/клиента (для лабы можно хранить User-Agent или что-то простое)
    private String deviceId;

    // access-токен можно не хранить, но для демонстрации оставим
    @Column(length = 512)
    private String accessToken;

    @Column(length = 512, nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Instant accessTokenExpiry;

    @Column(nullable = false)
    private Instant refreshTokenExpiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    public UserSession() {
    }

    public UserSession(UUID id,
                       String username,
                       String deviceId,
                       String accessToken,
                       String refreshToken,
                       Instant accessTokenExpiry,
                       Instant refreshTokenExpiry,
                       SessionStatus status) {
        this.id = id;
        this.username = username;
        this.deviceId = deviceId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
        this.status = status;
    }

    public static UserSession active(String username,
                                     String deviceId,
                                     String accessToken,
                                     String refreshToken,
                                     Instant accessTokenExpiry,
                                     Instant refreshTokenExpiry) {
        return new UserSession(
                null,
                username,
                deviceId,
                accessToken,
                refreshToken,
                accessTokenExpiry,
                refreshTokenExpiry,
                SessionStatus.ACTIVE
        );
    }

    // --- getters / setters ---

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getAccessTokenExpiry() {
        return accessTokenExpiry;
    }

    public void setAccessTokenExpiry(Instant accessTokenExpiry) {
        this.accessTokenExpiry = accessTokenExpiry;
    }

    public Instant getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public void setRefreshTokenExpiry(Instant refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
