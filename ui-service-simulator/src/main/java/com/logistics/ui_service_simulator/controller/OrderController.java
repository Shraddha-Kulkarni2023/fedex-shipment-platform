package com.logistics.ui_service_simulator.controller;

import com.logistics.ui_service_simulator.dto.CreateOrderRequest;
import com.logistics.ui_service_simulator.dto.UpdateStatusRequest;
import com.logistics.ui_service_simulator.entity.Shipment;
import com.logistics.ui_service_simulator.repository.ShipmentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ShipmentRepository shipmentRepository;

    public OrderController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @PostMapping
    public ResponseEntity<Shipment> placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Shipment shipment = Shipment.builder()
                .trackingNumber(generateTrackingNumber())
                .customerId(request.customerId())
                .customerEmail(request.customerEmail())
                .originAddress(request.originAddress())
                .destinationAddress(request.destinationAddress())
                .serviceType(request.serviceType())
                .status("CREATED")
                .expectedDeliveryDate(now.plusDays(3))
                .createdAt(now)
                .updatedAt(now)
                .build();

        Shipment saved = shipmentRepository.save(shipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{trackingNumber}/status")
    public ResponseEntity<Shipment> updateStatus(
            @PathVariable String trackingNumber,
            @Valid @RequestBody UpdateStatusRequest request) {

        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + trackingNumber));

        shipment.setStatus(request.status());
        shipment.setUpdatedAt(LocalDateTime.now());

        if ("DELIVERED".equalsIgnoreCase(request.status())) {
            shipment.setActualDeliveryDate(LocalDateTime.now());
        }

        return ResponseEntity.ok(shipmentRepository.save(shipment));
    }

    private String generateTrackingNumber() {
        return "SHP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
