package com.example.device_service.entity;

public class DeviceUpdateEvent {
    private String serialNumber;
    private String ip;

    public DeviceUpdateEvent(String serialNumber, String ip) {
        this.serialNumber = serialNumber;
        this.ip = ip;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
