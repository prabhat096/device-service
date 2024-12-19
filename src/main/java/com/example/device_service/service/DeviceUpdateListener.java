package com.example.device_service.service;

import com.example.device_service.entity.DeviceUpdateEvent;
import com.example.device_service.repository.DeviceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceUpdateListener {

    private final DeviceRepository deviceRepository;
    private final ObjectMapper objectMapper;

    public DeviceUpdateListener(DeviceRepository deviceRepository, ObjectMapper objectMapper) {
        this.deviceRepository = deviceRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "device-update-topic", groupId = "device-service-group")
    @Transactional
    public void handleUpdateEvent(ConsumerRecord<String, String> record) {
        try {
            // Parse the JSON payload
            String payload = record.value();
            DeviceUpdateEvent updateEvent = objectMapper.readValue(payload, DeviceUpdateEvent.class);

            // Update the IP in the database
            deviceRepository.findBySerialNumber(updateEvent.getSerialNumber()).ifPresentOrElse(device -> {
                device.setIp(updateEvent.getIp());
                deviceRepository.save(device);
                System.out.println("Updated IP for device with serial number: " + updateEvent.getSerialNumber());
            }, () -> {
                System.err.println("Device not found for serial number: " + updateEvent.getSerialNumber());
            });

        } catch (Exception e) {
            System.err.println("Error processing UPDATE event: " + e.getMessage());
        }
    }
}