package com.logistics.shipment_service.domain;

import com.logistics.shipment_service.event.ShipmentCreatedEvent;

import com.logistics.shipment_service.domain.Shipment;
import com.logistics.shipment_service.domain.ShipmentCdcEventHandler;
import com.logistics.shipment_service.domain.ShipmentRepository;
import com.logistics.shipment_service.event.ShipmentUpdatedEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShipmentCdcEventHandlerTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Test
    void shouldSaveNewShipmentWhenHandlingCreatedEvent() {
        ShipmentCdcEventHandler handler = new ShipmentCdcEventHandler(shipmentRepository);
        ShipmentCreatedEvent event = new ShipmentCreatedEvent(1L, "SHPA30F2460", "IN_TRANSIT");

        handler.handle(event);

        ArgumentCaptor<Shipment> captor = ArgumentCaptor.forClass(Shipment.class);
        verify(shipmentRepository).save(captor.capture());

        Shipment savedShipment = captor.getValue();
        assertThat(savedShipment.getId()).isEqualTo(1L);
        assertThat(savedShipment.getTrackingNumber()).isEqualTo("SHPA30F2460");
        assertThat(savedShipment.getStatus()).isEqualTo("IN_TRANSIT");
    }

        @org.junit.jupiter.api.Test
    void shouldUpdateExistingShipmentWhenHandlingUpdatedEvent() {
        ShipmentCdcEventHandler handler = new ShipmentCdcEventHandler(shipmentRepository);

        Shipment existingShipment = new Shipment(1L, "SHPA30F2460", "IN_TRANSIT");
        org.mockito.Mockito.when(shipmentRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existingShipment));

        ShipmentUpdatedEvent event = new ShipmentUpdatedEvent(1L, "SHPA30F2460", "DELIVERED");

        handler.handle(event);

        ArgumentCaptor<Shipment> captor = ArgumentCaptor.forClass(Shipment.class);
        verify(shipmentRepository).save(captor.capture());

        Shipment savedShipment = captor.getValue();
        assertThat(savedShipment.getId()).isEqualTo(1L);
        assertThat(savedShipment.getStatus()).isEqualTo("DELIVERED");
    }
}