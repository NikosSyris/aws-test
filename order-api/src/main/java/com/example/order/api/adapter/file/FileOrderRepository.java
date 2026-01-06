package com.example.order.api.adapter.file;

import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.model.OrderStatus;
import com.example.order.core.domain.port.OrderRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileOrderRepository implements OrderRepository {

    private final Map<String, Order> cache = new ConcurrentHashMap<>();
    private final Path storageDir = Path.of("orders");

    public FileOrderRepository() throws IOException {
        Files.createDirectories(storageDir);
        loadExistingOrders();
    }

    private void loadExistingOrders() throws IOException {
        // Load persisted orders at startup (optional)
        if (!Files.exists(storageDir)) return;

        Files.list(storageDir).forEach(file -> {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
                Order order = (Order) ois.readObject();
                cache.put(order.getOrderId(), order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void save(Order order) {
        cache.put(order.getOrderId(), order);
        Path file = storageDir.resolve(order.getOrderId() + ".dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
            oos.writeObject(order);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return cache.values().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findById(String id) {
        // First check in-memory cache
        Order order = cache.get(id);
        if (order != null) return Optional.of(order);

        // Attempt to load from file if missing
        Path file = storageDir.resolve(id + ".dat");
        if (!Files.exists(file)) return Optional.empty();

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            order = (Order) ois.readObject();
            cache.put(id, order); // cache it for future
            return Optional.of(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
