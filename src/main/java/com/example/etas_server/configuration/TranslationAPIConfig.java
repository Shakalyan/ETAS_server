package com.example.etas_server.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="tapi")
public class TranslationAPIConfig {

    private String domain;
    private String APIHost;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAPIHost() {
        return APIHost;
    }

    public void setAPIHost(String APIHost) {
        this.APIHost = APIHost;
    }
}
