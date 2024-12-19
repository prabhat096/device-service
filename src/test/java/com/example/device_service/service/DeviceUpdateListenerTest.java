package com.example.device_service.service;

import com.example.device_service.entity.Device;
import com.example.device_service.entity.DeviceUpdateEvent;
import com.example.device_service.repository.DeviceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DeviceUpdateListenerTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceUpdateListener deviceUpdateListener;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Create a test device in the database
        Device device = new Device();
        device.setSerialNumber("12345");
        device.setIp("192.168.1.1");
        device.setMac("00:11:22:33:44:55");
        device.setModel("TestModel");
        device.setManufacturer("TestManufacturer");
        deviceRepository.save(device);
    }

    @Test
    void testHandleUpdateEvent() throws Exception {
        // Prepare test data
        String serialNumber = "12345";
        String newIp = "192.168.1.100";
        String payload = objectMapper.writeValueAsString(new DeviceUpdateEvent(serialNumber, newIp));

        // Simulate a Kafka message
        ConsumerRecord<String, String> record = new ConsumerRecord<>("device-update-topic", 0, 0, serialNumber, payload);
        deviceUpdateListener.handleUpdateEvent(record);

        // Verify the database was updated
        Optional<Device> updatedDevice = deviceRepository.findBySerialNumber(serialNumber);
        assertThat(updatedDevice).isPresent();
        assertThat(updatedDevice.get().getIp()).isEqualTo(newIp);
    }
}
