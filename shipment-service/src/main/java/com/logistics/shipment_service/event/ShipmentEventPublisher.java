package com.logistics.shipment_service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class ShipmentEventPublisher {

    private static final String TOPIC = "shipment-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public ShipmentEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ShipmentCdcEvent event) {
        String key = String.valueOf(event.id());
        String payload = jsonMapper.writeValueAsString(event);
        kafkaTemplate.send(TOPIC, key, payload);
    }
}
