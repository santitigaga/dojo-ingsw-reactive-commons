package com.bancolombia.dojo.reactivecommons.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Getter
public class Constants {
    @Value("${spring.person.name}")
    private String nodeName;
    @Value("${spring.application.name}")
    private String appName;
}
