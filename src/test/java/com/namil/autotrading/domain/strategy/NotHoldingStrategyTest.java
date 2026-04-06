package com.namil.autotrading.domain.strategy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class NotHoldingStrategyTest {

    @Test
    void 보유중이면_전략_불만족() {
        //given
        NotHoldingStrategy strategy = new NotHoldingStrategy();
        StrategyContext context = new StrategyContext(0,0,0.0,true);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void 보유중이_아니면_전략_만족() {
        //given
        NotHoldingStrategy strategy = new NotHoldingStrategy();
        StrategyContext context = new StrategyContext(0,0,0.0,false);

        //when
        boolean result = strategy.isSatisfied(context);

        //then
        assertThat(result).isTrue();
    }
}
