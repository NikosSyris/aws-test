package com.example.order.api.adapter.messaging;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

@Component
public class SqsInitializer {

    public static final String QUEUE_NAME = "order-created";

    public SqsInitializer(SqsClient sqsClient) {
        sqsClient.createQueue(
                CreateQueueRequest.builder()
                        .queueName(QUEUE_NAME)
                        .build()
        );
    }
}

