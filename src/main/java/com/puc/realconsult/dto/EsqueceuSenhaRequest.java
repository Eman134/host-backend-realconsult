package com.puc.realconsult.dto;

public class EsqueceuSenhaRequest {
    private String email;

    public EsqueceuSenhaRequest() {}

    public EsqueceuSenhaRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}