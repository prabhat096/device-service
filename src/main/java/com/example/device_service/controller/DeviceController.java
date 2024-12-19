package com.example.device_service.controller;

import com.example.device_service.entity.Device;
import com.example.device_service.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.createDevice(device));
    }

    @PutMapping("/{serialNumber}")
    public ResponseEntity<Device> updateDevice(@PathVariable String serialNumber, @RequestBody Device updates) {
        return ResponseEntity.ok(deviceService.updateDevice(serialNumber, updates));
    }

    @DeleteMapping("/{serialNumber}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String serialNumber) {
        deviceService.deleteDevice(serialNumber);
        return ResponseEntity.noContent().build();
    }
}
