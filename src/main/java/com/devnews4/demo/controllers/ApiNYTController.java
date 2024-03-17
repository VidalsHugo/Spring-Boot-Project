package com.devnews4.demo.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.get("results");

            // Extraindo apenas os 20 primeiros itens
            StringBuilder limitedResponse = new StringBuilder("{\"results\":[");
            int limit = Math.min(1, resultsNode.size());
            for (int i = 0; i < limit; i++) {
                limitedResponse.append(resultsNode.get(i).toString());
                if (i < limit - 1) {
                    limitedResponse.append(",");
                }
            }
            limitedResponse.append("]}");

            return limitedResponse.toString();
        } catch (Exception e) {
            return "Erro ao processar a solicitação";
        }
    }
}

