package com.puc.realconsult.model;

import com.puc.realconsult.utils.StatusNotificacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel usuario;

    @NotBlank(message = "Mensagem é obrigatória")
    @Column(name = "mensagem", nullable = false)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusNotificacao status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

}
