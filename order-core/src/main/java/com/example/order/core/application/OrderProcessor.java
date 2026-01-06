package com.example.order.core.application;

import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.model.OrderStatus;
import com.example.order.core.domain.port.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor {

    private final OrderRepository orderRepository;

    public OrderProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void process(Order order) {
        try {
            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);

            // Simulate business work
            Thread.sleep(500);

            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);

        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
        }
    }
}
