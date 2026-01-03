package com.example.demo.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    @Tool(description = "Get weather details for a specific latitude/longitude")
    public String getWeatherInformationByLocation(double longitude, double latitude) {
        // A custom logic can be replaced here with a call to a weather API service.
        return "The weather is expected to be pleasant with some expectation of snow in the evening";
    }
}
