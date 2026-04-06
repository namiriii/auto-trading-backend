package com.namil.autotrading.domain.strategy;

//전략 판단에 필요한 공통 데이터
public class StrategyContext {

    private final int currentPrice;
    private final long readyCount;
    private final double averagePrice;

    public StrategyContext(int currentPrice, long readyCount, double averagePrice) {
        this.currentPrice = currentPrice;
        this.readyCount = readyCount;
        this.averagePrice = averagePrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public long getReadyCount() {
        return readyCount;
    }

    public double getAveragePrice() {
        return averagePrice;
    }
}
