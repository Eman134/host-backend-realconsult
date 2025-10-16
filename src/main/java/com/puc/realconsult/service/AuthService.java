package com.puc.realconsult.service;

import com.puc.realconsult.config.HttpsConfig;
import com.puc.realconsult.config.TokenService;
import com.puc.realconsult.dto.AutenticarDTO;
import com.puc.realconsult.dto.EsqueceuSenhaRequest;
import com.puc.realconsult.dto.RedefinirSenhaRequest;
import com.puc.realconsult.dto.loginUsuarioDTO;
import com.puc.realconsult.model.ResetSenhaToken;
import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.repository.ResetSenhaTokenRepository;
import com.puc.realconsult.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ResetSenhaTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HttpsConfig httpsConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public AuthLoginResult login(AutenticarDTO data, AuthenticationManager authManager) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = authManager.authenticate(usernamePassword);
        var usuario = (UserModel) auth.getPrincipal();

        String token = tokenService.gerarToken(usuario);

        var userDTO = new loginUsuarioDTO(null, usuario.getId(), usuario.getNome(), usuario.getAvatarColor());

        return new AuthLoginResult(token, userDTO);
    }

    public String resetPassword(RedefinirSenhaRequest request) {
        Optional<ResetSenhaToken> tokenOpt = tokenRepository.findByToken(request.getToken());

        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Token inválido.");
        }

        ResetSenhaToken resetToken = tokenOpt.get();

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token expirado.");
        }

        UserModel usuario = resetToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        userRepository.save(usuario);

        tokenRepository.delete(resetToken);

        return "Senha redefinida com sucesso!";
    }

    public void forgotPassword(EsqueceuSenhaRequest request) throws Exception {
        // Verifica se o usuário existe no banco
        UserModel usuario = userRepository.findByEmail(request.getEmail());

        if (usuario == null) {
            throw new Exception("E-mail não encontrado.");
        }

        // Gera um token único
        String token = UUID.randomUUID().toString();

        // Cria um token válido por 15 min
        ResetSenhaToken resetToken = new ResetSenhaToken(token, usuario);
        tokenRepository.save(resetToken);

        // Monta o link para o email
        String link = System.getProperty("spring.web.cors.allowed-origins") + "/redefinicao-senha?token=" + token;

        // Envia o email
        emailService.send(
                usuario.getEmail(),
                "Redefinição de Senha - REAL Consult",
                "Olá " + usuario.getNome() + ",\n\n" +
                        "Clique no link abaixo para redefinir sua senha:\n" +
                        link + "\n\n" +
                        "Este link expira em 15 minutos."
        );
    }

    public record AuthLoginResult(String token, loginUsuarioDTO user) {}

}
