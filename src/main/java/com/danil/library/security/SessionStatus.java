package com.danil.library.security;

/**
 * Статус сессии / refresh-токена.
 */
public enum SessionStatus {
    ACTIVE,   // сессия активна, refresh-токен можно использовать
    USED,     // refresh-токен уже был использован для обновления
    REVOKED   // сессия отозвана (например, при логауте/подозрении на взлом)
}
