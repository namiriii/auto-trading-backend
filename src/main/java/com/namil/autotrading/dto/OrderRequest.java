package com.namil.autotrading.dto;

import com.namil.autotrading.entity.OrderSide;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderRequest {

    @NotBlank(message = "마켓은 필수입니다.")
    private String market;

    @NotNull(message = "주문 타입은 필수입니다.")
    private OrderSide side;

    @NotNull(message = "금액은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "금액은 0보다 커야 합니다.")
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
