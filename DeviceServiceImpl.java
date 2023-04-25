package com.cmdb.integration.service;

import com.cmdb.integration.model.Device;
import com.cmdb.integration.model.DeviceType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private AccessToken accessToken;

    @Override
    public List<Device> getDevicesBySiteId(String siteId) throws IOException {
        List<Device> devices = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Device device = new Device();
        int pageNumber = 2;
        /*boolean hasMorePages = true;
        while (hasMorePages) {*/
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken.generateAccessToken());
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                env.getProperty("datto.endpoint") + "/api/v2/site/" + siteId + "/devices?max=10&page=" + pageNumber,
                HttpMethod.GET, entity, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode devicesNode = rootNode.path("devices");
        for (JsonNode deviceNode : devicesNode) {
            DeviceType deviceType = new DeviceType();
            deviceType.setCategory(deviceNode.get("deviceType").get("category").asText());
            deviceType.setType(deviceNode.get("deviceType").get("type").asText());
            device.setId(deviceNode.get("id").asLong());
            device.setUid(deviceNode.get("uid").asText());
            device.setSiteId(deviceNode.get("siteId").asText());
            device.setDeviceType(deviceType);
            device.setHostname(deviceNode.get("hostname").asText());
            device.setIntIpAddress(deviceNode.get("intIpAddress").asText());
            device.setOperatingSystem(deviceNode.get("operatingSystem").asText());
            device.setLastLoggedInUser(deviceNode.get("lastLoggedInUser").asText());
            devices.add(device);
        }
          /*  if (rootNode.path("pageDetails").path("count").asInt()==0) {
                hasMorePages = false;
            } else {
                pageNumber++;
            }
        }*/
        return devices;
    }
}