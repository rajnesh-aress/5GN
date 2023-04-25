package com.cmdb.integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface ProductService {
    public void syncProducts() throws JsonProcessingException;

    public long createProduct(String name, long assetTypeId) throws IOException;
}
