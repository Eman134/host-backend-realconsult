package com.puc.realconsult.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public final class RecoverToken {
    public static String recoverToken(HttpServletRequest request) {
        // 1) Cookie "token"
        if (request.getCookies() != null) {
            for (var c : request.getCookies()) {
                if ("token".equals(c.getName()) && c.getValue()!=null && !c.getValue().isBlank()) {
                    return c.getValue();
                }
            }
        }
        // 2) Header Cookie (fallback para SSR/proxies)
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            for (String p : cookieHeader.split(";")) {
                var kv = p.trim().split("=", 2);
                if (kv.length == 2 && "token".equals(kv[0])) return kv[1];
            }
        }
        // 3) Bearer (se você também aceita Authorization)
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) return auth.substring(7);

        return null;
    }
}
