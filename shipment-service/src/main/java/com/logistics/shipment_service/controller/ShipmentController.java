package com.logistics.shipment_service.controller;

import com.logistics.shipment_service.domain.ShipmentRepository;
import com.logistics.shipment_service.dto.ShipmentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;

    public ShipmentController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping("/api/shipments")
    public List<ShipmentResponse> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(ShipmentResponse::from)
                .toList();
    }
}