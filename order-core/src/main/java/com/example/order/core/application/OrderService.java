package com.example.order.core.application;

import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.model.OrderStatus;
import com.example.order.core.domain.port.OrderRepository;
import com.example.order.core.domain.port.PayloadStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PayloadStore payloadStore;

    public OrderService(
            OrderRepository orderRepository,
            PayloadStore payloadStore
    ) {
        this.orderRepository = orderRepository;
        this.payloadStore = payloadStore;
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
        return order;
    }
}
