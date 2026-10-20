package com.logistics.shipment_service.event;

import org.junit.jupiter.api.Test;

import com.logistics.shipment_service.event.ShipmentCdcEvent;
import com.logistics.shipment_service.event.ShipmentCdcEventMapper;
import com.logistics.shipment_service.event.ShipmentCreatedEvent;
import com.logistics.shipment_service.event.ShipmentUpdatedEvent;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentCdcEventMapperTest {

    private final ShipmentCdcEventMapper mapper = new ShipmentCdcEventMapper();

    @Test
    void shouldMapCreateOperationToShipmentCreatedEvent() {
        String debeziumJson = """
                {
                  "payload": {
                    "before": null,
                    "after": {
                      "id": 1,
                      "tracking_number": "SHPA30F2460",
                      "status": "IN_TRANSIT"
                    },
                    "op": "c"
                  }
                }
                """;

        ShipmentCdcEvent event = mapper.map(debeziumJson);

        assertThat(event).isInstanceOf(ShipmentCreatedEvent.class);
        ShipmentCreatedEvent createdEvent = (ShipmentCreatedEvent) event;
        assertThat(createdEvent.id()).isEqualTo(1L);
        assertThat(createdEvent.trackingNumber()).isEqualTo("SHPA30F2460");
        assertThat(createdEvent.status()).isEqualTo("IN_TRANSIT");
    }

    @Test
    void shouldMapUpdateOperationToShipmentUpdatedEvent() {
        String debeziumJson = """
                {
                  "payload": {
                    "before": {
                      "id": 1,
                      "tracking_number": "SHPA30F2460",
                      "status": "IN_TRANSIT"
                    },
                    "after": {
                      "id": 1,
                      "tracking_number": "SHPA30F2460",
                      "status": "DELIVERED"
                    },
                    "op": "u"
                  }
                }
                """;

        ShipmentCdcEvent event = mapper.map(debeziumJson);

        assertThat(event).isInstanceOf(ShipmentUpdatedEvent.class);
        ShipmentUpdatedEvent updatedEvent = (ShipmentUpdatedEvent) event;
        assertThat(updatedEvent.id()).isEqualTo(1L);
        assertThat(updatedEvent.trackingNumber()).isEqualTo("SHPA30F2460");
        assertThat(updatedEvent.status()).isEqualTo("DELIVERED");
    }
}