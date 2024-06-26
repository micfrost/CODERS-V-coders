package com.dci.a3m.service;

import com.dci.a3m.util.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate, @Value("${weather.api.key}") String apiKey){
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }


    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public String getWeather(String city) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();
        try{
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            return response != null ? response.getMain().getTemp() + "°C " + response.getWeather().get(0).getDescription() : "No weather data available";
        }catch (Exception e){
            return "No weather data available";
        }
    }
}
