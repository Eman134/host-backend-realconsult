package com.puc.realconsult.utils;

public class CnpjUtils {

    public static boolean validarCnpj(String cnpj) {
        // remove caracteres não numéricos
        cnpj = cnpj.replaceAll("\\D", "");

        // CNPJ precisa ter 14 dígitos
        if (cnpj.length() != 14) return false;

        // rejeita sequências repetidas (tipo 11111111111111)
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            int[] pesoPrimeiro = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesoSegundo  = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            // calcula primeiro dígito verificador
            int digito1 = calcularDigito(cnpj.substring(0, 12), pesoPrimeiro);

            // calcula segundo dígito verificador
            int digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoSegundo);

            // compara com os dígitos informados
            return cnpj.equals(cnpj.substring(0, 12) + digito1 + digito2);

        } catch (Exception e) {
            return false;
        }
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int i = str.length() - 1, digito; i >= 0; i--) {
            digito = Character.getNumericValue(str.charAt(i));
            soma += digito * peso[peso.length - str.length() + i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    public static String limparCnpj(String cnpj) {
        return cnpj.replaceAll("[^0-9]", "");
    }

}
