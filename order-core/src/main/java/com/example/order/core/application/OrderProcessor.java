package com.example.order.core.application;

import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.model.OrderStatus;
import com.example.order.core.domain.port.OrderRepository;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OrderProcessor {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessor.class);

    private final OrderRepository orderRepository;

    public OrderProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void process(Order order) {
        try {
            log.info("Processing order {}", order.getOrderId());

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
