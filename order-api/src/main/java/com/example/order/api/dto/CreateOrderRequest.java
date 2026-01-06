package com.example.order.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CreateOrderRequest(
        @NotBlank String customerId,
        @NotNull Map<String, Object> payload
) {
}
