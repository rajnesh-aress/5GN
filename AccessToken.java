package com.cmdb.integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class AccessToken {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    public String generateAccessToken() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("public-client", "public");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String result = restTemplate.exchange(env.getProperty("datto.endpoint") + "/auth/oauth/token?username=" + env.getProperty("datto.username") + "&password=" + env.getProperty("datto.password") + "&grant_type=" + env.getProperty("datto.grantType"), HttpMethod.POST, entity, String.class).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        return jsonNode.get("access_token").asText();
    }
}
