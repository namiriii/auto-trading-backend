package com.namil.autotrading.domain.strategy;

//전략 판단에 필요한 공통 데이터
public class StrategyContext {

    private final int currentPrice;
    private final long readyCount;

    public StrategyContext(int currentPrice, long readyCount) {
        this.currentPrice = currentPrice;
        this.readyCount = readyCount;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public long getReadyCount() {
        return readyCount;
    }
}
