package com.example.order.core.domain.port;

import com.example.order.core.domain.event.OrderCreatedEvent;

public interface OrderEventPublisher {
    void publish(OrderCreatedEvent event);
}
