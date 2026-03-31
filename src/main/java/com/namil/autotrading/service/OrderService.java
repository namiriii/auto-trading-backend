package com.namil.autotrading.service;

import com.namil.autotrading.dto.OrderPageResponse;
import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.dto.OrderResponse;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.exception.NotFoundException;
import com.namil.autotrading.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
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

    //주문 목록 조회(페이지 + 정렬 + 상태 필터)
    public OrderPageResponse getOrders(OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdAt"));

        Page<Order> orders;

        if(status == null) {
            //전체 주문 주회
            orders = orderRepository.findAll(pageable);
        } else {
            //상태별 주문 조회
            orders = orderRepository.findByStatus(status, pageable);
        }

        return new OrderPageResponse(
                orders.getContent().stream()
                        .map(OrderResponse::from)
                        .toList(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages()
        );

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
