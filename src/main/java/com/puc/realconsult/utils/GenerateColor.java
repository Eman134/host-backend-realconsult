package com.puc.realconsult.utils;

public class GenerateColor {
    public static String generate(String nome) {
        int hash = nome.hashCode();

        int r = Math.abs(hash % 128);
        int g = Math.abs((hash >> 8) % 128);
        int b = Math.abs((hash >> 16) % 128);

        r = Math.max(r, 50);
        g = Math.max(g, 50);
        b = Math.max(b, 50);

        return String.format("#%02x%02x%02x", r, g, b);
    }
}
