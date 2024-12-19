package com.example.device_service.controller;


import com.example.device_service.entity.Device;
import com.example.device_service.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DeviceControllerTests {

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private DeviceController deviceController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(deviceController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateDevice() throws Exception {
        Device device = new Device();
        device.setSerialNumber("12345");
        device.setIp("192.168.1.1");
        device.setType("IPV4");
        device.setMac("00:1A:2B:3C:4D:5E");
        device.setModel("ModelX");
        device.setManufacturer("ManufacturerY");

        when(deviceService.createDevice(any(Device.class))).thenReturn(device);

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("12345"))
                .andExpect(jsonPath("$.ip").value("192.168.1.1"))
                .andExpect(jsonPath("$.type").value("IPV4"))
                .andExpect(jsonPath("$.mac").value("00:1A:2B:3C:4D:5E"));

        verify(deviceService, times(1)).createDevice(any(Device.class));
    }

    @Test
    void testUpdateDevice() throws Exception {
        Device updatedDevice = new Device();
        updatedDevice.setSerialNumber("12345");
        updatedDevice.setIp("192.168.1.2");

        when(deviceService.updateDevice(eq("12345"), any(Device.class))).thenReturn(updatedDevice);

        mockMvc.perform(put("/devices/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDevice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("12345"))
                .andExpect(jsonPath("$.ip").value("192.168.1.2"));

        verify(deviceService, times(1)).updateDevice(eq("12345"), any(Device.class));
    }

    @Test
    void testDeleteDevice() throws Exception {
        doNothing().when(deviceService).deleteDevice("12345");

        mockMvc.perform(delete("/devices/12345"))
                .andExpect(status().isNoContent());

        verify(deviceService, times(1)).deleteDevice("12345");
    }
}