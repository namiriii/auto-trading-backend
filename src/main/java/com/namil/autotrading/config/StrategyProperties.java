package com.namil.autotrading.config;

import com.namil.autotrading.domain.strategy.StrategyType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app")
public class StrategyProperties {

    private List<StrategyType> strategies;

    public List<StrategyType> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<StrategyType> strategies) {
        this.strategies = strategies;
    }
}
