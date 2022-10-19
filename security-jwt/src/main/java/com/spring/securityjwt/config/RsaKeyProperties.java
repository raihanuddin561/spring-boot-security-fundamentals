package com.spring.securityjwt.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
@Data
public class RsaKeyProperties {
        public RSAPublicKey publicKey;
        public RSAPrivateKey privateKey;
}
