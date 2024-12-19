package com.example.device_service.repository;
import com.example.device_service.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {
    Optional<Device> findBySerialNumber(String serialNumber);

}
