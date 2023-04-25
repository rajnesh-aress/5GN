package com.cmdb.integration.controller;

import com.cmdb.integration.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cmdb-fresh")
public class CMDBFreshIntegrationController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private AssetServiceImpl assetService;

    @PostMapping(value = "/sync-product")
    @Operation(summary = "Sync all Products")
    public void syncProducts() throws IOException {
        productService.syncProducts();
    }

    @PostMapping(value = "/assets")
    @Operation(summary = "Create Asset in Fresh Service")
    public void createAssets() throws IOException {
        assetService.createAsset();
    }
}
