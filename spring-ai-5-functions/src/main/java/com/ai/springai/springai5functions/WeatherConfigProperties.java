package com.ai.springai.springai5functions;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather")
public record WeatherConfigProperties(String apiKey, String apiUrl) {
}
