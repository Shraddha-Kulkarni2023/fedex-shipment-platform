package com.logistics.shipment_service.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShipmentEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void shouldPublishCreatedEventKeyedByShipmentId() {
        ShipmentEventPublisher publisher = new ShipmentEventPublisher(kafkaTemplate);
        ShipmentCreatedEvent event = new ShipmentCreatedEvent(1L, "SHPA30F2460", "IN_TRANSIT");

        publisher.publish(event);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(org.mockito.ArgumentMatchers.eq("shipment-events"), keyCaptor.capture(), payloadCaptor.capture());

        assertThat(keyCaptor.getValue()).isEqualTo("1");
        assertThat(payloadCaptor.getValue()).contains("SHPA30F2460");
        assertThat(payloadCaptor.getValue()).contains("IN_TRANSIT");
    }
}