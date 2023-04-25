package com.cmdb.integration.service;

import com.cmdb.integration.model.Device;

import java.io.IOException;
import java.util.List;

public interface DeviceService {

    public List<Device> getDevicesBySiteId(String siteId) throws IOException;
}
