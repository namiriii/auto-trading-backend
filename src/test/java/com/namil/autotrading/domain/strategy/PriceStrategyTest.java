package com.namil.autotrading.domain.strategy;


import com.namil.autotrading.config.StrategyConfigProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceStrategyTest {

    @Test
    void 현재가격이_목표가격_이하면_true() {
        //given
        StrategyConfigProperties properties = new StrategyConfigProperties();
        StrategyConfigProperties.Price price = new StrategyConfigProperties.Price();
        price.setTargetPrice(100000000);
        properties.setPrice(price);

        PriceStrategy strategy = new PriceStrategy(properties);
        StrategyContext context = new StrategyContext(95000000, 0, 100000000.0,false);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void 현재가격이_목표가격_초과면_false() {
        //given
        StrategyConfigProperties properties = new StrategyConfigProperties();
        StrategyConfigProperties.Price price = new StrategyConfigProperties.Price();
        price.setTargetPrice(100000000);
        properties.setPrice(price);

        PriceStrategy strategy = new PriceStrategy(properties);
        StrategyContext context = new StrategyContext(105000000, 0, 100000000.0,false);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isFalse();
    }
}
