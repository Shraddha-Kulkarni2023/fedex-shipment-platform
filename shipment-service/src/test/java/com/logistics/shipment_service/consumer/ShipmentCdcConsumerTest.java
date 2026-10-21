package com.logistics.shipment_service.consumer;

import com.logistics.shipment_service.domain.ShipmentCdcEventHandler;
import com.logistics.shipment_service.event.ShipmentCdcEvent;
import com.logistics.shipment_service.event.ShipmentCdcEventMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentCdcConsumerTest {

    @Mock
    private ShipmentCdcEventMapper mapper;

    @Mock
    private ShipmentCdcEventHandler handler;

    @Test
    void shouldMapAndHandleIncomingMessage() {
        ShipmentCdcConsumer consumer = new ShipmentCdcConsumer(mapper, handler);
        String rawMessage = "some-raw-debezium-json";
        ShipmentCdcEvent mappedEvent = new com.logistics.shipment_service.event.ShipmentCreatedEvent(1L, "SHPA30F2460", "IN_TRANSIT");

        when(mapper.map(rawMessage)).thenReturn(mappedEvent);

        consumer.consume(rawMessage);

        verify(mapper).map(rawMessage);
        verify(handler).handle(mappedEvent);
    }
}