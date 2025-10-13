package com.puc.realconsult.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import javax.annotation.processing.Generated;

@Entity
@Table(name = "cliente")
@Data
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "uf_default", length = 20)
    private String ufDefault;

    @Column(name = "base_cliente", length = 100)
    private String baseCliente;

    @Column(name = "base_mapa_cliente", length = 40)
    private String baseMapaCliente;

    @NotBlank(message = "nome deve ser preenchido")
    @Column(name = "nome_empresa", length = 30)
    private String nomeEmpresa;

    @NotBlank(message = "Cnpj deve ser preenchido")
    @Column(name = "cnpj", length = 20, unique = true)
    private String cnpj;

    @Column(name = "path", nullable = false, length = 100)
    private String path;

    @Column(name = "numero_consultas", nullable = false)
    private Integer numeroConsultas = 0;

    @Column(name = "numero_consultas_realizadas", nullable = false)
    private Integer numeroConsultasRealizadas = 0;

    @Column(name = "semVT")
    private Boolean semVT;

    @Column(name = "tipoCartao", length = 50, nullable = false)
    private String tipoCartao = "Cartão Empresarial (PJ)";

    @Column(name = "tipoRoteirizacao", length = 10, nullable = false)
    private String tipoRoteirizacao = "Tp";

    @Column(name = "perfil_consulta", length = 10, nullable = false)
    private String perfilConsulta = "pre pago";

    // =========================
    // Construtores
    // =========================

    public ClienteModel() {
    }

    public ClienteModel(String ufDefault, String baseCliente, String baseMapaCliente, String nomeEmpresa,
                   String cnpj, String path, Boolean semVT, String tipoCartao,
                   String tipoRoteirizacao, String perfilConsulta) {
        this.ufDefault = ufDefault;
        this.baseCliente = baseCliente;
        this.baseMapaCliente = baseMapaCliente;
        this.nomeEmpresa = nomeEmpresa;
        this.cnpj = cnpj;
        this.path = path;
        this.semVT = semVT;
        this.tipoCartao = tipoCartao;
        this.tipoRoteirizacao = tipoRoteirizacao;
        this.perfilConsulta = perfilConsulta;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nomeEmpresa='" + nomeEmpresa + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", tipoCartao='" + tipoCartao + '\'' +
                ", perfilConsulta='" + perfilConsulta + '\'' +
                '}';
    }
}

