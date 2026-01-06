package com.example.order.api.dto;

import com.example.order.core.domain.model.OrderStatus;

public record CreateOrderResponse(
        String orderId,
        OrderStatus status
) {
}
