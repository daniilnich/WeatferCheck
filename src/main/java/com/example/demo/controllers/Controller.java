package com.example.demo.controllers;

import com.example.demo.entity.CityApiResponse;
import com.example.demo.entity.WeatherApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private final String apiKey = "25945754093d33223b7a7b21e5189f1c";

    @GetMapping
    public String start(){
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model){
        System.out.println(city);
        RestTemplate restTemplate = new RestTemplate();
        String urlCity = "https://api.openweathermap.org/geo/1.0/direct?q=" + city + ",643&limit=1&appid=" + apiKey;
        double lat = 0;
        double lon = 0;


        ResponseEntity<List<CityApiResponse>> responseEntity = restTemplate.exchange(
                urlCity,
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<CityApiResponse>>() {}
        );

        List<CityApiResponse> cityList = responseEntity.getBody();

        if (cityList != null && !cityList.isEmpty()) {
            CityApiResponse cityApi = cityList.get(0);
            lat = cityApi.lat;
            lon = cityApi.lon;
        } else {
            System.out.println("Город не найден.");
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + apiKey;

        ResponseEntity<WeatherApiResponse> responseWeather = restTemplate.getForEntity(url, WeatherApiResponse.class);

        String weather = responseWeather.getBody().weather.get(0).main;
        double temp = responseWeather.getBody().main.temp;
        double feelsLike = responseWeather.getBody().main.feels_like;
        double wind = responseWeather.getBody().wind.speed;
        double pressure = responseWeather.getBody().main.pressure * 0.75;
        double visibility = responseWeather.getBody().visibility / 1000;

        model.addAttribute("weather111111", weather);
        model.addAttribute("temp", temp);
        model.addAttribute("feels_like", feelsLike);
        model.addAttribute("wind", wind);
        model.addAttribute("pressure", pressure);
        model.addAttribute("visibility", visibility);

        return "weather";
    }
}
