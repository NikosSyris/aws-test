package com.example.order.core.domain.event;

public record OrderCreatedEvent(
        String orderId,
        String customerId,
        String payloadLocation
) {}