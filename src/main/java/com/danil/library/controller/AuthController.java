package com.danil.library.controller;

import com.danil.library.dto.LoginRequest;
import com.danil.library.dto.RefreshRequest;
import com.danil.library.dto.TokenPairResponse;
import com.danil.library.security.UserSessionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserSessionService userSessionService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserSessionService userSessionService) {
        this.authenticationManager = authenticationManager;
        this.userSessionService = userSessionService;
    }

    @PostMapping("/login")
    public TokenPairResponse login(@RequestBody LoginRequest request,
                                   @RequestHeader(value = "User-Agent", required = false) String deviceId) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        return userSessionService.createSession(user, deviceId);
    }

    @PostMapping("/refresh")
    public TokenPairResponse refresh(@RequestBody RefreshRequest request,
                                     @RequestHeader(value = "User-Agent", required = false) String deviceId) {

        return userSessionService.refreshTokens(request.getRefreshToken(), deviceId);
    }
}
