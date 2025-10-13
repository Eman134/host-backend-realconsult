package com.puc.realconsult.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "despesas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespesaModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    @Column(nullable = false)
    private String titulo;
    
    @NotBlank(message = "Cliente/Contrato é obrigatório")
    @Size(max = 100, message = "Cliente/Contrato deve ter no máximo 100 caracteres")
    @Column(name = "cliente_contrato", nullable = false)
    private String clienteContrato;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    @Column(nullable = false)
    private String categoria;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDespesa status = StatusDespesa.ATIVA;
    
    public enum StatusDespesa {
        ATIVA, INATIVA, PAGA, PENDENTE
    }
}
