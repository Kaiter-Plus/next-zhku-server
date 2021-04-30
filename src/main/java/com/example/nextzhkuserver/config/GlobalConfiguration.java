package com.example.nextzhkuserver.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("com.example")
@Getter
@Setter
public class GlobalConfiguration {

    private String jwtKey;
}
