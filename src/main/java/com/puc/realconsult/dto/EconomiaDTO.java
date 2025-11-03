package com.puc.realconsult.dto;

import java.util.List;

public record EconomiaDTO(
    Long clienteId,
    String clienteNome,
    Long auditoriaId,
    String auditoriaNome,
    Double economiaPrevista,
    Double economiaReal,
    Double economiaPrevistaMensal,
    Double economiaRealMensal,
    Double diferenca,
    Double totalAcumulado,
    List<FuncionarioEconomiaDTO> funcionarios,
    Integer mesReferencia, // 1-12, nullable
    Integer anoReferencia // nullable
) {}

