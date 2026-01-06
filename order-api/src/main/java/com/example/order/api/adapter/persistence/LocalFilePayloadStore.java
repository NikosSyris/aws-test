package com.example.order.api.adapter.persistence;

import com.example.order.core.domain.port.PayloadStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFilePayloadStore implements PayloadStore {

    private final Path storageDir = Path.of("payloads");

    public LocalFilePayloadStore() throws IOException {
        Files.createDirectories(storageDir);
    }

    @Override
    public String store(String orderId, byte[] payload) {
        Path file = storageDir.resolve(orderId + ".dat");
        try {
            Files.write(file, payload);
            return file.toString(); // reference returned
        } catch (IOException e) {
            throw new RuntimeException("Failed to store payload", e);
        }
    }
}
