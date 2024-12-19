package com.example.device_service.service;

import com.example.device_service.entity.OutboxEvent;
import com.example.device_service.entity.OutboxStatus;
import com.example.device_service.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxProcessor(OutboxEventRepository outboxEventRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 5000) // Runs every 5 seconds
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatus(OutboxStatus.PENDING);

        for (OutboxEvent event : pendingEvents) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getPayload());
                event.setStatus(OutboxStatus.PROCESSED);
                outboxEventRepository.save(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
