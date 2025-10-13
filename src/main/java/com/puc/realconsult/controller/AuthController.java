package com.puc.realconsult.controller;

import com.puc.realconsult.dto.AutenticarDTO;
import com.puc.realconsult.dto.loginUsuarioDTO;
import com.puc.realconsult.repository.UserRepository;
import com.puc.realconsult.model.UserModel;
import com.puc.realconsult.service.UserService;
import com.puc.realconsult.config.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService usuarioService;

    @PostMapping("/login")
    public ResponseEntity loginUsuario(@RequestBody @Valid AutenticarDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var usuario = (UserModel) auth.getPrincipal();

        var token = tokenService.gerarToken((UserModel) auth.getPrincipal());

        System.out.println(usuario);

        return ResponseEntity.ok(new loginUsuarioDTO(token, usuario.getId(), usuario.getNome(), usuario.getAvatarColor()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok().body("Logout bem-sucedido");
    }

}
