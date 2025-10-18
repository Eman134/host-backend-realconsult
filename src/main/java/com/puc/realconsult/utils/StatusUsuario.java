package com.puc.realconsult.utils;

public enum StatusUsuario {
    ATIVO("Ativo"),
    INATIVO("Inativo");

    private final String descricao;

    StatusUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
