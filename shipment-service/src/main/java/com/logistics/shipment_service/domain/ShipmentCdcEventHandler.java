package com.logistics.shipment_service.domain;

import com.logistics.shipment_service.event.ShipmentCdcEvent;
import com.logistics.shipment_service.event.ShipmentCreatedEvent;
import com.logistics.shipment_service.event.ShipmentUpdatedEvent;
import org.springframework.stereotype.Component;

@Component
public class ShipmentCdcEventHandler {

    private final ShipmentRepository shipmentRepository;

    public ShipmentCdcEventHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public void handle(ShipmentCdcEvent event) {
        switch (event) {
            case ShipmentCreatedEvent created -> {
                Shipment shipment = new Shipment(created.id(), created.trackingNumber(), created.status());
                shipmentRepository.save(shipment);
            }
            case ShipmentUpdatedEvent updated -> {
                Shipment existingShipment = shipmentRepository.findById(updated.id())
                        .orElseThrow(() -> new IllegalStateException(
                                "Cannot update shipment that does not exist: " + updated.id()));
                existingShipment.setTrackingNumber(updated.trackingNumber());
                existingShipment.setStatus(updated.status());
                shipmentRepository.save(existingShipment);
            }
        }
    }
}