package com.puc.realconsult.controller;

import com.puc.realconsult.config.HttpsConfig;
import com.puc.realconsult.config.SecurityConfig;
import com.puc.realconsult.config.TokenService;
import com.puc.realconsult.dto.AutenticarDTO;
import com.puc.realconsult.dto.EsqueceuSenhaRequest;
import com.puc.realconsult.dto.RedefinirSenhaRequest;
import com.puc.realconsult.dto.loginUsuarioDTO;
import com.puc.realconsult.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    private final HttpsConfig httpsConfig;

    private boolean https;

    @PostMapping("/login")
    public ResponseEntity<loginUsuarioDTO> login(@RequestBody @Valid AutenticarDTO data, HttpServletResponse response) {

        // Faz autenticação e gera token novo
        var result = authService.login(data, authenticationManager);

        // Limpa cookie antigo, caso exista
        Cookie oldCookie = new Cookie("token", null);
        oldCookie.setPath("/");
        oldCookie.setHttpOnly(true);
        oldCookie.setMaxAge(0);
        response.addCookie(oldCookie);

        // Adiciona cookie com token novo
        Cookie cookie = new Cookie("token", result.token());
        cookie.setHttpOnly(true);
        cookie.setSecure(httpsConfig.isHttps());
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        // Retorna DTO do usuário
        return ResponseEntity.ok(result.user());
    }



    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok("Logout bem-sucedido");
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody RedefinirSenhaRequest request) {
        try {
            String msg = authService.resetPassword(request);
            return ResponseEntity.ok(msg);
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

