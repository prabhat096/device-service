package com.example.device_service.service;
import com.example.device_service.entity.OutboxEvent;
import com.example.device_service.entity.OutboxStatus;
import com.example.device_service.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;

import static org.mockito.Mockito.*;

class OutboxProcessorTests {

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private OutboxProcessor outboxProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessOutboxEvents() {
        OutboxEvent event = new OutboxEvent();
        event.setId(1L);
        event.setTopic("device-events");
        event.setPayload("CREATE:123");

        when(outboxEventRepository.findByStatus(OutboxStatus.PENDING)).thenReturn(Collections.singletonList(event));

        outboxProcessor.processOutboxEvents();

        verify(kafkaTemplate, times(1)).send("device-events", "CREATE:123");
        verify(outboxEventRepository, times(1)).save(any(OutboxEvent.class));
    }
}

