package com.cmdb.integration;

import com.cmdb.integration.service.ProductServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
public class SchedulerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfiguration.class);

    @Autowired
    ProductServiceImpl productService;

    //@GetMapping(value = "/products")
    @Scheduled(cron = "0 */2 * * * *")
    public void syncProducts() throws JsonProcessingException {
        logger.info("Time : {}", LocalDateTime.now());
        productService.syncProducts();
    }
}
