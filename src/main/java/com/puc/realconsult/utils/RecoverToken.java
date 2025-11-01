package com.puc.realconsult.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class RecoverToken {
    public static String recoverToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) return auth.substring(7);
        return null;
    }
}
