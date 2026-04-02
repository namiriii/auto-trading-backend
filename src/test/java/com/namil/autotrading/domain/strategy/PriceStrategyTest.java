package com.namil.autotrading.domain.strategy;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceStrategyTest {

    private final PriceStrategy priceStrategy = new PriceStrategy();

    @Test
    void 현재가격이_목표가격_이하면_true() {
        //given
        StrategyContext context = new StrategyContext(95000000, 0);

        //when
        boolean result = priceStrategy.isSatisfied(context);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void 현재가격이_목표가격_초과면_false() {
        //given
        StrategyContext context = new StrategyContext(105000000, 0);

        //when
        boolean result = priceStrategy.isSatisfied(context);

        //then
        assertThat(result).isFalse();
    }
}
