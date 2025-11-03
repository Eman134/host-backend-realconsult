package com.puc.realconsult.controller;

import com.puc.realconsult.dto.UserDTO;
import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal) {
        UserModel u = userRepository.findByEmail(principal.getUsername());
        if (u == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(toDTO(u));
    }

    // Atualizar campos simples do próprio usuário (ex.: avatarColor)
    @PatchMapping("/me")
    public ResponseEntity<?> patchMe(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody Map<String, Object> payload) {

        UserModel u = userRepository.findByEmail(principal.getUsername());
        if (u == null) return ResponseEntity.status(401).build();

        if (payload.containsKey("avatarColor")) {
            u.setAvatarColor(String.valueOf(payload.get("avatarColor")));
        }
        userRepository.save(u);
        return ResponseEntity.ok(toDTO(u));
    }

    // Upload do avatar (multipart). Adapte a estratégia de armazenamento.
    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        UserModel u = userRepository.findByEmail(principal.getUsername());
        if (u == null) return ResponseEntity.status(401).build();

        // TODO: salvar arquivo em disco/S3 e setar URL no usuário.
        // Exemplo placeholder:
        // String url = avatarStorageService.save(u.getId(), file);
        // u.setAvatarUrl(url);

        userRepository.save(u);
        return ResponseEntity.ok(toDTO(u));
    }

    // Trocar senha autenticado
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody ChangePasswordRequest req) {

        UserModel u = userRepository.findByEmail(principal.getUsername());
        if (u == null) return ResponseEntity.status(401).build();

        if (!passwordEncoder.matches(req.getCurrentPassword(), u.getSenha())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Senha atual incorreta"));
        }
        u.setSenha(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    private UserDTO toDTO(UserModel u) {
        return new UserDTO(
                u.getId(),
                u.getNome(),
                u.getCargo(),
                u.getEmail(),
                u.getStatus(),
                u.getAvatarColor()
        );
    }

    @Data
    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;
    }
}
