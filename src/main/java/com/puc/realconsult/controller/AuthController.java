package com.puc.realconsult.controller;

import com.puc.realconsult.config.HttpsConfig;
import com.puc.realconsult.config.TokenService;
import com.puc.realconsult.dto.*;
import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.repository.UserRepository;
import com.puc.realconsult.service.AuthService;
import com.puc.realconsult.utils.RecoverToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String COOKIE_NAME = "token";
    private static final String COOKIE_PATH = "/";

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final HttpsConfig httpsConfig;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        String token = RecoverToken.recoverToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("message", "Token obrigatório"));
        }

        try {
            String email = tokenService.validarToken(token);

            UserModel userModel = userRepository.findByEmail(email);
            if (userModel == null) {
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                        .body(Map.of("message", "Usuário não encontrado"));
            }

            var dto = new UserDTO(
                    userModel.getId(),
                    userModel.getNome(),
                    userModel.getCargo(),
                    userModel.getEmail(),
                    userModel.getStatus(),
                    userModel.getAvatarColor()
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("message", "Token inválido"));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<loginUsuarioDTO> login(@RequestBody @Valid AutenticarDTO data,
                                                 HttpServletResponse response) {
        var result = authService.login(data, authenticationManager);

        boolean https = httpsConfig.isHttps();
        boolean crossSite = httpsConfig.isCrossSite();

        ResponseCookie.ResponseCookieBuilder delB = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .path(COOKIE_PATH)
                .maxAge(0);

        if (crossSite) {
            delB.sameSite("None").secure(https);
        } else {
            delB.sameSite("Lax").secure(https);
        }
        response.addHeader(HttpHeaders.SET_COOKIE, delB.build().toString());

        ResponseCookie.ResponseCookieBuilder jwtB = ResponseCookie.from(COOKIE_NAME, result.token())
                .httpOnly(true)
                .path(COOKIE_PATH)
                .maxAge(Duration.ofDays(1));

        if (crossSite) {
            jwtB.sameSite("None").secure(https);
        } else {
            jwtB.sameSite("Lax").secure(https);
        }
        response.addHeader(HttpHeaders.SET_COOKIE, jwtB.build().toString());

        return ResponseEntity.ok(result.user());
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(jakarta.servlet.http.HttpServletResponse response) {
        boolean crossSite = httpsConfig.isCrossSite();
        ResponseCookie del = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(httpsConfig.isHttps())
                .sameSite(crossSite ? "None" : "Lax")
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
