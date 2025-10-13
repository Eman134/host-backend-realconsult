package com.puc.realconsult.controller;

import com.puc.realconsult.dto.EsqueceuSenhaRequest;
import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.model.ResetSenhaToken;
import com.puc.realconsult.repository.UserRepository;
import com.puc.realconsult.repository.ResetSenhaTokenRepository;
import com.puc.realconsult.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID; //Gera tokens únicos para cada usuário

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class EsqueceuSenhaController {
    private final UserRepository userRepository;
    private final ResetSenhaTokenRepository tokenRepository;
    private final EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EsqueceuSenhaRequest request) {
        UserModel usuario = userRepository.findByEmail(request.getEmail());

        if (usuario == null) {
            return ResponseEntity.badRequest().body("E-mail não encontrado.");
        }

        // Gera um token unico
        String token = UUID.randomUUID().toString();

        // Cria um token valido por 15 min
        ResetSenhaToken resetToken = new ResetSenhaToken(token, usuario);
        tokenRepository.save(resetToken);

        // Monta o link para o email
        String link = "http://localhost:3000/redefinicao-senha?token=" + token;

        // Envia o email
        emailService.send(
                usuario.getEmail(),
                "Redefinição de Senha - REAL Consult",
                "Olá " + usuario.getNome() + ",\n\n" +
                        "Clique no link abaixo para redefinir sua senha:\n" +
                        link + "\n\n" +
                        "Este link expira em 15 minutos."
        );

        return ResponseEntity.ok("E-mail de redefinição enviado com sucesso.");
    }
}