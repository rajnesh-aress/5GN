package com.cmdb.integration.service;

import com.cmdb.integration.model.*;
import com.cmdb.integration.repository.AssetRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {

    private static final String HARDWARE_ASSET_ID = "hardware.asset.id";
    private static final String COMPUTER_ASSET_ID = "computer.asset.id";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private DeviceServiceImpl deviceService;

    @Autowired
    private AuditDetailsServiceImpl auditDetailsService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public void createAsset() throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> assetList = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        Asset assetData = new Asset();
        headers.set("Authorization", "Basic SFVkRTNXMm5YSkZCbVRHQ1d5Ng==");
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        List<Company> companies = companyService.getAllCompanies();
        for (Company company : companies) {
            List<Device> devices = deviceService.getDevicesBySiteId(company.getSiteId());
            for (Device device : devices) {
                Optional<Asset> existingAsset = assetRepository.findByDeviceId(device.getUid());
                if (existingAsset.isPresent()) {
                    continue;
                }
                Audit auditDetails = auditDetailsService.getAuditDetailsByDeviceId(device.getUid());
                long assetTypeId = getAssetTypeId(device.getDeviceType().getCategory());
                Long productId = productService.createProduct(auditDetails.getSystemInfo().getModel(), assetTypeId);
                assetList.put("product_" + env.getProperty(HARDWARE_ASSET_ID), productId);
                assetList.put("serial_number_" + env.getProperty(HARDWARE_ASSET_ID), auditDetails.getBios().getSerialNumber());
                assetList.put("cpu_core_count_" + env.getProperty(COMPUTER_ASSET_ID), auditDetails.getSystemInfo().getTotalCpuCores());
                assetList.put("os_" + env.getProperty(COMPUTER_ASSET_ID), device.getOperatingSystem());
                assetList.put("hostname_" + env.getProperty(COMPUTER_ASSET_ID), device.getHostname());
                assetList.put("computer_ip_address_" + env.getProperty(COMPUTER_ASSET_ID), device.getIntIpAddress());
                assetList.put("last_login_by_" + env.getProperty(COMPUTER_ASSET_ID), device.getLastLoggedInUser());
                assetList.put("asset_state_" + env.getProperty(HARDWARE_ASSET_ID), "In Use");
                requestBody.put("name", device.getHostname());
                requestBody.put("asset_type_id", assetTypeId);
                requestBody.put("type_fields", assetList);
                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
                ResponseEntity<String> asset = restTemplate.exchange(
                        "https://5gn-dev.freshservice.com/api/v2/assets",
                        HttpMethod.POST, requestEntity, String.class);
                JsonNode rootNode = objectMapper.readTree(asset.getBody());
                JsonNode assetsNode = rootNode.path("asset");
                assetData.setAssetId(assetsNode.get("id").asLong());
                assetData.setDeviceId(device.getUid());
                assetRepository.save(assetData);
            }
        }
    }

    private long getAssetTypeId(String category) {
        if ("Server".equals(category))
            return Long.parseLong(env.getProperty("server.asset.id"));
        else if ("Laptop".equals(category))
            return Long.parseLong(env.getProperty("laptop.asset.id"));
        else if ("Desktop".equals(category))
            return Long.parseLong(env.getProperty("desktop.asset.id"));
        else
            return 0;
    }
}