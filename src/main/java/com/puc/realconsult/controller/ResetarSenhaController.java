package com.puc.realconsult.controller;

import com.puc.realconsult.dto.RedefinirSenhaRequest;
import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.model.ResetSenhaToken;
import com.puc.realconsult.repository.ResetSenhaTokenRepository;
import com.puc.realconsult.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ResetarSenhaController {
    private final ResetSenhaTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody RedefinirSenhaRequest request) {
        Optional<ResetSenhaToken> tokenOpt = tokenRepository.findByToken(request.getToken());

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido.");
        }

        ResetSenhaToken resetToken = tokenOpt.get();

        if (resetToken.isExpired()) {
            return ResponseEntity.badRequest().body("Token expirado.");
        }

        UserModel usuario = resetToken.getUsuario();
        usuario.setSenha(request.getNovaSenha()); // depois vamos criptografar
        userRepository.save(usuario);

        // opcional: remover token após uso
        tokenRepository.delete(resetToken);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }
}
