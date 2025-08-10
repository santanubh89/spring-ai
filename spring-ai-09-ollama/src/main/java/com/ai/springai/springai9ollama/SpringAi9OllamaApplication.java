package com.ai.springai.springai9ollama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WeatherConfigProperties.class)
public class SpringAi9OllamaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAi9OllamaApplication.class, args);
    }

}
