package com.namil.autotrading.dto;

import com.namil.autotrading.entity.OrderSide;

import java.math.BigDecimal;

public class OrderRequest {

    private String market;
    private OrderSide side;
    private BigDecimal amount;

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
