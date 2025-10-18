package com.puc.realconsult.config;

import com.puc.realconsult.utils.StatusUsuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem usuários
        if (repository.count() == 0) {
            // Criar usuários de exemplo com novos cargos
            UserModel usuario1 = new UserModel();
            usuario1.setNome("Teste Administrador");
            usuario1.setCargo("Administrador");
            usuario1.setEmail("adm@vtreal.com");
            usuario1.setStatus(StatusUsuario.ATIVO);
            usuario1.setAvatarColor("#4016cc");
            usuario1.setSenha(passwordEncoder.encode("123456"));
            
            UserModel usuario2 = new UserModel();
            usuario2.setNome("Teste Gerente");
            usuario2.setCargo("Gerente");
            usuario2.setEmail("gerente@vtreal.com");
            usuario2.setStatus(StatusUsuario.ATIVO);
            usuario2.setAvatarColor("#84cc16");
            usuario2.setSenha(passwordEncoder.encode("123456"));

            UserModel usuario3 = new UserModel();
            usuario3.setNome("Teste Analista");
            usuario3.setCargo("Analista");
            usuario3.setEmail("analista@vtreal.com");
            usuario3.setStatus(StatusUsuario.ATIVO);
            usuario3.setAvatarColor("#8916cc");
            usuario3.setSenha(passwordEncoder.encode("123456"));

            repository.save(usuario1);
            repository.save(usuario2);
            repository.save(usuario3);
        }
    }
}