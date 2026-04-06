package com.namil.autotrading.domain.strategy;

import com.namil.autotrading.config.StrategyConfigProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadyCountStrategyTest {

    @Test
    void READY가_3개_미만이면_true() {
        //given
        StrategyConfigProperties properties = new StrategyConfigProperties();
        StrategyConfigProperties.ReadyCount readyCount = new StrategyConfigProperties.ReadyCount();
        readyCount.setMaxReadyCount(3);
        properties.setReadyCount(readyCount);

        ReadyCountStrategy strategy = new ReadyCountStrategy(properties);
        StrategyContext context = new StrategyContext(0, 2, 0.0);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void READY가_3개_이상이면_false() {
        //given
        StrategyConfigProperties properties = new StrategyConfigProperties();
        StrategyConfigProperties.ReadyCount readyCount = new StrategyConfigProperties.ReadyCount();
        readyCount.setMaxReadyCount(3);
        properties.setReadyCount(readyCount);

        ReadyCountStrategy strategy = new ReadyCountStrategy(properties);
        StrategyContext context = new StrategyContext(0, 3, 0.0);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isFalse();
    }
}
