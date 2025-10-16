package com.puc.realconsult.controller;

import com.puc.realconsult.config.HttpsConfig;
import com.puc.realconsult.dto.AutenticarDTO;
import com.puc.realconsult.dto.EsqueceuSenhaRequest;
import com.puc.realconsult.dto.RedefinirSenhaRequest;
import com.puc.realconsult.dto.loginUsuarioDTO;
import com.puc.realconsult.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String COOKIE_NAME = "token";
    private static final String COOKIE_PATH = "/";

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final HttpsConfig httpsConfig;

    @PostMapping("/login")
    public ResponseEntity<loginUsuarioDTO> login(@RequestBody @Valid AutenticarDTO data,
                                                 jakarta.servlet.http.HttpServletResponse response) {

        var result = authService.login(data, authenticationManager);

        // Apaga cookie antigo (mesmos atributos)
        ResponseCookie del = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(httpsConfig.isHttps())
                .sameSite("None")
                .path(COOKIE_PATH)
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, del.toString());

        // Cria cookie novo (cross-site)
        ResponseCookie jwt = ResponseCookie.from(COOKIE_NAME, result.token())
                .httpOnly(true)
                .secure(httpsConfig.isHttps())   // true em produção/HTTPS
                .sameSite("None")                // necessário para front em outro domínio
                .path(COOKIE_PATH)
                .maxAge(Duration.ofDays(1))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwt.toString());

        return ResponseEntity.ok(result.user());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(jakarta.servlet.http.HttpServletResponse response) {
        ResponseCookie del = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(httpsConfig.isHttps())
                .sameSite("None")
                .path(COOKIE_PATH)
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, del.toString());
        return ResponseEntity.ok("Logout bem-sucedido");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody RedefinirSenhaRequest request) {
        try {
            return ResponseEntity.ok(authService.resetPassword(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EsqueceuSenhaRequest request) {
        try {
            authService.forgotPassword(request);
            return ResponseEntity.ok("E-mail de redefinição enviado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
