package com.devnews4.demo.controllers;

import com.devnews4.demo.services.ApiNYTService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api-nyt")
public class ApiNYTController {

    @Autowired
    ApiNYTService apiNYTService;

    @GetMapping("/top-stories/menu-principal")
    public String menuPrincipal(){
        return apiNYTService.getTopStories("home");
    }
    @GetMapping("/top-stories/technology")
    public String menuTechnology() {
        return apiNYTService.getTopStories("technology");
    }
    @GetMapping("/top-stories/arts")
    public String menuArts(){
        return apiNYTService.getTopStories("arts");
    }
    @GetMapping("/top-stories/business")
    public String menuBusiness(){
        return apiNYTService.getTopStories("business");
    }
    @GetMapping("/top-stories/politics")
    public String menuPolitics(){
        return apiNYTService.getTopStories("politics");
    }
    @GetMapping("/top-stories/science")
    public String menuScience(){
        return apiNYTService.getTopStories("science");
    }
    @GetMapping("/top-stories/sports")
    public String menuSports(){
        return apiNYTService.getTopStories("sports");
    }
    @GetMapping("/top-stories/travel")
    public String menuTravel(){
        return apiNYTService.getTopStories("travel");
    }
    @GetMapping("/top-stories/world")
    public String menuworld(){
        return apiNYTService.getTopStories("world");
    }
}