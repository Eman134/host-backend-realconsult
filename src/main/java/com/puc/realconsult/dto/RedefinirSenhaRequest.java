package com.puc.realconsult.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedefinirSenhaRequest {
    private String token;
    private String novaSenha;
}
