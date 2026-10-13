package com.logistics.ui_service_simulator.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
        @NotBlank String customerId,
        @NotBlank String customerEmail,
        @NotBlank String originAddress,
        @NotBlank String destinationAddress,
        @NotBlank String serviceType
) {}
