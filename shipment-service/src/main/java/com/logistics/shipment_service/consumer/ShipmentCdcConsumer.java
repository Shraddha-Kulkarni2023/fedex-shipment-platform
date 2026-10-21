package com.logistics.shipment_service.consumer;

import com.logistics.shipment_service.domain.ShipmentCdcEventHandler;
import com.logistics.shipment_service.event.ShipmentCdcEvent;
import com.logistics.shipment_service.event.ShipmentCdcEventMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ShipmentCdcConsumer {

    private final ShipmentCdcEventMapper mapper;
    private final ShipmentCdcEventHandler handler;

    public ShipmentCdcConsumer(ShipmentCdcEventMapper mapper, ShipmentCdcEventHandler handler) {
        this.mapper = mapper;
        this.handler = handler;
    }

    @KafkaListener(topics = "ui-service.public.shipments", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        ShipmentCdcEvent event = mapper.map(message);
        handler.handle(event);
    }
}