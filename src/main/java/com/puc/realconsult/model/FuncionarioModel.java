package com.puc.realconsult.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "funcionario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_funcionario_auditoria_matricula_mes_ano", 
                columnNames = {"auditoria_id", "matricula", "mes_referencia", "ano_referencia"})
})
@Data
public class FuncionarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funcionario_id")
    private Long funcionarioId;

    @Column(name = "matricula")
    private Long matricula;

    @ManyToOne
    @JoinColumn(name = "auditoria_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AuditoriaModel auditoria;

    @NotBlank
    @Column(name = "nome_funcionario")
    private String nomeFuncionario;

    @NotBlank
    @Column(name = "unidade")
    private String unidade;

    @Column(name = "situacao")
    private String situacao;

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

    @Column(name = "operadora_ida")
    private String operadoraIda;

    @Column(name = "operadora_volta")
    private String operadoraVolta;

    @Column(name = "mes_referencia")
    private Integer mesReferencia; // 1-12 (Janeiro a Dezembro), nullable para compatibilidade

    @Column(name = "ano_referencia")
    private Integer anoReferencia; // 2024, 2025, etc, nullable para compatibilidade
}
