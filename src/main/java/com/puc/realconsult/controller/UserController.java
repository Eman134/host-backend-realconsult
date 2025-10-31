package com.puc.realconsult.controller;

import java.util.List;
import java.util.Map;

import com.puc.realconsult.service.NotificationService;
import com.puc.realconsult.utils.StatusUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService service;
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<UserModel>> listarUsuarios(
            @RequestParam(required = false) String busca) {
        try {
            List<UserModel> usuarios;
            if (busca != null && !busca.trim().isEmpty()) {
                usuarios = service.buscarUsuarios(busca);
            } else {
                usuarios = service.listarTodosUsuarios();
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            return service.buscarUsuarioPorId(id)
                    .map(usuario -> ResponseEntity.ok(usuario))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody UserModel usuario) {
        try {
            UserModel usuarioCriado = service.criarUsuario(usuario);
            String[] cargos = {"Gerente", "Administrador"};
            notificationService.enviarNotificacaoParaCargos(cargos, "Usuário " + usuarioCriado.getNome() + " cadastrado!");
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UserModel usuario) {
        try {
            UserModel usuarioAtualizado = service.atualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        try {
            service.excluirUsuario(id);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário excluído com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor"));
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserModel>> listarUsuariosPorStatus(
            @PathVariable StatusUsuario status) {
        try {
            List<UserModel> usuarios = service.listarUsuariosPorStatus(status);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}