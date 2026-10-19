package com.logistics.shipment_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

@Component
public class ShipmentCdcConsumer {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final List<String> receivedMessages = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "ui-service.public.shipments", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        receivedMessages.add(message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }
}