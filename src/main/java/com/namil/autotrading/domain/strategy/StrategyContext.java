package com.namil.autotrading.domain.strategy;

//전략 판단에 필요한 공통 데이터
public class StrategyContext {

    private final int currentPrice;

    public StrategyContext(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }
}
