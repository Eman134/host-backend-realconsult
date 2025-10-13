package com.puc.realconsult.config;

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
            // Criar usuários de exemplo baseados na imagem
            UserModel usuario1 = new UserModel();
            usuario1.setNome("Sérgio Sermenho");
            usuario1.setCargo("Administrador");
            usuario1.setEmail("sergio@vtreal.com");
            usuario1.setStatus(UserModel.StatusUsuario.ATIVO);
            usuario1.setAvatarColor("#22c55e"); // Verde
            usuario1.setSenha(passwordEncoder.encode("123456"));
            
            UserModel usuario2 = new UserModel();
            usuario2.setNome("Ramon Lacerda");
            usuario2.setCargo("Administrador");
            usuario2.setEmail("ramon@vtreal.com");
            usuario2.setStatus(UserModel.StatusUsuario.INATIVO);
            usuario2.setAvatarColor("#84cc16"); // Verde claro
            usuario2.setSenha(passwordEncoder.encode("123456"));

            UserModel usuario3 = new UserModel();
            usuario3.setNome("Julia Ventura");
            usuario3.setCargo("Administrador");
            usuario3.setEmail("julia@vtreal.com");
            usuario3.setStatus(UserModel.StatusUsuario.ATIVO);
            usuario3.setAvatarColor("#cc1656");
            usuario3.setSenha(passwordEncoder.encode("123456"));

            UserModel usuario4 = new UserModel();
            usuario4.setNome("Kayke Emanoel");
            usuario4.setCargo("Administrador");
            usuario4.setEmail("kayke@vtreal.com");
            usuario4.setStatus(UserModel.StatusUsuario.ATIVO);
            usuario4.setAvatarColor("#4016cc");
            usuario4.setSenha(passwordEncoder.encode("123456"));
            
            UserModel usuario5 = new UserModel();
            usuario5.setNome("Ana Luiza");
            usuario5.setCargo("Administrador");
            usuario5.setEmail("ana@vtreal.com");
            usuario5.setStatus(UserModel.StatusUsuario.ATIVO);
            usuario5.setAvatarColor("#8916cc");
            usuario5.setSenha(passwordEncoder.encode("123456"));

            repository.save(usuario1);
            repository.save(usuario2);
            repository.save(usuario3);
            repository.save(usuario4);
            repository.save(usuario5);
        }
    }
}