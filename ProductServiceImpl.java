package com.cmdb.integration.service;

import com.cmdb.integration.model.Product;
import com.cmdb.integration.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void syncProducts() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = new Product();
        headers.set("Authorization", "Basic SFVkRTNXMm5YSkZCbVRHQ1d5Ng==");
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://5gn-dev.freshservice.com/api/v2/products",
                HttpMethod.GET, entity, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode productsNode = rootNode.path("products");
        for (JsonNode productNode : productsNode) {
            product.setId(productNode.get("id").asLong());
            product.setName(productNode.get("name").asText());
            product.setCreatedAt(LocalDateTime.parse(productNode.get("created_at").asText(), FORMATTER));
            product.setUpdatedAt(LocalDateTime.parse(productNode.get("updated_at").asText(), FORMATTER));
            productRepository.saveOrUpdate(product);
        }
    }

    @Override
    public long createProduct(String name, long assetTypeId) throws IOException {
        long productId;
        Map<String, Object> requestBody = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = new Product();
        headers.set("Authorization", "Basic SFVkRTNXMm5YSkZCbVRHQ1d5Ng==");
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestBody.put("name", name);
        requestBody.put("asset_type_id", assetTypeId);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        Optional<Product> existingProduct = productRepository.findByName(name);
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(name);
            productRepository.save(updatedProduct);
            productId = updatedProduct.getId();
        } else {
            ResponseEntity<String> productData = restTemplate.exchange("https://5gn-dev.freshservice.com/api/v2/products",
                    HttpMethod.POST, requestEntity, String.class);
            JsonNode rootNode = objectMapper.readTree(productData.getBody());
            JsonNode productsNode = rootNode.path("product");
            product.setId(productsNode.get("id").asLong());
            product.setName(productsNode.get("name").asText());
            product.setCreatedAt(LocalDateTime.parse(productsNode.get("created_at").asText(), FORMATTER));
            product.setUpdatedAt(LocalDateTime.parse(productsNode.get("updated_at").asText(), FORMATTER));
            productRepository.save(product);
            productId = product.getId();
        }
        return productId;
    }

}
