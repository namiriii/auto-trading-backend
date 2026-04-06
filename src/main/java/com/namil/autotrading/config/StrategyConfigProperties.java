package com.namil.autotrading.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.strategy-config")
public class StrategyConfigProperties {

    private Price price = new Price();
    private ReadyCount readyCount = new ReadyCount();
    private MovingAverage movingAverage = new MovingAverage();

    public static class Price {
        private int targetPrice;

        public int getTargetPrice() {
            return targetPrice;
        }
        public void setTargetPrice(int targetPrice) {
            this.targetPrice = targetPrice;
        }
    }

    public static class ReadyCount {
        private long maxReadyCount;

        public long getMaxReadyCount() {
            return maxReadyCount;
        }
        public void setMaxReadyCount(long maxReadyCount) {
            this.maxReadyCount = maxReadyCount;
        }
    }

    public static class MovingAverage {
        private int period;

        public int getPeriod() {
            return period;
        }
        public void setPeriod(int period) {
            this.period = period;
        }
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public ReadyCount getReadyCount() {
        return readyCount;
    }

    public void setReadyCount(ReadyCount readyCount) {
        this.readyCount = readyCount;
    }

    public MovingAverage getMovingAverage() {
        return movingAverage;
    }

    public void setMovingAverage(MovingAverage movingAverage) {
        this.movingAverage = movingAverage;
    }
}
