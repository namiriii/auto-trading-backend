package com.namil.autotrading.dto;

import com.namil.autotrading.entity.Order;
import com.namil.autotrading.entity.OrderSide;
import com.namil.autotrading.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private String market;
    private OrderSide side;
    private BigDecimal amount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderResponse(Long id,
                         String market,
                         OrderSide side,
                         BigDecimal amount,
                         OrderStatus status,
                         LocalDateTime createdAt) {
        this.id = id;
        this.market = market;
        this.side = side;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getMarket(),
                order.getSide(),
                order.getAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getMarket() {
        return market;
    }

    public OrderSide getSide() {
        return side;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
