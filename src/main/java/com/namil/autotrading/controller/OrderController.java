package com.namil.autotrading.controller;

import com.namil.autotrading.dto.OrderPageResponse;
import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.dto.OrderResponse;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public OrderResponse createOrder(@RequestBody @Valid OrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/orders")
    public OrderPageResponse getOrders(@RequestParam(required = false) OrderStatus status,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrders(status, page, size);
    }

    @GetMapping("/orders/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PatchMapping("orders/{id}/ordered")
    public OrderResponse markAsOrdered(@PathVariable Long id) {
        return orderService.markAsOrdered(id);
    }

    @PatchMapping("/orders/{id}/cancel")
    public OrderResponse canccelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @GetMapping("/orders/status/{status}")
    public List<OrderResponse> getOrdersByStatus(@PathVariable OrderStatus status,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrdersByStatus(status, page, size);
    }
}
