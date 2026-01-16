package com.example.order.core.domain.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerId;
    private OrderStatus status;
    private Instant createdAt;
    private String payloadLocation;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPayloadLocation() {
        return payloadLocation;
    }

    public void setPayloadLocation(String payloadLocation) {
        this.payloadLocation = payloadLocation;
    }
}
