package com.puc.realconsult.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security")
public class HttpsConfig {

    private boolean https;
    private boolean crossSite;

    public boolean isHttps() {
        return https;
    }

    public boolean isCrossSite() {
        return crossSite;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public void setCrossSite(boolean crossSite) {
        this.crossSite = crossSite;
    }
}

