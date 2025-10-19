package com.puc.realconsult.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.puc.realconsult.utils.StatusUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotBlank(message = "Cargo é obrigatório")
    @Column(name = "cargo", nullable = false)
    private String cargo;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusUsuario status;

    @Column(name = "avatar_color")
    private String avatarColor;

    @Column(name = "senha")
    private String senha;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if ("Administrador".equals(this.cargo)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
            // Administrador tem acesso total
            authorities.add(new SimpleGrantedAuthority("ROLE_GERENTE"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ANALISTA"));
        } else if ("Gerente".equals(this.cargo)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_GERENTE"));
            // Gerente tem acesso total
            authorities.add(new SimpleGrantedAuthority("ROLE_ANALISTA"));
        } else if ("Analista".equals(this.cargo)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ANALISTA"));
        }
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.status == StatusUsuario.ATIVO;
    }

    @Override
    public String toString() {
        return "";
    }
}