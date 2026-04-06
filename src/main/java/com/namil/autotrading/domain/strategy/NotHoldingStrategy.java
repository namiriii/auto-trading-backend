package com.namil.autotrading.domain.strategy;

import org.hibernate.query.Order;
import org.springframework.stereotype.Component;

@Component
public class NotHoldingStrategy implements OrderStrategy {



    @Override
    public StrategyType getType() {
        return StrategyType.NOT_HOLDING;
    }

    @Override
    public boolean isSatisfied(StrategyContext context) {
        return !context.isHolding();
    }

    @Override
    public String getName() {
        return "NOT_HOLDING";
    }
}
