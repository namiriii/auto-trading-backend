package com.namil.autotrading.entity;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

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
    private LocalDateTime orderedAt;
    private LocalDateTime canceledAt;

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

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    // READY 상태의 주문을 실제 주문(ORDER) 상태로 변경
    public void executeOrder() {
        if(this.status != OrderStatus.READY) {
            throw new IllegalStateException("READY 상태만 주문 가능");
        }
        this.status = OrderStatus.ORDERED;
        this.orderedAt = LocalDateTime.now();
    }

    // READY 상태의 주문만 취소(CANCELED) 상태로 변경
    public void cancelOrder() {
        if(this.status != OrderStatus.READY) {
            throw new IllegalStateException("READY 상태의 주문만 취소할 수 있습니다.");
        }
        this.status = OrderStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }
}
