package com.baeldung.springai.memory;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;


@Component
public class WeatherTool {

    private final RestTemplate restTemplate = new RestTemplate();

    // MCP tool accessible by LLM
    @Tool
    public String getWeather(String city) {
        try {
            // Step 1: Get coordinates for city (using Open-Meteo geocoding API)
            String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1";
            String geoResponse = restTemplate.getForObject(geoUrl, String.class);

            JSONObject geoJson = new JSONObject(geoResponse);
            if (!geoJson.has("results")) {
                return "Could not find coordinates for " + city;
            }

            JSONObject location = geoJson.getJSONArray("results").getJSONObject(0);
            double latitude = location.getDouble("latitude");
            double longitude = location.getDouble("longitude");

            // Step 2: Fetch current weather
            String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                    + "&longitude=" + longitude
                    + "&current_weather=true";
            String weatherResponse = restTemplate.getForObject(weatherUrl, String.class);

            JSONObject weatherJson = new JSONObject(weatherResponse);
            JSONObject current = weatherJson.getJSONObject("current_weather");

            double temp = current.getDouble("temperature");
            double wind = current.getDouble("windspeed");

            return "Weather in " + city + ": " + temp + "°C, Windspeed " + wind + " km/h.";
        } catch (Exception e) {
            return "Could not fetch weather for " + city + ": " + e.getMessage();
        }
    }
}
