package com.example.order.worker.adapter.persistence;

import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.model.OrderStatus;
import com.example.order.core.domain.port.OrderRepository;

import java.io.*;
import java.util.*;

public class FileOrderRepository implements OrderRepository {

    private final File storageDir = new File("orders");

    public FileOrderRepository() {
        storageDir.mkdirs();
    }

    @Override
    public void save(Order order) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File(storageDir, order.getOrderId() + ".dat"))
        )) {
            oos.writeObject(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> findById(String id) {
        File file = new File(storageDir, id + ".dat");
        if (!file.exists()) return Optional.empty();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return Optional.of((Order) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        File[] files = storageDir.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files == null) return Collections.emptyList();
        List<Order> orders = new ArrayList<>();
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Order order = (Order) ois.readObject();
                if (order.getStatus() == status) orders.add(order);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return orders;
    }
}
