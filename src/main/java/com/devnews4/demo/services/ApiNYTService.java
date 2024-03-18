package com.devnews4.demo.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiNYTService {

    @Value("${NYT_API_KEY}")
    private String nytKey;

    public String getTopStories(String categoria){
        try {
            String apiUrl = "https://api.nytimes.com/svc/topstories/v2/"+categoria+".json?api-key="+nytKey;
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
