package com.example.order.worker.job;

import com.example.order.core.application.OrderProcessor;
import com.example.order.core.domain.model.OrderStatus;
import com.example.order.core.domain.port.OrderRepository;
import jakarta.annotation.PostConstruct;

public class OrderProcessingJob {

    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor;

    public OrderProcessingJob(OrderRepository orderRepository, OrderProcessor orderProcessor) {
        this.orderRepository = orderRepository;
        this.orderProcessor = orderProcessor;
    }

    /**
     * Start background processing when the application starts
     */
    @PostConstruct
    public void start() {
        new Thread(this::loop).start();
    }

    private void loop() {
        while (true) {
            // Find all received orders and process them
            orderRepository.findByStatus(OrderStatus.RECEIVED)
                    .forEach(orderProcessor::process);

            try {
                Thread.sleep(1000); // poll every second
            } catch (InterruptedException ignored) {}
        }
    }
}
