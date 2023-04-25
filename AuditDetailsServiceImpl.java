package com.cmdb.integration.service;

import com.cmdb.integration.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuditDetailsServiceImpl implements AuditDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private AccessToken accessToken;

    @Override
    public Audit getAuditDetailsByDeviceId(String deviceId) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        headers.set("Authorization", "Bearer " + accessToken.generateAccessToken());
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                env.getProperty("datto.endpoint") + "/api/v2/audit/device/" + deviceId + "",
                HttpMethod.GET, entity, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setModel(rootNode.get("systemInfo").get("model").asText());
        systemInfo.setTotalCpuCores(rootNode.get("systemInfo").get("totalCpuCores").asLong());
        PhysicalMemory physicalMemory = new PhysicalMemory();
        physicalMemory.setSize(rootNode.get("physicalMemory").get(0).get("size").asLong());
        Bios bios = new Bios();
        bios.setSerialNumber(rootNode.get("bios").get("serialNumber").asText());
        Audit audit = new Audit();
        audit.setSystemInfo(systemInfo);
        audit.setPhysicalMemory(physicalMemory);
        audit.setBios(bios);
        return audit;
    }
}