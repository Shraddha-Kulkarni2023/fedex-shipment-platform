package com.logistics.shipment_service.event;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Component;

@Component
public class ShipmentCdcEventMapper {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public ShipmentCdcEvent map(String debeziumJson) {
        try {
            JsonNode root = jsonMapper.readTree(debeziumJson);
            JsonNode payload = root.get("payload");
            String op = payload.get("op").asString();
            JsonNode after = payload.get("after");

            Long id = after.get("id").asLong();
            String trackingNumber = after.get("tracking_number").asString();
            String status = after.get("status").asString();

            return switch (op) {
                case "c" -> new ShipmentCreatedEvent(id, trackingNumber, status);
                case "u" -> new ShipmentUpdatedEvent(id, trackingNumber, status);
                default -> throw new IllegalArgumentException("Unsupported Debezium operation: " + op);
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Debezium CDC event", e);
        }
    }
}