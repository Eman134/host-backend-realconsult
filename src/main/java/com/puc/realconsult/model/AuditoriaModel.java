package com.puc.realconsult.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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

    @Column(name = "mes_referencia")
    private Integer mesReferencia; // 1-12 (Janeiro a Dezembro), nullable para compatibilidade

    @Column(name = "ano_referencia")
    private Integer anoReferencia; // 2024, 2025, etc, nullable para compatibilidade
}