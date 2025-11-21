package com.danil.library.security;

import com.danil.library.dto.TokenPairResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Сервис для создания/обновления сессий и пар токенов.
 */
@Service
public class UserSessionService {

    private final UserSessionRepository sessionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserSessionService(UserSessionRepository sessionRepository,
                              JwtTokenProvider jwtTokenProvider) {
        this.sessionRepository = sessionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public TokenPairResponse createSession(UserDetails userDetails, String deviceId) {
        // генерим токены
        String access = jwtTokenProvider.generateAccessToken(userDetails);
        String refresh = jwtTokenProvider.generateRefreshToken(userDetails);

        Instant accessExp = jwtTokenProvider.getAccessTokenExpiryInstant();
        Instant refreshExp = jwtTokenProvider.getRefreshTokenExpiryInstant();

        UserSession session = UserSession.active(
                userDetails.getUsername(),
                deviceId,
                access,
                refresh,
                accessExp,
                refreshExp
        );

        sessionRepository.save(session);

        return new TokenPairResponse(access, refresh);
    }

    @Transactional
    public TokenPairResponse refreshTokens(String refreshToken, String deviceId) {
        var sessionOpt = sessionRepository.findByRefreshToken(refreshToken);

        if (sessionOpt.isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }

        UserSession session = sessionOpt.get();

        // проверка статуса и сроков
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new RuntimeException("Refresh token already used or revoked");
        }

        if (session.getRefreshTokenExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        // помечаем старую сессию как использованную
        session.setStatus(SessionStatus.USED);
        sessionRepository.save(session);

        // генерируем новую пару токенов
        // username берём из старой сессии
        var fakeUserDetails = org.springframework.security.core.userdetails.User
                .withUsername(session.getUsername())
                .password("") // пароль не нужен
                .authorities("ROLE_USER") // роль тут не важна для токена, но можно позже докрутить
                .build();

        String newAccess = jwtTokenProvider.generateAccessToken(fakeUserDetails);
        String newRefresh = jwtTokenProvider.generateRefreshToken(fakeUserDetails);

        Instant newAccessExp = jwtTokenProvider.getAccessTokenExpiryInstant();
        Instant newRefreshExp = jwtTokenProvider.getRefreshTokenExpiryInstant();

        UserSession newSession = UserSession.active(
                session.getUsername(),
                deviceId,
                newAccess,
                newRefresh,
                newAccessExp,
                newRefreshExp
        );

        sessionRepository.save(newSession);

        return new TokenPairResponse(newAccess, newRefresh);
    }
}
