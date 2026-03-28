package com.namil.autotrading.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String market;

    @Enumerated(EnumType.STRING)
    private OrderSide side;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    protected Order() {
    }

    public Order(String market, OrderSide side, BigDecimal amount, OrderStatus status) {
        this.market = market;
        this.side = side;
        this.amount = amount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    //getter

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
