package com.example.order.api.adapter.messaging;

import com.example.order.core.domain.event.OrderCreatedEvent;
import com.example.order.core.domain.port.OrderEventPublisher;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqsOrderEventPublisher implements OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SqsOrderEventPublisher.class);

    private final SqsClient sqsClient;
    private final JsonMapper objectMapper;

    public SqsOrderEventPublisher(SqsClient sqsClient,
                                  JsonMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(OrderCreatedEvent event) {
        try {
            String queueUrl = sqsClient.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName("order-created")
                            .build()
            ).queueUrl();

            sqsClient.sendMessage(
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(objectMapper.writeValueAsString(event))
                            .build()
            );

            log.info("Published OrderCreatedEvent for {}", event.orderId());

        } catch (Exception e) {
            throw new RuntimeException("Failed to publish order event", e);
        }
    }
}
