package com.example.order.api.adapter.messaging;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

@Component
public class SqsInitializer {

    private final SqsClient sqsClient;
    public static final String QUEUE_NAME = "order-created";

    public SqsInitializer(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        retryCreateQueue();
    }

    private void retryCreateQueue() {
        int attempts = 0;
        while (attempts < 10) {
            try {
                sqsClient.createQueue(CreateQueueRequest.builder()
                        .queueName(QUEUE_NAME)
                        .build());
                return;
            } catch (Exception e) {
                attempts++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
            }
        }
        throw new IllegalStateException("Could not initialize SQS after retries");
    }
}


