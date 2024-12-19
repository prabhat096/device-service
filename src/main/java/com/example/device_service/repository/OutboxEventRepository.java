package com.example.device_service.repository;

import com.example.device_service.entity.OutboxEvent;
import com.example.device_service.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByStatus(OutboxStatus status);
}
