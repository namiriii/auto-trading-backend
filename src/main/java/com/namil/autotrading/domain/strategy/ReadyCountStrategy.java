package com.namil.autotrading.domain.strategy;

import com.namil.autotrading.config.StrategyConfigProperties;
import com.namil.autotrading.entity.OrderStatus;
import com.namil.autotrading.repository.OrderRepository;
import org.springframework.stereotype.Component;

//READY 주문 개수가 3개 미만이면 만족
@Component
public class ReadyCountStrategy implements OrderStrategy{

    private final StrategyConfigProperties strategyConfigProperties;

    public ReadyCountStrategy(StrategyConfigProperties strategyConfigProperties) {
        this.strategyConfigProperties = strategyConfigProperties;
    }

    @Override
    public StrategyType getType() {
        return StrategyType.READY_COUNT;
    }

    @Override
    public boolean isSatisfied(StrategyContext context) {
        long maxReadyCount = strategyConfigProperties.getReadyCount().getMaxReadyCount();
        return context.getReadyCount() < maxReadyCount;
    }

    @Override
    public String getName() {
        return "READY_COUNT";
    }
}
