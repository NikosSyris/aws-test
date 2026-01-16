package com.example.order.core.application;

import com.example.order.core.domain.event.OrderCreatedEvent;
import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.model.OrderStatus;
import com.example.order.core.domain.port.OrderEventPublisher;
import com.example.order.core.domain.port.OrderRepository;
import com.example.order.core.domain.port.PayloadStore;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final PayloadStore payloadStore;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository,
                        PayloadStore payloadStore,
                        OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.payloadStore = payloadStore;
        this.eventPublisher = eventPublisher;
    }

    public Order createOrder(String customerId, byte[] payload) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.RECEIVED);
        order.setCreatedAt(Instant.now());

        String payloadLocation = payloadStore.store(order.getOrderId(), payload);
        order.setPayloadLocation(payloadLocation);

        orderRepository.save(order);

        log.info("Order created: {}", order.getOrderId());

        eventPublisher.publish(
                new OrderCreatedEvent(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getPayloadLocation()
                )
        );

        return order;
    }
}
