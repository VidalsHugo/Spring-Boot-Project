package com.devnews4.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api-nyt")
public class ApiNYTController {
    @Value("${NYT_API_KEY}")
    private String nytKey;
    @GetMapping("/top-stories/technology")
    public String getTechnology() {
        try {
            String apiUrl = "https://api.nytimes.com/svc/topstories/v2/technology.json?api-key=" + nytKey;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(apiUrl, String.class);
        } catch (Exception e) {
            return "Erro ao processar a solicitação";
        }
    }

}
