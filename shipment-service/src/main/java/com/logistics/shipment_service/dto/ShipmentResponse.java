package com.logistics.shipment_service.dto;

import com.logistics.shipment_service.domain.Shipment;

public record ShipmentResponse(Long id, String trackingNumber, String status) {

    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(shipment.getId(), shipment.getTrackingNumber(), shipment.getStatus());
    }
}