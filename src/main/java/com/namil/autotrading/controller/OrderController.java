package com.namil.autotrading.controller;

import com.namil.autotrading.dto.OrderRequest;
import com.namil.autotrading.entity.Order;
import com.namil.autotrading.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public Order createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }
}
