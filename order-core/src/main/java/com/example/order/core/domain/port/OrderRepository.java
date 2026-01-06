package com.example.order.core.domain.port;

import com.example.order.core.domain.model.Order;
import com.example.order.core.domain.model.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(String orderId);

    List<Order> findByStatus(OrderStatus status);

}
