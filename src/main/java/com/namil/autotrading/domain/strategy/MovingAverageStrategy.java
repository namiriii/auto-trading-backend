package com.namil.autotrading.domain.strategy;

import com.namil.autotrading.config.StrategyConfigProperties;
import org.springframework.stereotype.Component;

@Component
public class MovingAverageStrategy implements OrderStrategy{

    private final StrategyConfigProperties strategyConfigProperties;

    public MovingAverageStrategy(StrategyConfigProperties strategyConfigProperties) {
        this.strategyConfigProperties = strategyConfigProperties;
    }


    @Override
    public StrategyType getType() {
        return StrategyType.MOVING_AVERAGE;
    }

    @Override
    public boolean isSatisfied(StrategyContext context) {
        int period = strategyConfigProperties.getMovingAverage().getPeriod();
        return context.getCurrentPrice() <= context.getAveragePrice();
    }

    @Override
    public String getName() {
        return "MOVING_AVERAGE";
    }
}
