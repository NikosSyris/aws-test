package com.example.order.worker.config;

import com.example.order.core.application.OrderProcessor;
import com.example.order.core.application.OrderService;
import com.example.order.core.domain.port.OrderEventPublisher;
import com.example.order.core.domain.port.OrderRepository;
import com.example.order.core.domain.port.PayloadStore;
import com.example.order.worker.adapter.messaging.NoOpOrderEventPublisher;
import com.example.order.worker.adapter.persistence.FileOrderRepository;
import com.example.order.worker.adapter.persistence.NoOpPayloadStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class WorkerConfig {

    @Bean
    public OrderRepository orderRepository() {
        return new FileOrderRepository();
    }

    @Bean
    public PayloadStore payloadStore() {
        return new NoOpPayloadStore();
    }

    @Bean
    public OrderEventPublisher orderEventPublisher() {
        return new NoOpOrderEventPublisher();
    }

    @Bean
    public OrderService orderService(
            OrderRepository orderRepository,
            PayloadStore payloadStore,
            OrderEventPublisher orderEventPublisher
    ) {
        return new OrderService(orderRepository, payloadStore, orderEventPublisher);
    }

    @Bean
    public OrderProcessor orderProcessor(OrderRepository orderRepository) {
        return new OrderProcessor(orderRepository);
    }
}
