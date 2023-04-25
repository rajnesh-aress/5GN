package com.cmdb.integration.model;

public class Audit {
    private String portalUrl;
    private SystemInfo systemInfo;
    private Bios bios;
    private PhysicalMemory physicalMemory;

    public String getPortalUrl() {
        return portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public Bios getBios() {
        return bios;
    }

    public void setBios(Bios bios) {
        this.bios = bios;
    }

    public PhysicalMemory getPhysicalMemory() {
        return physicalMemory;
    }

    public void setPhysicalMemory(PhysicalMemory physicalMemory) {
        this.physicalMemory = physicalMemory;
    }
}
