package com.example.order.worker.adapter.messaging;

import com.example.order.core.domain.event.OrderCreatedEvent;
import com.example.order.core.domain.port.OrderEventPublisher;

public class NoOpOrderEventPublisher implements OrderEventPublisher {

    @Override
    public void publish(OrderCreatedEvent event) {
        // intentionally empty
    }
}

