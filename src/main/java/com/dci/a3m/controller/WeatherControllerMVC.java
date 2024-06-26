package com.dci.a3m.controller;

import com.dci.a3m.entity.Member;
import com.dci.a3m.service.MemberService;
import com.dci.a3m.service.WeatherService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherControllerMVC {

    private final WeatherService weatherService;
    private final MemberService memberService;

    public WeatherControllerMVC(WeatherService weatherService, MemberService memberService) {
        this.weatherService = weatherService;
        this.memberService = memberService;
    }

    @GetMapping("/weather")
    public String getWeather(Model model) {
        Member authenticatedMember = memberService.getAuthenticatedMember();
        if (authenticatedMember != null && authenticatedMember.getCity() != null) {
            String weather = weatherService.getWeather(authenticatedMember.getCity());
            model.addAttribute("weather", weather);
        }
        else {
            model.addAttribute("weather", "No weather data available");
        }
        return "weather";
    }
}
