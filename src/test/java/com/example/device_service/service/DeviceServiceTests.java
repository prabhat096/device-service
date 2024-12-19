package com.example.device_service.service;
import com.example.device_service.entity.Device;
import com.example.device_service.entity.OutboxEvent;
import com.example.device_service.repository.DeviceRepository;
import com.example.device_service.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DeviceServiceTests {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @InjectMocks
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDevice() {
        Device device = new Device();
        device.setSerialNumber("123");
        device.setIp("192.168.1.1");

        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        Device result = deviceService.createDevice(device);

        assertNotNull(result);
        assertEquals("123", result.getSerialNumber());
        verify(outboxEventRepository, times(1)).save(any(OutboxEvent.class));
    }

    @Test
    void testUpdateDevice() {
        Device existingDevice = new Device();
        existingDevice.setSerialNumber("123");
        existingDevice.setIp("192.168.1.1");

        Device updates = new Device();
        updates.setIp("192.168.1.2");

        when(deviceRepository.findById("123")).thenReturn(java.util.Optional.of(existingDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(existingDevice);

        Device result = deviceService.updateDevice("123", updates);

        assertNotNull(result);
        assertEquals("192.168.1.2", result.getIp());
        verify(outboxEventRepository, times(1)).save(any(OutboxEvent.class));
    }
}

