package com.namil.autotrading.service;

import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.dto.OrderResponse;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.exception.NotFoundException;
import com.namil.autotrading.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order(
                request.getMarket(),
                request.getSide(),
                request.getAmount(),
                OrderStatus.READY
        );

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getMarket(),
                savedOrder.getSide(),
                savedOrder.getAmount(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt()
        );
    }

    public List<OrderResponse> getOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order ->new OrderResponse(
                        order.getId(),
                        order.getMarket(),
                        order.getSide(),
                        order.getAmount(),
                        order.getStatus(),
                        order.getCreatedAt()
                )).toList();
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("주문 없음"));
    }

    public OrderResponse markAsOrdered(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("주문 없음"));

        order.changeStatus(OrderStatus.ORDERED);

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getMarket(),
                savedOrder.getSide(),
                savedOrder.getAmount(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt()
        );
    }
}
