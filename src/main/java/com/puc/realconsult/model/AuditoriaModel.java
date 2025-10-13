package com.puc.realconsult.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.annotation.processing.Generated;

@Entity
@Data
public class AuditoriaModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "cliente")
    private String cliente;

    @NotBlank
    @Column(name = "nome")
    private String nome;
}