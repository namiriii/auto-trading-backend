package com.namil.autotrading.domain.strategy;

import org.springframework.stereotype.Component;

@Component
public class MovingAverageStrategy implements OrderStrategy{


    @Override
    public StrategyType getType() {
        return StrategyType.MOVING_AVERAGE;
    }

    @Override
    public boolean isSatisfied(StrategyContext context) {
        return context.getCurrentPrice() <= context.getAveragePrice();
    }

    @Override
    public String getName() {
        return "MOVING_AVERAGE";
    }
}
