package com.logistics.shipment_service.event;
//import main.java.com.logistics.shipment_service.event.ShipmentCdcEvent;

public record ShipmentCreatedEvent(
        Long id,
        String trackingNumber,
        String status
) implements ShipmentCdcEvent {
}