package com.example.order.worker.adapter.messaging;

import com.example.order.core.application.OrderProcessor;
import com.example.order.core.domain.event.OrderCreatedEvent;
import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.port.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import tools.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final SqsClient sqsClient;
    private final JsonMapper objectMapper;
    private final OrderProcessor orderProcessor;
    private final OrderRepository orderRepository;

    public OrderCreatedListener(SqsClient sqsClient,
                                JsonMapper objectMapper,
                                OrderProcessor orderProcessor,
                                OrderRepository orderRepository) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.orderProcessor = orderProcessor;
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedDelay = 1000) // poll every 1 second
    public void poll() {
        try {
            String queueUrl = sqsClient.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName("order-created")
                            .build()
            ).queueUrl();

            ReceiveMessageResponse response =
                    sqsClient.receiveMessage(
                            ReceiveMessageRequest.builder()
                                    .queueUrl(queueUrl)
                                    .waitTimeSeconds(10)
                                    .maxNumberOfMessages(5)
                                    .build()
                    );

            for (Message msg : response.messages()) {
                try {
                    // Convert JSON -> Event
                    OrderCreatedEvent event =
                            objectMapper.readValue(msg.body(), OrderCreatedEvent.class);

                    log.info("Received OrderCreatedEvent for {}", event.orderId());

                    // Lookup the order from repository
                    Optional<Order> optionalOrder = orderRepository.findById(event.orderId());

                    if (optionalOrder.isPresent()) {
                        orderProcessor.process(optionalOrder.get());
                    } else {
                        log.warn("Order not found: {}", event.orderId());
                    }

                    // Delete message after successful processing
                    sqsClient.deleteMessage(
                            DeleteMessageRequest.builder()
                                    .queueUrl(queueUrl)
                                    .receiptHandle(msg.receiptHandle())
                                    .build()
                    );
                } catch (Exception e) {
                    // Do NOT delete message on failure
                    log.error("Failed to process SQS message", e);
                }
            }
        } catch (Exception e) {
            log.error("SQS polling failed", e);
        }
    }
}
