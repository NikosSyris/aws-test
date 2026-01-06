package com.example.order.core.domain.port;

public interface PayloadStore {

    /**
     * Stores the payload and returns its location reference.
     */
    String store(String orderId, byte[] payload);
}
