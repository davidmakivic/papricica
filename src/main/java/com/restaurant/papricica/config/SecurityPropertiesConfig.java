package com.restaurant.papricica.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityPropertiesConfig {

    @Bean
    @ConfigurationProperties(prefix = "security.auth")
    protected Auth auth() {
        return new Auth();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.jwt")
    protected Jwt jwt() {
        return new Jwt();
    }

    public static class Auth{
        @Getter
        @Setter
        private String header;

        @Getter
        @Setter
        private String prefix;

        @Getter
        @Setter
        private String loginUri;
    }

    public static class Jwt{
        @Getter
        @Setter
        private String secret;

        @Getter
        @Setter
        private String type;

        @Getter
        @Setter
        private String issuer;

        @Getter
        @Setter
        private String audience;

        @Getter
        @Setter
        private Long expirationTime;
    }
}
