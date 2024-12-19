package com.example.device_service.service;

import com.example.device_service.entity.Device;
import com.example.device_service.entity.OutboxEvent;
import com.example.device_service.repository.DeviceRepository;
import com.example.device_service.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final OutboxEventRepository outboxEventRepository;

    public DeviceService(DeviceRepository deviceRepository, OutboxEventRepository outboxEventRepository) {
        this.deviceRepository = deviceRepository;
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    public Device createDevice(Device device) {
        Device savedDevice = deviceRepository.save(device);

        OutboxEvent event = new OutboxEvent();
        event.setTopic("device-events");
        event.setPayload("CREATE:" + savedDevice.getSerialNumber());
        outboxEventRepository.save(event);

        return savedDevice;
    }

    @Transactional
    public Device updateDevice(String serialNumber, Device updates) {
        Device existingDevice = deviceRepository.findById(serialNumber)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (updates.getIp() != null) existingDevice.setIp(updates.getIp());
        if (updates.getType() != null) existingDevice.setType(updates.getType());
        if (updates.getMac() != null) existingDevice.setMac(updates.getMac());
        if (updates.getModel() != null) existingDevice.setModel(updates.getModel());
        if (updates.getManufacturer() != null) existingDevice.setManufacturer(updates.getManufacturer());

        Device updatedDevice = deviceRepository.save(existingDevice);

        OutboxEvent event = new OutboxEvent();
        event.setTopic("device-events");
        event.setPayload("UPDATE:" + updatedDevice.getSerialNumber());
        outboxEventRepository.save(event);

        return updatedDevice;
    }

    @Transactional
    public void deleteDevice(String serialNumber) {
        if (!deviceRepository.existsById(serialNumber)) {
            throw new RuntimeException("Device not found");
        }

        deviceRepository.deleteById(serialNumber);

        OutboxEvent event = new OutboxEvent();
        event.setTopic("device-events");
        event.setPayload("DELETE:" + serialNumber);
        outboxEventRepository.save(event);
    }
}

