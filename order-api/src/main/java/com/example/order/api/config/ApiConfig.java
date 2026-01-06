package com.example.order.api.config;

import com.example.order.api.adapter.file.FileOrderRepository;
import com.example.order.api.adapter.persistence.LocalFilePayloadStore;
import com.example.order.core.application.OrderService;
import com.example.order.core.domain.port.OrderRepository;
import com.example.order.core.domain.port.PayloadStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ApiConfig {

    @Bean
    public OrderRepository orderRepository() throws IOException {
        return new FileOrderRepository();
    }

    @Bean
    public PayloadStore payloadStore() throws IOException {
        return new LocalFilePayloadStore();
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository,
                                     PayloadStore payloadStore) {
        return new OrderService(orderRepository, payloadStore);
    }
}

