package com.namil.autotrading.service;

import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderSide;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderRequest request) {

        Order order = new Order(
                request.getMarket(),
                request.getSide(),
                request.getAmount(),
                OrderStatus.READY
        );

        return orderRepository.save(order);
    }
}
