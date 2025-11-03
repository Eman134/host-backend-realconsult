package com.puc.realconsult.dto;

public record FuncionarioEconomiaDTO(
    Long funcionarioId,
    String nomeFuncionario,
    Double custoAtual,
    Double custoProposto,
    Double custoImplantado
) {}

