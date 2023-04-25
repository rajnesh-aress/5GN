package com.cmdb.integration.model;

public class SystemInfo {

    private String model;
    private long totalCpuCores;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getTotalCpuCores() {
        return totalCpuCores;
    }

    public void setTotalCpuCores(long totalCpuCores) {
        this.totalCpuCores = totalCpuCores;
    }
}
