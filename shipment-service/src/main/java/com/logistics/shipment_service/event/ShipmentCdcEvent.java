package com.logistics.shipment_service.event;

public sealed interface ShipmentCdcEvent
        permits ShipmentCreatedEvent, ShipmentUpdatedEvent {
}
