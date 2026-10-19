package com.logistics.shipment_service.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = { "ui-service.public.shipments" },
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
class ShipmentCdcConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ShipmentCdcConsumer shipmentCdcConsumer;

    @Test
    void shouldReceiveRawDebeziumMessageFromShipmentsTopic() throws InterruptedException {
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

        kafkaTemplate.send("ui-service.public.shipments", debeziumJson);

        boolean messageReceived = shipmentCdcConsumer.getLatch().await(10, TimeUnit.SECONDS);

        assertThat(messageReceived).isTrue();
        assertThat(shipmentCdcConsumer.getReceivedMessages()).hasSize(1);
        assertThat(shipmentCdcConsumer.getReceivedMessages().get(0)).contains("SHPA30F2460");
    }
}
