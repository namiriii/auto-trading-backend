package com.namil.autotrading.domain.strategy;

import com.namil.autotrading.config.StrategyConfigProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

//가격 전략 : 현재 가격이 목표 가격 이하이면 만족
@Component
public class PriceStrategy implements OrderStrategy{

    private final StrategyConfigProperties strategyConfigProperties;

    public PriceStrategy(StrategyConfigProperties strategyConfigProperties) {
        this.strategyConfigProperties = strategyConfigProperties;
    }

    @Override
    public StrategyType getType() {
        return StrategyType.PRICE;
    }

    @Override
    public boolean isSatisfied(StrategyContext context) {
        int currentPrice = context.getCurrentPrice();;
        int targetPrice = strategyConfigProperties.getPrice().getTargetPrice();

        return currentPrice <= targetPrice;
    }

    @Override
    public String getName() {
        return "PRICE";
    }
}
