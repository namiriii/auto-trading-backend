package com.namil.autotrading.domain.strategy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadyCountStrategyTest {

    private final ReadyCountStrategy readyCountStrategy = new ReadyCountStrategy();

    @Test
    void READY가_3개_미만이면_true() {
        //given
        StrategyContext context = new StrategyContext(0, 2, 0.0);

        //when
        boolean result = readyCountStrategy.isSatisfied(context);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void READY가_3개_이상이면_false() {
        //given
        StrategyContext context = new StrategyContext(0, 3, 0.0);

        //when
        boolean result = readyCountStrategy.isSatisfied(context);

        //then
        assertThat(result).isFalse();
    }
}
