package com.ai.springai.springai5functions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WeatherConfigProperties.class)
public class SpringAi5FunctionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAi5FunctionsApplication.class, args);
    }

}
