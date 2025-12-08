package com.example.demo.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class MyTools {

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Value("${weather.apiKey}")
    private String apiKey;

    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Tool(description = "Get the current weather provided the latitude and longitude")
    public WeatherData getWeather(double latitude, double longitude) {
        System.out.println("Get the current weather provided the latitude and longitude");
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("API Key: " + apiKey);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey);

        return restTemplate.getForObject(builder.toUriString(), WeatherData.class);
    }

    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "Set a user alarm for the given time, provided in ISO-8601 format")
    void setAlarm(String time) {
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("Alarm set for " + alarmTime);
    }
}
