package com.puc.realconsult.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Data

public class FuncionarioModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matricula")
    private Long matricula;

    @NotBlank
    @Column(name = "nome_funcionario")
    private String nomeFuncionario;

    @NotBlank
    private String unidade;

    @NotBlank
    private String situacao;

    @ManyToOne
    @JoinColumn(name= "auditoria_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AuditoriaModel auditoria;

    @Column(name = "custo_atual")
    private float custoAtual;

    @Column(name = "custo_proposto")
    private float custoProposto;


    @Column(name = "economia_valor")
    private float economiaValor;


    @Column(name = "economia_percentual")
    private float economiaPercentual;

    @NotBlank
    @Column(name = "tipo_dia")
    private String tipoDia;


    @Column(name = "dias_mes")
    private Long diasMes;


    @Column(name = "valor_diario")
    private float valorDiario;


    @Column(name = "custo_implantado")
    private float valorCustoImplantado;


    @Column(name = "economia_implantada")
    private float valorEconomiaImplantada;

    @Column(name = "linhaum_ida")
    private float tarifaLinhaUmIda;

    @Column(name = "linhadois_ida")
    private float tarifaLinhaDoisIda;

    @Column(name = "linhatres_ida")
    private float tarifaLinhaTresIda;

    @Column(name = "linhaquatro_ida")
    private float tarifaLinhaQuatroIda;

    @Column(name = "linhaum_volta")
    private float tarifaLinhaUmVolta;

    @Column(name = "linhadois_volta")
    private float tarifaLinhaDoisVolta;

    @Column(name = "linhatres_volta")
    private float tarifaLinhaTresVolta;

    @Column(name = "linhaquatro_volta")
    private float tarifaLinhaQuatroVolta;

    @Column(name = "operadora_transporte")
    private String operadoraTransporte;

}