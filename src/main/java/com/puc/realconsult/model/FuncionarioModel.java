package com.puc.realconsult.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;


@Entity
@Table(name ="funcionario")
@Data

public class FuncionarioModel{

    @Id
    @Column(name = "matricula")
    private Long matricula;

    @NotBlank
    @Column(name = "nome_funcionario")
    private String nomeFuncionario;

    @NotBlank
    private String unidade;

    @Column(name = "situacao")
    private String situacao;

    @ManyToOne
    @JoinColumn(name= "auditoria_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Nullable
    private AuditoriaModel auditoria;

    @Column(name = "custo_atual")
    private Double custoAtual;

    @Column(name = "custo_proposto")
    private Double custoProposto;


    @Column(name = "economia_valor")
    private Double economiaValor;


    @Column(name = "economia_percentual")
    private Double economiaPercentual;

    @NotBlank
    @Column(name = "tipo_dia")
    private String tipoDia;


    @Column(name = "dias_mes")
    private Long diasMes;


    @Column(name = "valor_diario")
    private Double valorDiario;


    @Column(name = "custo_implantado")
    private Double valorCustoImplantado;


    @Column(name = "economia_implantada")
    private Double valorEconomiaImplantada;

    @Column(name = "linhaum_ida")
    private Double tarifaLinhaUmIda;

    @Column(name = "linhadois_ida")
    private Double tarifaLinhaDoisIda;

    @Column(name = "linhatres_ida")
    private Double tarifaLinhaTresIda;

    @Column(name = "linhaquatro_ida")
    private Double tarifaLinhaQuatroIda;

    @Column(name = "linhaum_volta")
    private Double tarifaLinhaUmVolta;

    @Column(name = "linhadois_volta")
    private Double tarifaLinhaDoisVolta;

    @Column(name = "linhatres_volta")
    private Double tarifaLinhaTresVolta;

    @Column(name = "linhaquatro_volta")
    private Double tarifaLinhaQuatroVolta;

    @Column(name = "operadora_transporte")
    private String operadoraTransporte;

}