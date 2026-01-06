package com.example.order.api.controller;

import com.example.order.api.dto.CreateOrderRequest;
import com.example.order.api.dto.CreateOrderResponse;
import com.example.order.core.application.OrderService;
import com.example.order.core.domain.model.Order;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final JsonMapper objectMapper;

    public OrderController(OrderService orderService, JsonMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public CreateOrderResponse createOrder(
            @RequestBody @Valid CreateOrderRequest request
    ) throws Exception {

        // convert DTO payload to bytes
        byte[] payloadBytes = objectMapper.writeValueAsBytes(request.payload());

        // create order via service
        Order order = orderService.createOrder(request.customerId(), payloadBytes);

        // return agreed response
        return new CreateOrderResponse(
                order.getOrderId(),
                order.getStatus()
        );
    }
}
