package com.cmdb.integration.service;

import com.cmdb.integration.model.Audit;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuditDetailsService {

    public Audit getAuditDetailsByDeviceId(String deviceId) throws JsonProcessingException;
}
