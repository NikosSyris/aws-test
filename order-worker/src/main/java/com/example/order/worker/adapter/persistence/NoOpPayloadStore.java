package com.example.order.worker.adapter.persistence;

import com.example.order.core.domain.port.PayloadStore;

public class NoOpPayloadStore implements PayloadStore {

    @Override
    public String store(String orderId, byte[] payload) {
        // Worker never stores payloads
        return null;
    }
}
