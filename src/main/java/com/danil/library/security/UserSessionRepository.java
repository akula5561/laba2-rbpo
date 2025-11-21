package com.danil.library.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с таблицей user_sessions.
 */
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    Optional<UserSession> findByRefreshToken(String refreshToken);
}
