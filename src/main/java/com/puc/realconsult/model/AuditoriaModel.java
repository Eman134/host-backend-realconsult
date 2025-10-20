package com.puc.realconsult.model;

import com.puc.realconsult.model.ClienteModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.annotation.processing.Generated;

@Entity
@Table(name ="auditoria")
@Data
public class AuditoriaModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "cliente_id", referencedColumnName ="id_cliente")
    private ClienteModel cliente;

    @NotBlank
    @Column(name = "nome")
    private String nome;
}