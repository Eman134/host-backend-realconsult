package com.puc.realconsult.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<UserModel> listarTodosUsuarios() {
        return repository.findAll();
    }
    
    public List<UserModel> buscarUsuarios(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodosUsuarios();
        }
        return repository.buscarPorTermo(termo.trim());
    }
    
    public Optional<UserModel> buscarUsuarioPorId(Long id) {
        return repository.findById(id);
    }
    
    public UserModel criarUsuario(UserModel usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já está em uso: " + usuario.getEmail());
        }

        usuario.setAvatarColor(gerarCorAvatar(usuario.getNome()));
        
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode("123456"));
        }
        
        return repository.save(usuario);
    }
    
    public UserModel atualizarUsuario(Long id, UserModel usuarioAtualizado) {
        UserModel usuarioExistente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        
        if (repository.existsByEmailAndIdNot(usuarioAtualizado.getEmail(), id)) {
            throw new RuntimeException("Email já está em uso: " + usuarioAtualizado.getEmail());
        }
        
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setCargo(usuarioAtualizado.getCargo());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setStatus(usuarioAtualizado.getStatus());
        
        return repository.save(usuarioExistente);
    }
    
    public void excluirUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
    
    public List<UserModel> listarUsuariosPorStatus(UserModel.StatusUsuario status) {
        return repository.findByStatus(status);
    }

    private String gerarCorAvatar(String nome) {
        int hash = nome.hashCode();

        int r = Math.abs(hash % 128);
        int g = Math.abs((hash >> 8) % 128);
        int b = Math.abs((hash >> 16) % 128);

        r = Math.max(r, 50);
        g = Math.max(g, 50);
        b = Math.max(b, 50);

        return String.format("#%02x%02x%02x", r, g, b);
    }

}