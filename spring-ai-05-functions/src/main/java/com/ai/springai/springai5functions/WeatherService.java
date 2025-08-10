package com.ai.springai.springai5functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

@Service
public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    public static final Logger log = LoggerFactory.getLogger(SpringAi5FunctionsApplication.class);

    private final WeatherConfigProperties weatherConfig;

    private final RestClient restClient;

    public WeatherService(WeatherConfigProperties weatherConfigProperties) {
        this.weatherConfig = weatherConfigProperties;
        this.restClient = RestClient.create(weatherConfigProperties.apiUrl());
    }

    @Override
    public Response apply(Request request) {
        log.info("Weather Request: {}", request);
        Response response = restClient.get()
                .uri("/current.json?key={key}&q={q}", weatherConfig.apiKey(), request.city())
                .retrieve().body(Response.class);
        log.info("Weather Response: {}", response);
        return response;
    }

    public record Request(String city) {}

    public record Response(Location location, Current current) {}

    public record Location(String name, String region, String country, long lat, long lon) {}

    public record Current(String temp_f, Condition condition, String wind_mph, String humidity) {}

    public record Condition(String text) {}
}
