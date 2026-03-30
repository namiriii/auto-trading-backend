package com.namil.autotrading.service;

import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.dto.OrderResponse;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.exception.NotFoundException;
import com.namil.autotrading.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("주문 없음"));
    }

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

        return OrderResponse.from(savedOrder);

//        return new OrderResponse(
//                savedOrder.getId(),
//                savedOrder.getMarket(),
//                savedOrder.getSide(),
//                savedOrder.getAmount(),
//                savedOrder.getStatus(),
//                savedOrder.getCreatedAt()
//        );
    }

    public List<OrderResponse> getOrders(OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createAt"));

        Page<Order> orders;

        if(status == null) {
            orders = orderRepository.findAll(pageable);
        } else {
            orders = orderRepository.findByStatus(status, pageable);
        }

        return orders.stream()
                .map(OrderResponse::from)
                .toList();
//        return orderRepository.findAll()
//                .stream()
//                .map(order ->new OrderResponse(
//                        order.getId(),
//                        order.getMarket(),
//                        order.getSide(),
//                        order.getAmount(),
//                        order.getStatus(),
//                        order.getCreatedAt()
//                )).toList();
    }

    public OrderResponse getOrder(Long id) {
        Order order = findOrderOrThrow(id);

        return OrderResponse.from(order);
//        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("주문 없음"));
    }

    public OrderResponse markAsOrdered(Long id) {
        Order order = findOrderOrThrow(id);
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("주문 없음"));

        order.executeOrder();

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
//        return new OrderResponse(
//                savedOrder.getId(),
//                savedOrder.getMarket(),
//                savedOrder.getSide(),
//                savedOrder.getAmount(),
//                savedOrder.getStatus(),
//                savedOrder.getCreatedAt()
//        );
    }

    public OrderResponse cancelOrder(Long id) {

        Order order = findOrderOrThrow(id);
//        Order order = orderRepository.findById(id)
//                .orElseThrow(()-> new NotFoundException("주문 없음"));

        order.cancelOrder();

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
//        return new OrderResponse(
//                savedOrder.getId(),
//                savedOrder.getMarket(),
//                savedOrder.getSide(),
//                savedOrder.getAmount(),
//                savedOrder.getStatus(),
//                savedOrder.getCreatedAt()
//        );
    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC, "createdAt"));

        return orderRepository.findByStatus(status, pageable)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }
}
